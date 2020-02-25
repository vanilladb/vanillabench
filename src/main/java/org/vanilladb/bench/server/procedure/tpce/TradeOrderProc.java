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
package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.param.tpce.TradeOrderParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

/**
 * Inputs: acct_id, co_name, exec_f_name, exec_l_name, exec_tax_id, is_lifo
 * issue, requested_price, roll_it_back, st_pending_id, st_submitted_id,
 * symbol, trade_qty, trade_type_id, type_is_margin
 * 
 * Outputs: buy_value, sell_value, status, tax_amount, trade_id
 * 
 * @author SLMT
 *
 */
public class TradeOrderProc extends StoredProcedure<TradeOrderParamHelper> {
	
	String acctName, custFName, custLName, taxId, brokerName, exchId, sName, statusId;
	long brokerId, custId, coId;
	int taxStatus, custTier, typeIsMarket, typeIsSell;
	double marketPrice;

	public TradeOrderProc() {
		super(new TradeOrderParamHelper());
	}

	@Override
	protected void executeSql() {
		frame1();
		frame2();
		frame3();
		frame4();
		
		// TODO: frame5: force rolling back by the client
	}
	
	/**
	 * Get customer, customer account, and broker information
	 */
	private void frame1() {
		TradeOrderParamHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();
		
		// SELECT acct_name = ca_name, broker_id = ca_b_id, 
		// cust_id = ca_c_id, tax_status = ca_tax_st FROM
		// customer_account WHERE ca_id = acct_id
		String sql = "SELECT ca_name, ca_b_id, ca_c_id, ca_tax_st FROM customer_account"
				+ " WHERE ca_id = " + paramHelper.getAcctId();
		Scan s = StoredProcedureHelper.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		acctName = (String) s.getVal("ca_name").asJavaVal();
		brokerId = (Long) s.getVal("ca_b_id").asJavaVal();
		custId = (Long) s.getVal("ca_c_id").asJavaVal();
		taxStatus = (Integer) s.getVal("ca_tax_st").asJavaVal();
		s.close();
		
		// TODO: Add this
		// num_found = row_count
		
		// SELECT cust_f_name = c_f_name, cust_l_name = c_l_name, 
		// cust_tier = c_tier, tax_id = c_tax_id FROM
		// customer WHERE c_id = cust_id
		sql = "SELECT c_f_name, c_l_name, c_tier, c_tax_id FROM customer"
				+ " WHERE c_id = " + custId;
		s = StoredProcedureHelper.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		custFName = (String) s.getVal("c_f_name").asJavaVal();
		custLName = (String) s.getVal("c_l_name").asJavaVal();
		custTier = (Integer) s.getVal("c_tier").asJavaVal();
		taxId = (String) s.getVal("c_tax_id").asJavaVal();
		s.close();
		
		// SELECT broker_name = b_name FROM broker WHERE b_id = broker_id
		sql = "SELECT b_name FROM broker WHERE b_id = " + brokerId;
		s = StoredProcedureHelper.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		brokerName = (String) s.getVal("b_name").asJavaVal();
		s.close();
	}
	
	/**
	 * Check executor's permission
	 */
	private void frame2() {
		// TODO: This has been skipped in simplified version
		// SELECT ap_acl = ap_acl FROM account_permission WHERE
		// ap_ca_id = acct_id, ap_f_name = exec_f_name,
		// ap_l_name = exec_l_name, ap_tax_id = exec_tax_id
	}
	
	/**
	 * Estimate overall effects of the trade
	 */
	private void frame3() {
		TradeOrderParamHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();
		
		// ===== Simplified Version =====
		
		// SELECT co_id = s_co_id, exch_id = s_ex_id, s_name = s_name
		// FROM security WHERE s_symb = symbol
		String sql = "SELECT s_co_id, s_ex_id, s_name FROM security WHERE "
				+ "s_symb = " + paramHelper.getSymbol();
		Scan s = StoredProcedureHelper.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		coId = (Long) s.getVal("s_co_id").asJavaVal();
		exchId = (String) s.getVal("s_ex_id").asJavaVal();
		sName = (String) s.getVal("s_name").asJavaVal();
		s.close();
		
		// SELECT market_price = lt_price FROM last_trade
		// WHERE lt_s_symb = symbol
		sql = "SELECT lt_price FROM last_trade WHERE "
				+ "lt_s_symb = " + paramHelper.getSymbol();
		s = StoredProcedureHelper.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		marketPrice = (Double) s.getVal("lt_price").asJavaVal();
		s.close();
		
		// SELECT type_is_market = tt_is_mrkt, type_is_sell = tt_is_sell
		// FROM trade_type WHERE tt_id = trade_type_id
		sql = "SELECT tt_is_mrkt, tt_is_sell FROM trade_type WHERE "
				+ "tt_id = " + paramHelper.getTradeTypeId();
		s = StoredProcedureHelper.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		typeIsMarket = (Integer) s.getVal("tt_is_mrkt").asJavaVal();
		typeIsSell = (Integer) s.getVal("tt_is_sell").asJavaVal();
		s.close();
		
		if (typeIsMarket == 1) {
			statusId = "A";
		} else {
			statusId = "B";
		}
		
		
		// TODO: Implement this version
		// ===== Full Version =====
		
		// Get information on the security
		if (true) {
			// SELECT co_id = co_id FROM company WHERE co_name = co_name
		
			// SELECT exch_id = s_ex_id, s_name = s_name, symbol = s_symb
			// FROM security WHERE s_co_id = co_id AND s_issue = issue
		} else {
			// SELECT co_id = s_co_id, exch_id = s_ex_id, s_name = s_name
			// FROM security WHERE s_symb = symbol
			
			// SELECT co_name = co_name FROM company WHERE co_id = co_id
		}
		
		// Get current pricing information for the security
		// SELECT market_price = lt_price FROM last_trade
		// WHERE lt_s_symb = symbol
		
		// Set trade characteristics based on the type of trade
		// SELECT type_is_market = tt_is_mrkt, type_is_sell = tt_is_sell
		// FROM trade_type WHERE tt_id = trade_type_id
		
		// If this is a limit-order, then the requested_price was passed in to the frame,
		// but if this a market-order, then the requested_price needs to be set to the
		// current market price.
//		if( type_is_market ) then {
//			requested_price = market_price
//		}
		
		// TODO: Estimation
	}
	
	/**
	 * Record the trade request by making all related updates
	 */
	private void frame4() {
		TradeOrderParamHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();
		long currentTime = System.currentTimeMillis();
		
		// XXX: Lots of dummy value
		// Record trade information in TRADE table.
		// INSERT INTO trade (t_id, t_dts, t_st_id, t_tt_id, t_is_cash,
		// t_s_symb, t_qty, t_bid_price, t_ca_id, t_exec_name, t_trade_price,
		// t_chrg, t_comm, t_tax, t_lifo) VALUES (...)
		String sql = String.format("INSERT INTO trade (t_id, t_dts, t_st_id, t_tt_id, "
				+ "t_is_cash, t_s_symb, t_qty, t_bid_price, t_ca_id, t_exec_name, "
				+ "t_trade_price, t_chrg, t_comm, t_tax, t_lifo) VALUES (%d, %d, '%s', "
				+ "'%s', %d, '%s', %d, %f, %d, '%s', %f, %f, %f, %f, %d)",
				paramHelper.getTradeId(), currentTime, statusId, 
				paramHelper.getTradeTypeId(), 1, paramHelper.getSymbol(),
				paramHelper.getTradeQty(), marketPrice, paramHelper.getAcctId(), "exec_name",
				paramHelper.getTradePrice(), 0.0, 0.0, 0.0, 1);
		StoredProcedureHelper.executeUpdate(sql, tx);
		
		// TODO: Implement this (not in the simplified version)
		// Record pending trade information in TRADE_REQUEST table 
		// if this trade is a limit trade
		// INSERT INTO trade_request (tr_t_id, tr_tt_id, tr_s_symb, tr_qty,
		// tr_bid_price, tr_b_id) VALUES (...)
		
		// Record trade information in TRADE_HISTORY table
		// INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES (...)
		sql = String.format("INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES "
				+ "(%d, %d, '%s')",  paramHelper.getTradeId(), currentTime, statusId);
		StoredProcedureHelper.executeUpdate(sql, tx);
	}
}
