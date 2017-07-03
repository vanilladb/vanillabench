package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.param.tpce.TradeResultParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.query.algebra.Scan;

public class TradeResultProc extends BasicStoredProcedure<TradeResultParamHelper> {
	
	public TradeResultProc() {
		super(new TradeResultParamHelper());
	}

	@Override
	protected void executeSql() {
		// SELECT ca_name, ca_b_id, ca_c_id FROM customer_account WHERE
		// ca_id = acctId
		String sql = "SELECT ca_name, ca_b_id, ca_c_id FROM customer_account WHERE "
				+ "ca_id = " + paramHelper.getAcctId();
		Scan s = executeQuery(sql);
		s.getVal("ca_name").asJavaVal();
		s.getVal("ca_b_id").asJavaVal();
		s.getVal("ca_c_id").asJavaVal();

		// SELECT c_name FROM customer WHERE c_id = customerKey
		sql = "SELECT c_f_name FROM customer WHERE c_id = " + paramHelper.getCustomerId();
		s = executeQuery(sql);
		s.getVal("c_f_name").asJavaVal();

		// SELECT b_name FROM broker WHERE b_id = brokerId
		sql = "SELECT b_name FROM broker WHERE b_id = " + paramHelper.getBrokerId();
		s = executeQuery(sql);
		s.getVal("b_name").asJavaVal();

		// SELECT t_trade_price FROM trade WHERE t_id = tradeId
		sql = "SELECT t_trade_price FROM trade WHERE t_id = " + paramHelper.getTradeId();
		s = executeQuery(sql);
		s.getVal("t_trade_price").asJavaVal();

		// INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES (...)
		long currentTime = System.currentTimeMillis();
		sql = String.format("INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES "
				+ "(%d, %d, '%s')",  paramHelper.getTradeId(), currentTime, "A");
		executeUpdate(sql);

		// UPDATE customer_account SET ca_bal = ca_bal + tradePrice WHERE
		// ca_id = acctId
		sql = String.format("UPDATE customer_account SET ca_bal = %f WHERE ca_id = %d", 
				1000.0, paramHelper.getAcctId());
		executeUpdate(sql);
	}
}
