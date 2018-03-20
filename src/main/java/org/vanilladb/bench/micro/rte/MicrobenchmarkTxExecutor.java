package org.vanilladb.bench.micro.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.micro.MicroTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;

public class MicrobenchmarkTxExecutor extends TransactionExecutor<MicroTransactionType> {

	public MicrobenchmarkTxExecutor(MicrobenchmarkParamGen pg) {
		this.pg = pg;
	}

	public TxnResultSet execute(SutConnection conn) {
		try {
			TxnResultSet rs = new TxnResultSet();
			rs.setTxnType(pg.getTxnType());

			// generate parameters
			Object[] params = pg.generateParameter();

			// send txn request and start measure txn response time
			long txnRT = System.nanoTime();
			
			SutResultSet result = executeTxn(conn, params);

			// measure txn response time
			txnRT = System.nanoTime() - txnRT;

			// display output
			if (TransactionExecutor.DISPLAY_RESULT)
				System.out.println(pg.getTxnType() + " " + result.outputMsg());

			rs.setTxnIsCommited(result.isCommitted());
			rs.setOutMsg(result.outputMsg());
			rs.setTxnResponseTimeNs(txnRT);
			rs.setTxnEndTime();

			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	protected JdbcExecutor<MicroTransactionType> getJdbcExecutor() {
		throw new UnsupportedOperationException("no JDCB implementation for the micro-benchmarks");
	}
}
