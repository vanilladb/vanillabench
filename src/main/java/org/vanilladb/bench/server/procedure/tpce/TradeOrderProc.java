package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.param.tpce.TradeOrderParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;

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
public class TradeOrderProc extends BasicStoredProcedure<TradeOrderParamHelper> {

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
		// SELECT acct_name = ca_name, broker_id = ca_b_id, 
		// cust_id = ca_c_id, tax_status = ca_tax_st FROM
		// customer_account WHERE ca_id = acct_id
		
		// num_found = row_count
		
		// SELECT cust_f_name = c_f_name, cust_l_name = c_l_name, 
		// cust_tier = c_tier, tax_id = c_tax_id FROM
		// customer WHERE c_id = cust_id
		
		// SELECT broker_name = b_name FROM broker WHERE b_id = broker_id
	}
	
	/**
	 * Check executor's permission
	 */
	private void frame2() {
		// SELECT ap_acl = ap_acl FROM account_permission WHERE
		// ap_ca_id = acct_id, ap_f_name = exec_f_name,
		// ap_l_name = exec_l_name, ap_tax_id = exec_tax_id
	}
	
	/**
	 * Estimate overall effects of the trade
	 */
	private void frame3() {
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
		// SELECT type_is_market = tt_is_mark, type_is_sell = tt_is_sell
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
		// Record trade information in TRADE table.
		// INSERT INTO trade (t_id, t_dts, t_st_id, t_tt_id, t_is_cash,
		// t_s_symb, t_qty, t_bid_price, t_ca_id, t_exec_name, t_trade_price,
		// t_chrg, t_comm, t_tax, t_lifo) VALUES (...)
		
		// Record pending trade information in TRADE_REQUEST table 
		// if this trade is a limit trade
		// INSERT INTO trade_request (tr_t_id, tr_tt_id, tr_s_symb, tr_qty,
		// tr_bid_price, tr_b_id) VALUES (...)
		
		// Record trade information in TRADE_HISTORY table
		// INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES (...)
	}
}
