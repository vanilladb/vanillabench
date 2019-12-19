package org.vanilladb.bench.benchmarks.ycsb.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.benchmarks.ycsb.YcsbTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;

public class YcsbTxExecutor extends TransactionExecutor<YcsbTransactionType> {
	
	public YcsbTxExecutor(TxParamGenerator<YcsbTransactionType> pg) {
		this.pg = pg;
	}
	
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
	protected JdbcExecutor<YcsbTransactionType> getJdbcExecutor() {
		throw new UnsupportedOperationException("no JDCB implementation for YCSB");
	}
}
