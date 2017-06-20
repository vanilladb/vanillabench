package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.param.tpcc.PaymentProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.server.VanillaDb;

public class PaymentProc extends BasicStoredProcedure<PaymentProcParamHelper> {

	public PaymentProc() {
		super(new PaymentProcParamHelper());
	}

	@Override
	protected void executeSql() {
		int wid = paramHelper.getWid();
		int did = paramHelper.getDid();
		int cid = paramHelper.getcid();
		int cwid = paramHelper.getCwid();
		int cdid = paramHelper.getCdid();
		double hAmount = paramHelper.getHamount();
		
		// SELECT w_name, w_street_1, w_street_2, w_city, w_state, w_zip, w_ytd
		// FROM warehouse WHERE w_id = wid;
		String sql = "SELECT w_name, w_street_1, w_street_2, w_city,w_state, w_zip, w_ytd "
				+ "FROM warehouse WHERE w_id = " + wid;
		Scan s = executeQuery(sql);
		String wName = (String) s.getVal("w_name").asJavaVal();
		s.getVal("w_street_1").asJavaVal();
		s.getVal("w_street_2").asJavaVal();
		s.getVal("w_city").asJavaVal();
		s.getVal("w_state").asJavaVal();
		s.getVal("w_zip").asJavaVal();
		double wYtd = (Double) s.getVal("w_ytd").asJavaVal();
		s.close();
		
		// UPDATE warehouse SET w_ytd = (wYtd + hAmount) WHERE w_id = + wid;
		sql = "UPDATE warehouse SET w_ytd = " + 
				String.format("%f", wYtd + hAmount) +
				" WHERE w_id = " + wid;
		executeUpdate(sql);
		
		// SELECT d_name, d_street_1, d_street_2, d_city, d_state, d_zip, d_ytd
		// FROM district WHERE d_w_id = wid AND d_id = did;
		sql = "SELECT d_name, d_street_1, d_street_2, d_city, d_state, d_zip, d_ytd "
				+ "FROM district WHERE d_w_id = " + wid + " AND d_id = " + did;
		s = executeQuery(sql);
		String dName = (String) s.getVal("d_name").asJavaVal();
		s.getVal("d_street_1").asJavaVal();
		s.getVal("d_street_2").asJavaVal();
		s.getVal("d_city").asJavaVal();
		s.getVal("d_state").asJavaVal();
		s.getVal("d_zip").asJavaVal();
		s.getVal("d_ytd").asJavaVal();
		double dYtd = (Double) s.getVal("d_ytd").asJavaVal();
		s.close();
		
		// UPDATE district SET d_ytd = (dYtd + hAmount) WHERE d_w_id = wid AND d_id = did
		sql = "UPDATE district SET d_ytd = " + 
				String.format("%f", dYtd + hAmount) +
				" WHERE d_w_id = " + wid + " AND d_id = " + did;
		executeUpdate(sql);
		
		// TODO: Add select by name
		
		// Select by cid
		
		// SELECT c_first, c_middle, c_last, c_street_1, c_street_2, c_city,
		// c_state, c_zip, c_phone, c_credit, c_credit_lim,
		// c_discount, c_balance, c_since FROM customer
		// WHERE c_w_id = cwid AND c_d_id = cdid AND c_id = cid;
		sql = "SELECT c_first, c_middle, c_last, c_street_1, c_street_2, c_city, " +
				"c_state, c_zip, c_phone, c_credit, c_credit_lim, " +
				"c_discount, c_balance, c_since FROM customer " +
				"WHERE c_w_id = " + cwid + " AND c_d_id = " + cdid + " AND c_id = " + cid;
		s = executeQuery(sql);
		paramHelper.setcFirst((String) s.getVal("c_first").asJavaVal());
		paramHelper.setcMiddle((String) s.getVal("c_middle").asJavaVal());
		paramHelper.setcLast((String) s.getVal("c_last").asJavaVal());
		paramHelper.setcStreet1((String) s.getVal("c_street_1").asJavaVal());
		paramHelper.setcStreet2((String) s.getVal("c_street_2").asJavaVal());
		paramHelper.setcCity((String) s.getVal("c_city").asJavaVal());
		paramHelper.setcState((String) s.getVal("c_state").asJavaVal());
		paramHelper.setcZip((String) s.getVal("c_zip").asJavaVal());
		paramHelper.setcPhone((String) s.getVal("c_phone").asJavaVal());
		String cCredit = (String) s.getVal("c_credit").asJavaVal();
		paramHelper.setcCredit(cCredit);
		paramHelper.setcCreditLim((double) s.getVal("c_credit_lim").asJavaVal());
		paramHelper.setcDiscount((double) s.getVal("c_discount").asJavaVal());
		double cBalance = (double) s.getVal("c_balance").asJavaVal();
		paramHelper.setcBalance(cBalance);
		paramHelper.setcSince((long) s.getVal("c_since").asJavaVal());
		s.close();
		
		cBalance += hAmount;
		
		if (cCredit.equals("BC")) {
			paramHelper.setisBadCredit(true);
			
			// SELECT c_data FROM customer WHERE c_w_id = cwid AND
			// c_d_id = cdid AND c_id = cid;
			sql = "SELECT c_data FROM customer WHERE c_w_id = " + cwid +
					" AND c_d_id = " + cdid + " AND c_id = " + cid;
			s = executeQuery(sql);
			String cData = (String) s.getVal("c_data").asJavaVal();
			s.close();
			
			String cNewData = String.format("| %4d %2d %4d %2d %4d $%7.2f ",
					cid, cdid, cwid, did, wid, hAmount);
			cNewData += cData;
			if (cNewData.length() > 499)
				cNewData = cNewData.substring(0, 499);
			paramHelper.setcData(cNewData);
			
			// UPDATE customer SET c_balance = cBalance, c_data = 'cNewData'
			// WHERE c_w_id = cwid AND c_d_id = cdid AND c_id = cid
			sql = "UPDATE customer SET c_balance = " + 
					String.format("%f", cBalance) +
					", c_data = '" + cNewData + "' WHERE c_w_id = " + cwid +
					" AND c_d_id = " + cdid + " AND c_id = " + cid;
			executeUpdate(sql);
		} else {
			// UPDATE customer SET c_balance = cBalance
			// WHERE c_w_id = cwid AND c_d_id = cdid AND c_id = cid
			sql = "UPDATE customer SET c_balance = " + 
					String.format("%f", cBalance) +
					" WHERE c_w_id = " + cwid +
					" AND c_d_id = " + cdid + " AND c_id = " + cid;
			executeUpdate(sql);
		}
		
		String hData = wName + "    " + dName;
		long hDate = System.currentTimeMillis();
		paramHelper.sethDate(hDate);
		
		// INSERT INTO history (h_c_id, h_c_d_id, h_c_w_id, h_d_id, h_w_id,
		// h_date, h_amount, h_data) VALUES (cid, cdid, cwid, did, wid, hDate,
		// hAmount, hData)
		sql = String.format("INSERT INTO history (h_c_id, h_c_d_id, h_c_w_id, h_d_id, "
				+ "h_w_id, h_date, h_amount, h_data) VALUES ( %d, %d, %d, %d, "
				+ "%d, %d, %f, '%s')",
				cid, cdid, cwid, did, wid, hDate, hAmount, hData);
		executeUpdate(sql);
	}
	
	private Scan executeQuery(String sql) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		Scan s = p.open();
		s.beforeFirst();
		if (s.next()) {
			return s;
		} else
			throw new RuntimeException("Query: " + sql + " fails.");
	}
	
	private void executeUpdate(String sql) {
		int count = VanillaDb.newPlanner().executeUpdate(sql, tx);
		
		if (count > 1)
			throw new RuntimeException("Update: " + sql + " affect more than 1 record.");
		else if (count < 1)
			throw new RuntimeException("Update: " + sql + " fails.");
	}
}
