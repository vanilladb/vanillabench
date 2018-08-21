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
package org.vanilladb.bench.benchmarks.tpcc.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.benchmarks.tpcc.TpccTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;
import org.vanilladb.bench.util.BenchProperties;

public class TpccTxExecutor extends TransactionExecutor<TpccTransactionType> {

	private final static boolean ENABLE_THINK_AND_KEYING_TIME;

	static {
		ENABLE_THINK_AND_KEYING_TIME = BenchProperties.getLoader()
				.getPropertyAsBoolean(TpccTxExecutor.class.getName() + ".ENABLE_THINK_AND_KEYING_TIME", false);
	}
	
	private TpccTxParamGenerator tpccPg;

	public TpccTxExecutor(TpccTxParamGenerator pg) {
		this.pg = pg;
		tpccPg = pg;
	}

	@Override
	public TxnResultSet execute(SutConnection conn) {
		try {
			TxnResultSet rs = new TxnResultSet();
			rs.setTxnType(pg.getTxnType());

			// keying
			if (ENABLE_THINK_AND_KEYING_TIME) {
				// wait for a keying time and generate parameters
				long t = tpccPg.getKeyingTime();
				rs.setKeyingTime(t);
				Thread.sleep(t);
			} else
				rs.setKeyingTime(0);

			// generate parameters
			Object[] params = pg.generateParameter();

			// send txn request and start measure txn response time
			long txnRT = System.nanoTime();
			
			SutResultSet result = executeTxn(conn, params);

			// measure txn Sresponse time
			txnRT = System.nanoTime() - txnRT;

			// display output
			if (TransactionExecutor.DISPLAY_RESULT)
				System.out.println(pg.getTxnType() + " " + result.outputMsg());

			rs.setTxnIsCommited(result.isCommitted());
			rs.setOutMsg(result.outputMsg());
			rs.setTxnResponseTimeNs(txnRT);
			rs.setTxnEndTime();

			// thinking
			if (ENABLE_THINK_AND_KEYING_TIME) {
				// wait for a think time
				long t = tpccPg.getThinkTime();
				Thread.sleep(t);
				rs.setThinkTime(t);
			} else
				rs.setThinkTime(0);

			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	protected JdbcExecutor<TpccTransactionType> getJdbcExecutor() {
		throw new UnsupportedOperationException("no JDCB implementation for TPC-C");
	}
}
