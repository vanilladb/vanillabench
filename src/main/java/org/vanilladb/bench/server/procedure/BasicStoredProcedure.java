/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.vanilladb.bench.server.procedure;

import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.concurrency.LockAbortException;

public abstract class BasicStoredProcedure<H extends StoredProcedureParamHelper> implements StoredProcedure {
	private static Logger logger = Logger.getLogger(BasicStoredProcedure.class
			.getName());
	
	protected H paramHelper;
	protected Transaction tx;
	
	public BasicStoredProcedure(H helper) {
		if (helper == null)
			throw new IllegalArgumentException("paramHelper should not be null");
		
		paramHelper = helper;
	}
	
	@Override
	public void prepare(Object... pars) {
		// prepare parameters
		paramHelper.prepareParameters(pars);
		
		// create a transaction
		boolean isReadOnly = paramHelper.isReadOnly();
		tx = VanillaDb.txMgr().newTransaction(
			Connection.TRANSACTION_SERIALIZABLE, isReadOnly);
	}

	@Override
	public SpResultSet execute() {
		try {
			executeSql();
			
			// The transaction finishes normally
			tx.commit();
			paramHelper.setCommitted(true);
			
		} catch (LockAbortException lockAbortEx) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(lockAbortEx.getMessage());
			tx.rollback();
			paramHelper.setCommitted(false);
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
			paramHelper.setCommitted(false);
		}

		return paramHelper.createResultSet();
	}
	
	protected abstract void executeSql();
	
	protected Scan executeQuery(String sql) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		Scan s = p.open();
		s.beforeFirst();
		if (s.next()) {
			return s;
		} else
			throw new RuntimeException("Query: '" + sql + "' fails.");
	}
	
	protected void executeUpdate(String sql) {
		VanillaDb.newPlanner().executeUpdate(sql, tx);
	}
}
