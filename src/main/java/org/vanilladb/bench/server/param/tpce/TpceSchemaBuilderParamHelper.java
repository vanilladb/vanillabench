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
package org.vanilladb.bench.server.param.tpce;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TpceSchemaBuilderParamHelper extends StoredProcedureParamHelper {

	private static final String TABLES_DDL[] = {
			// Customer Category
			// TODO: watch_item, watch_list
			"CREATE TABLE account_permission ( ap_ca_id LONG, ap_cal VARCHAR(4),"
						+ "ap_tax_id VARCHAR(20), ap_l_name VARCHAR(25), ap_f_name VARCHAR(20) )",
			"CREATE TABLE customer ( c_id LONG, c_tax_id VARCHAR(20), c_st_id VARCHAR(4),"
						+ " c_l_name VARCHAR(25), c_f_name VARCHAR(20), c_m_name VARCHAR(1),"
						+ " c_gndr VARCHAR(1), c_tier INT, c_dob LONG, c_ad_id LONG,"
						+ " c_ctry_1 VARCHAR(3), c_area_1 VARCHAR(3), c_local_1 VARCHAR(10),"
						+ " c_ext_1 VARCHAR(5), c_ctry_2 VARCHAR(3), c_area_2 VARCHAR(3),"
						+ " c_local_2 VARCHAR(10), c_ext_2 VARCHAR(5), c_ctry_3 VARCHAR(3),"
						+ " c_area_3 VARCHAR(3), c_local_3 VARCHAR(10), c_ext_3 VARCHAR(5),"
						+ " c_email_1 VARCHAR(50), c_email_2 VARCHAR(50))",
			"CREATE TABLE customer_account ( ca_id LONG, ca_b_id LONG, ca_c_id LONG,"
						+ " ca_name VARCHAR(50), ca_tax_st INT, ca_bal DOUBLE )",
			"CREATE TABLE customer_taxrate ( cx_tx_id VARCHAR(4), cx_c_id LONG )",
			"CREATE TABLE holding ( h_t_id LONG, h_ca_id LONG, h_s_symb VARCHAR(15),"
						+ " h_dts LONG, h_price DOUBLE, h_qty INT )",
			"CREATE TABLE holding_history ( hh_h_t_id LONG, hh_t_id LONG, hh_before_qty INT, hh_after_qty INT )",
			"CREATE TABLE holding_summary ( hs_ca_id LONG, hs_s_symb VARCHAR(15), hs_qty INT )",
			
			// Broker Category
			// TODO: cash_transaction, settlement
			"CREATE TABLE broker ( b_id LONG, b_st_id VARCHAR(4), b_name VARCHAR(49),"
						+ " b_num_trades INT, b_comm_total DOUBLE )",
			"CREATE TABLE charge ( ch_tt_id VARCHAR(3), ch_c_tier INT, ch_chrg DOUBLE )",
			"CREATE TABLE commission_rate ( cr_c_tier INT, cr_tt_id VARCHAR(3), cr_ex_id VARCHAR(6),"
						+ " cr_from_qty INT, cr_to_qty INT, cr_rate DOUBLE )",
			"CREATE TABLE trade ( t_id LONG, t_dts LONG, t_st_id VARCHAR(4), t_tt_id VARCHAR(3),"
						+ " t_is_cash INT, t_s_symb VARCHAR(15), t_qty INT, t_bid_price DOUBLE,"
						+ " t_ca_id LONG, t_exec_name VARCHAR(49), t_trade_price DOUBLE,"
						+ " t_chrg DOUBLE, t_comm DOUBLE, t_tax DOUBLE, t_lifo INT )",
			"CREATE TABLE trade_history ( th_t_id LONG, th_dts LONG, th_st_id VARCHAR(4) )",
			"CREATE TABLE trade_request ( tr_t_id LONG, tr_tt_id VARCHAR(3), tr_s_symb VARCHAR(15),"
						+ " tr_qty INT, tr_bid_price DOUBLE, tr_b_id LONG )",
			"CREATE TABLE trade_type ( tt_id VARCHAR(3), tt_name VARCHAR(12), tt_is_sell INT,"
						+ " tt_is_mrkt INT )",
						
			// Market Category
			// TODO: company_competitor, daily_market, exchange, financial, industry, news_item,
			// news_xref, sector
			"CREATE TABLE company ( co_id LONG, co_st_id VARCHAR(4), co_name VARCHAR(60),"
						+ " co_in_id VARCHAR(2), co_sp_rate VARCHAR(4), co_ceo VARCHAR(46),"
						+ " co_ad_id LONG, co_desc VARCHAR(150), co_open_date LONG )",
			"CREATE TABLE last_trade ( lt_s_symb VARCHAR(15), lt_dts LONG, lt_price DOUBLE,"
						+ " lt_open_price DOUBLE, lt_vol INT )",
			"CREATE TABLE security ( s_symb VARCHAR(15), s_issue VARCHAR(6),"
						+ "s_st_id VARCHAR(4), s_name VARCHAR(70), s_ex_id VARCHAR(6),"
						+ "s_co_id LONG, s_num_out INT, s_start_date LONG, s_exch_date LONG, "
						+ "s_pe DOUBLE, s_52wk_high DOUBLE, s_52wk_high_date LONG, "
						+ "s_52wk_low DOUBLE, s_52wk_low_date LONG, s_dividend DOUBLE, "
						+ "s_yield DOUBLE )"
			
			// Dimension Category
			// TODO: address, status_type, taxrate, zip_code
						
	};

	private static final String INDEXES_DDL[] = {
			"CREATE INDEX idx_customer_id ON customer (c_id)",
			"CREATE INDEX idx_customer_account_id ON customer_account (ca_id)",
			"CREATE INDEX idx_broker_id ON broker (b_id)",
			"CREATE INDEX idx_security_symbol ON security (s_symb)",
			"CREATE INDEX idx_last_trade_security_symbol ON last_trade (lt_s_symb)",
			"CREATE INDEX idx_trade_type_id ON trade_type (tt_id)" };

	public String[] getTableSchemas() {
		return TABLES_DDL;
	}

	public String[] getIndexSchemas() {
		return INDEXES_DDL;
	}

	@Override
	public void prepareParameters(Object... pars) {
		// nothing to do
	}

	@Override
	public SpResultSet createResultSet() {
		return new SpResultSet(isCommitted, new Schema(), new SpResultRecord());
	}

}
