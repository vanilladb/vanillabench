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
			throw new RuntimeException("Query: " + sql + " fails.");
	}
	
	protected void executeUpdate(String sql) {
		try {
		int count = VanillaDb.newPlanner().executeUpdate(sql, tx);
		
		if (count > 1)
			throw new RuntimeException("Update: " + sql + " affect more than 1 record.");
		else if (count < 1)
			throw new RuntimeException("Update: " + sql + " fails.");
		} catch (Exception e) {
			System.out.println(sql);
			throw e;
		}
	}
}
