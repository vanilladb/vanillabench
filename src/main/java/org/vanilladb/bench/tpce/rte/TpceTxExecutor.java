package org.vanilladb.bench.tpce.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;
import org.vanilladb.bench.tpce.TpceTransactionType;

public class TpceTxExecutor extends TransactionExecutor<TpceTransactionType> {
	
	private TpceTxParamGenerator paramGen;

	public TpceTxExecutor(TpceTxParamGenerator tpcePg) {
		this.pg = tpcePg;
		paramGen = tpcePg;
	}
	
	@Override
	public TxnResultSet execute(SutConnection conn) {
		try {
			TxnResultSet rs = new TxnResultSet();
			rs.setTxnType(pg.getTxnType());

			// generate parameters
			Object[] params = pg.generateParameter();

			// send tx request and start measure tx response time
			long txnRT = System.nanoTime();
			
			SutResultSet result = executeTxn(conn, params);

			// measure txn Sresponse time
			txnRT = System.nanoTime() - txnRT;
			
			// notify the parameter generator
			paramGen.onResponseReceived(result);

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
	protected JdbcExecutor<TpceTransactionType> getJdbcExecutor() {
		throw new UnsupportedOperationException("no JDCB implementation for TPC-E");
	}
}
