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
package org.vanilladb.bench.server.param.tpcc;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TpccSchemaBuilderProcParamHelper extends StoredProcedureParamHelper {

	private final String TABLES_DDL[] = {
			"CREATE TABLE warehouse ( w_id INT, w_name VARCHAR(10), "
					+ "w_street_1 VARCHAR(20), w_street_2 VARCHAR(20), w_city VARCHAR(20), "
					+ "w_state VARCHAR(2), w_zip VARCHAR(9), w_tax DOUBLE,  w_ytd DOUBLE )",
			"CREATE TABLE district ( d_id INT, d_w_id INT, d_name VARCHAR(10), "
					+ "d_street_1 VARCHAR(20), d_street_2 VARCHAR(20), d_city VARCHAR(20), "
					+ "d_state VARCHAR(2), d_zip VARCHAR(9), d_tax DOUBLE, d_ytd DOUBLE, "
					+ "d_next_o_id INT )",
			"CREATE TABLE customer ( c_id INT, c_d_id INT, c_w_id INT, "
					+ "c_first VARCHAR(16), c_middle VARCHAR(2), c_last VARCHAR(16), "
					+ "c_street_1 VARCHAR(20), c_street_2 VARCHAR(20), c_city VARCHAR(20), "
					+ "c_state VARCHAR(2), c_zip VARCHAR(9), c_phone VARCHAR(16), "
					+ "c_since LONG, c_credit VARCHAR(2), c_credit_lim DOUBLE, "
					+ "c_discount DOUBLE, c_balance DOUBLE, c_ytd_payment DOUBLE, "
					+ "c_payment_cnt INT, c_delivery_cnt INT, c_data VARCHAR(500) ) ",
			"CREATE TABLE history ( h_c_id INT, h_c_d_id INT, h_c_w_id INT, "
					+ "h_d_id INT, h_w_id INT, h_date LONG, h_amount DOUBLE, "
					+ "h_data VARCHAR(24) )",
			"CREATE TABLE new_order ( no_o_id INT, no_d_id INT, no_w_id INT )",
			"CREATE TABLE orders ( o_id INT, o_d_id INT, o_w_id INT, "
					+ "o_c_id INT, o_entry_d LONG, o_carrier_id INT, o_ol_cnt INT, "
					+ "o_all_local INT )",
			"CREATE TABLE order_line ( ol_o_id INT, ol_d_id INT, ol_w_id INT, "
					+ "ol_number INT, ol_i_id INT, ol_supply_w_id INT, ol_delivery_d LONG, "
					+ "ol_quantity INT, ol_amount DOUBLE, ol_dist_info VARCHAR(24) )",
			"CREATE TABLE item ( i_id INT, i_im_id INT, i_name VARCHAR(24), "
					+ "i_price DOUBLE, i_data VARCHAR(50) )",
			"CREATE TABLE stock ( s_i_id INT, s_w_id INT, s_quantity INT, "
					+ "s_dist_01 VARCHAR(24), s_dist_02 VARCHAR(24), s_dist_03 VARCHAR(24), "
					+ "s_dist_04 VARCHAR(24), s_dist_05 VARCHAR(24), s_dist_06 VARCHAR(24), "
					+ "s_dist_07 VARCHAR(24), s_dist_08 VARCHAR(24), s_dist_09 VARCHAR(24), "
					+ "s_dist_10 VARCHAR(24), s_ytd INT, s_order_cnt INT, s_remote_cnt INT, "
					+ "s_data VARCHAR(50) )" };
	private final String INDEXES_DDL[] = {
			"CREATE INDEX idx_warehouse ON warehouse (w_id)",
			"CREATE INDEX idx_district ON district (d_id)",
			"CREATE INDEX idx_customer ON customer (c_id)",
			"CREATE INDEX idx_history ON history (h_c_id)",
			"CREATE INDEX idx_order ON orders (o_id)",
			"CREATE INDEX idx_new_order ON new_order (no_o_id)",
			"CREATE INDEX idx_order_line ON order_line (ol_o_id)",
			"CREATE INDEX idx_stock ON stock (s_i_id)",
			"CREATE INDEX idx_item ON item (i_id)" };

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
