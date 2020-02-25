/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
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
package org.vanilladb.bench.benchmarks.tpce.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.benchmarks.tpce.TpceTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;

public class TpceTxExecutor extends TransactionExecutor<TpceTransactionType> {
	
	private TpceTxParamGenerator paramGen;

	public TpceTxExecutor(TpceTxParamGenerator tpcePg) {
		this.pg = tpcePg;
		paramGen = tpcePg;
	}
	
	@Override
	public TxnResultSet execute(SutConnection conn) {
		try {
			// generate parameters
			Object[] params = pg.generateParameter();

			// send txn request and start measure txn response time
			long txnRT = System.nanoTime();
			
			SutResultSet result = executeTxn(conn, params);

			// measure txn response time
			long txnEndTime = System.nanoTime();
			txnRT = txnEndTime - txnRT;
			
			// notify the parameter generator
			paramGen.onResponseReceived(result);

			// display output
			if (TransactionExecutor.DISPLAY_RESULT)
				System.out.println(pg.getTxnType() + " " + result.outputMsg());

			return new TxnResultSet(pg.getTxnType(), txnRT, txnEndTime,
					result.isCommitted(), result.outputMsg());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	protected JdbcExecutor<TpceTransactionType> getJdbcExecutor() {
		throw new UnsupportedOperationException("no JDCB implementation for TPC-E");
	}
}
