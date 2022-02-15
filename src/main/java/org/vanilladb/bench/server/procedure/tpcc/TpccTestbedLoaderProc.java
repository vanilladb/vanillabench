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
package org.vanilladb.bench.server.procedure.tpcc;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.tpcc.TpccConstants;
import org.vanilladb.bench.benchmarks.tpcc.TpccParameters;
import org.vanilladb.bench.benchmarks.tpcc.TpccValueGenerator;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.bench.util.DoublePlainPrinter;
import org.vanilladb.bench.util.RandomPermutationGenerator;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

public class TpccTestbedLoaderProc extends StoredProcedure<StoredProcedureParamHelper> {
	private static Logger logger = Logger.getLogger(TpccTestbedLoaderProc.class.getName());

	private TpccValueGenerator rg = new TpccValueGenerator();

	public TpccTestbedLoaderProc() {
		super(StoredProcedureParamHelper.newDefaultParamHelper());
	}
	
	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.INFO))
			logger.info("Start loading testbed...");

		// turn off logging set value to speed up loading process
		// TODO: remove this hack code in the future
		RecoveryMgr.enableLogging(false);

		// Generate item records
		generateItems(1, TpccConstants.NUM_ITEMS);

		// Generate warehouse
		for (int wid = 1; wid <= TpccParameters.NUM_WAREHOUSES; wid++)
			generateWarehouseInstance(wid);

		if (logger.isLoggable(Level.INFO))
			logger.info("Loading completed. Flush all loading data to disks...");

		// TODO: remove this hack code in the future
		RecoveryMgr.enableLogging(true);

		// Create a checkpoint
		CheckpointTask cpt = new CheckpointTask();
		cpt.createCheckpoint();

		// Delete the log file and create a new one
		VanillaDb.logMgr().removeAndCreateNewLog();

		if (logger.isLoggable(Level.INFO))
			logger.info("Loading procedure finished.");

	}

	private void generateItems(int startIId, int endIId) {
		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating items from i_id=" + startIId + " to i_id=" + endIId);

		int iid, iimid;
		String iname, idata;
		double iprice;
		String sql;
		Transaction tx = getTransaction();
		for (int i = startIId; i <= endIId; i++) {
			iid = i;

			// Randomly generate values
			/*
			 * iimid = rg.number(TpccConstants.MIN_IM, TpccConstants.MAX_IM);
			 * iname = rg.randomAString(TpccConstants.MIN_I_NAME,
			 * TpccConstants.MAX_I_NAME); iprice =
			 * rg.fixedDecimalNumber(TpccConstants.MONEY_DECIMALS,
			 * TpccConstants.MIN_PRICE, TpccConstants.MAX_PRICE); idata =
			 * rg.randomAString(TpccConstants.MIN_I_DATA,
			 * TpccConstants.MAX_I_DATA); if (Math.random() < 0.1) idata =
			 * fillOriginal(idata);
			 */

			// Deterministic value generation by item id
			iimid = iid % (TpccConstants.MAX_IM - TpccConstants.MIN_IM) + TpccConstants.MIN_IM;
			iname = String.format("%0" + TpccConstants.MIN_I_NAME + "d", iid);
			iprice = (iid % (int) (TpccConstants.MAX_PRICE - TpccConstants.MIN_PRICE)) + TpccConstants.MIN_PRICE;
			idata = String.format("%0" + TpccConstants.MIN_I_DATA + "d", iid);

			sql = "INSERT INTO item(i_id, i_im_id, i_name, i_price, i_data) VALUES (" + iid + ", " + iimid + ", '"
					+ iname + "', " + DoublePlainPrinter.toPlainString(iprice) + ", '" + idata + "' )";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}

		if (logger.isLoggable(Level.FINE))
			logger.info("Populating items completed.");
	}

	private void generateWarehouseInstance(int wid) {
		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating warehouse " + wid);

		generateWarehouse(wid);
		generateStocks(wid);
		generateDistricts(wid);

		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating customers for warehouse " + wid);

		int numDist = TpccConstants.DISTRICTS_PER_WAREHOUSE;
		for (int i = 1; i <= numDist; i++)
			generateCustomers(wid, i);
		for (int i = 1; i <= numDist; i++)
			generateCustomerHistory(wid, i);
		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating order for warehouse " + wid);
		for (int i = 1; i <= numDist; i++)
			generateOrders(wid, i);
		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating new order for warehouse " + wid);
		for (int i = 1; i <= numDist; i++)
			generateNewOrders(wid, i);
		if (logger.isLoggable(Level.FINE))
			logger.info("Finish populating warehouse " + wid);
	}

	private void generateWarehouse(int wid) {
		Transaction tx = getTransaction();
		double wtax, wytd;
		String wname, wst1, wst2, wcity, wstate, wzip;
		wname = rg.randomAString(6, 10);
		wst1 = rg.randomAString(10, 20);
		wst2 = rg.randomAString(10, 20);
		wcity = rg.randomAString(10, 20);
		wstate = rg.randomAString(2);
		wzip = makeZip();
		wtax = makeTax();
		wytd = TpccConstants.INITIAL_W_YTD;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO warehouse(w_id, w_name, w_street_1, w_street_2, w_city, w_state, w_zip, w_tax, w_ytd) ");
		sb.append("values (").append(wid).append(", '").append(wname);
		sb.append("', '").append(wst1).append("', '").append(wst2);
		sb.append("', '").append(wcity).append("', '").append(wstate);
		sb.append("', '").append(wzip).append("', ").append(DoublePlainPrinter.toPlainString(wtax));
		sb.append(", ").append(DoublePlainPrinter.toPlainString(wytd)).append(" )");
		StoredProcedureHelper.executeUpdate(sb.toString(), tx);
	}

	private void generateStocks(int wid) {
		Transaction tx = getTransaction();
		int swid = wid, siid, squantity;
		String sd1, sd2, sd3, sd4, sd5, sd6, sd7, sd8, sd9, sd10, sdata;

		for (int i = 1; i <= TpccConstants.NUM_ITEMS; i++) {
			siid = i;
			squantity = rg.number(TpccConstants.MIN_QUANTITY, TpccConstants.MAX_QUANTITY);
			sd1 = rg.randomAString(24);
			sd2 = rg.randomAString(24);
			sd3 = rg.randomAString(24);
			sd4 = rg.randomAString(24);
			sd5 = rg.randomAString(24);
			sd6 = rg.randomAString(24);
			sd7 = rg.randomAString(24);
			sd8 = rg.randomAString(24);
			sd9 = rg.randomAString(24);
			sd10 = rg.randomAString(24);

			// S_DATA
			sdata = rg.randomAString(TpccConstants.MIN_I_DATA, TpccConstants.MAX_I_DATA);
			if (Math.random() < 0.1)
				sdata = fillOriginal(sdata);

			String sql = "INSERT INTO stock(s_i_id, s_w_id, s_quantity, "
					+ "s_dist_01, s_dist_02 ,s_dist_03, s_dist_04, s_dist_05,"
					+ "s_dist_06, s_dist_07 ,s_dist_08, s_dist_09, s_dist_10,"
					+ "s_ytd, s_order_cnt, s_remote_cnt, s_data) VALUES (" + siid + ", " + swid + ", " + squantity
					+ ", '" + sd1 + "', '" + sd2 + "', '" + sd3 + "', '" + sd4 + "', '" + sd5 + "', '" + sd6 + "', '"
					+ sd7 + "', '" + sd8 + "', '" + sd9 + "', '" + sd10 + "', 0, 0, 0, '" + sdata + "')";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
	}

	private void generateDistricts(int wid) {
		Transaction tx = getTransaction();
		int did;
		double dtax, dytd;
		dytd = TpccConstants.INITIAL_D_YTD;

		String dname, dst1, dst2, dcity, dstate, dzip;
		for (int i = 1; i <= TpccConstants.DISTRICTS_PER_WAREHOUSE; i++) {
			did = i;
			dname = rg.randomAString(6, 10);
			dst1 = rg.randomAString(10, 20);
			dst2 = rg.randomAString(10, 20);
			dcity = rg.randomAString(10, 20);
			dstate = rg.randomAString(2);
			dzip = makeZip();
			dtax = makeTax();

			String sql = "INSERT INTO district(d_id, d_w_id, "
					+ "d_name, d_street_1, d_street_2, d_city, d_state, d_zip,"
					+ " d_tax, d_ytd, d_next_o_id ) VALUES (" + did + ", " + wid + ", '" + dname + "', '" + dst1
					+ "', '" + dst2 + "', '" + dcity + "', '" + dstate + "', '" + dzip + "', "
					+ DoublePlainPrinter.toPlainString(dtax) + ", " + DoublePlainPrinter.toPlainString(dytd) + ", "
					+ (TpccConstants.CUSTOMERS_PER_DISTRICT + 1) + ")";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
	}

	private void generateCustomers(int wid, int did) {
		Transaction tx = getTransaction();
		int cid;
		String clast, cmiddle = TpccConstants.MIDDLE, cfirst, cst1, cst2, ccity, cstate, czip, cphone, ccredit, cdata;
		long csince;
		double cdiscount, ccl, cbal, cytdpay;
		ccl = TpccConstants.INITIAL_CREDIT_LIM;
		cbal = TpccConstants.INITIAL_BALANCE;
		cytdpay = TpccConstants.INITIAL_YTD_PAYMENT;

		for (int i = 1; i <= TpccConstants.CUSTOMERS_PER_DISTRICT; i++) {
			cid = i;
			if (i > TpccConstants.NUM_DISTINCT_CLAST) // TpccConstants.CUSTOMERS_PER_DISTRICT/3
				clast = rg.makeRandomLastName(true);
			else
				clast = rg.makeLastName(cid - 1);

			cfirst = rg.randomAString(TpccConstants.MIN_FIRST, TpccConstants.MAX_FIRST);
			cst1 = rg.randomAString(10, 20);
			cst2 = rg.randomAString(10, 20);
			ccity = rg.randomAString(10, 20);
			cstate = rg.randomAString(2);
			czip = makeZip();
			cphone = rg.nstring(TpccConstants.PHONE, TpccConstants.PHONE);
			if (Math.random() < 0.1)
				ccredit = TpccConstants.BAD_CREDIT;
			else
				ccredit = TpccConstants.GOOD_CREDIT;
			csince = System.currentTimeMillis();
			cdiscount = rg.fixedDecimalNumber(TpccConstants.DISCOUNT_DECIMALS, TpccConstants.MIN_DISCOUNT,
					TpccConstants.MAX_DISCOUNT);

			cdata = rg.randomAString(TpccConstants.MIN_C_DATA, TpccConstants.MAX_C_DATA);

			String sql = "INSERT INTO customer(c_id, c_d_id, c_w_id, "
					+ "c_last, c_middle, c_first, c_street_1, c_street_2, "
					+ "c_city, c_state, c_zip, c_phone, c_since, c_credit,"
					+ "c_credit_lim, c_discount, c_balance, c_ytd_payment, "
					+ "c_payment_cnt, c_delivery_cnt, c_data ) VALUES (" + cid + "," + did + "," + wid + ",'" + clast
					+ "', '" + cmiddle + "', '" + cfirst + "', '" + cst1 + "', '" + cst2 + "', '" + ccity + "', '"
					+ cstate + "', '" + czip + "', '" + cphone + "', " + csince + ", '" + ccredit + "', "
					+ DoublePlainPrinter.toPlainString(ccl) + ", " + DoublePlainPrinter.toPlainString(cdiscount) + ", "
					+ DoublePlainPrinter.toPlainString(cbal) + ", " + DoublePlainPrinter.toPlainString(cytdpay)
					+ ", 1, 0, '" + cdata + "')";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
		if (logger.isLoggable(Level.FINE))
			logger.info("Finish populating customers for district " + did);
	}

	private void generateCustomerHistory(int wid, int did) {
		Transaction tx = getTransaction();
		int hcid;
		Long hdate;
		double hamount = TpccConstants.INITIAL_AMOUNT;
		String hdata;
		for (int i = 1; i <= TpccConstants.CUSTOMERS_PER_DISTRICT; i++) {
			hcid = i;
			hdata = rg.randomAString(TpccConstants.MIN_DATA, TpccConstants.MAX_DATA);
			hdate = System.currentTimeMillis();

			String sql = "INSERT INTO history(h_c_id, h_c_d_id, h_c_w_id, "
					+ "h_d_id,h_w_id, h_date, h_amount, h_data ) VALUES (" + hcid + ", " + did + "," + wid + ","
					+ did + "," + wid + "," + hdate + "," + DoublePlainPrinter.toPlainString(hamount) + ", '" + hdata
					+ "')";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
	}

	private void generateOrders(int wid, int did) {
		Transaction tx = getTransaction();
		int oid, ocid, ocarid, ol_cnt;
		long oenrtyd;
		RandomPermutationGenerator rpg = new RandomPermutationGenerator(TpccConstants.CUSTOMERS_PER_DISTRICT);
		rpg.next();
		for (int i = 1; i <= TpccConstants.CUSTOMERS_PER_DISTRICT; i++) {
			oid = i;
			ocid = rpg.get(i - 1);
			oenrtyd = System.currentTimeMillis();
			if (i < TpccConstants.NEW_ORDER_START_ID)
				ocarid = rg.number(TpccConstants.MIN_CARRIER_ID, TpccConstants.MAX_CARRIER_ID);
			else
				ocarid = TpccConstants.NULL_CARRIER_ID;
			ol_cnt = rg.number(TpccConstants.MIN_OL_CNT, TpccConstants.MAX_OL_CNT);

			String sql = "INSERT INTO ORDERS(o_id, o_c_id, o_d_id, "
					+ "o_w_id, o_entry_d, o_carrier_id, o_ol_cnt, o_all_local) VALUES (" + oid + ", " + ocid + ", "
					+ did + "," + wid + "," + oenrtyd + "," + ocarid + ", " + ol_cnt + ",1)";
			StoredProcedureHelper.executeUpdate(sql, tx);

			generateOrderLine(wid, did, i, ol_cnt, oenrtyd);
		}
	}

	public void generateOrderLine(int warehouseId, int districtId, int orderId, int ol_cnt, long date) {
		Transaction tx = getTransaction();
		int olnum, oliid;
		long oldeld;
		double olamount;
		String oldistinfo;
		for (int i = 1; i <= ol_cnt; i++) {
			olnum = i;
			oliid = rg.number(1, TpccConstants.NUM_ITEMS);

			if (orderId < TpccConstants.NEW_ORDER_START_ID) {
				oldeld = date;
				olamount = 0.0;
			} else {
				oldeld = TpccConstants.NULL_DELIVERY_DATE;
				olamount = rg.fixedDecimalNumber(TpccConstants.MONEY_DECIMALS, TpccConstants.MIN_AMOUNT,
						TpccConstants.MAX_PRICE * TpccConstants.MAX_OL_QUANTITY);
			}

			oldistinfo = rg.randomAString(24);
			String sql = "INSERT INTO order_line(ol_o_id, ol_d_id, " + "ol_w_id, ol_number, ol_i_id, ol_supply_w_id, "
					+ "ol_delivery_d, ol_quantity, ol_amount, ol_dist_info)" + " VALUES (" + orderId + "," + districtId
					+ "," + warehouseId + "," + olnum + "," + oliid + ", " + warehouseId + ", " + oldeld + ", 5, "
					+ DoublePlainPrinter.toPlainString(olamount) + ", '" + oldistinfo + "')";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
	}

	private void generateNewOrders(int wid, int did) {
		Transaction tx = getTransaction();
		int nooid;
		for (int i = TpccConstants.NEW_ORDER_START_ID; i <= TpccConstants.CUSTOMERS_PER_DISTRICT; i++) {
			nooid = i;
			String sql = "INSERT INTO new_order(no_o_id, no_d_id, no_w_id) VALUES (" + nooid + "," + did + "," + wid
					+ ")";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
	}

	private String fillOriginal(String data) {
		int originalLength = TpccConstants.ORIGINAL_STRING.length();
		int position = rg.number(0, data.length() - originalLength);
		String out = data.substring(0, position) + TpccConstants.ORIGINAL_STRING
				+ data.substring(position + originalLength);
		return out;
	}

	private String makeZip() {
		return rg.nstring(4) + TpccConstants.ZIP_SUFFIX;
	}

	private double makeTax() {
		return rg.fixedDecimalNumber(TpccConstants.TAX_DECIMALS, TpccConstants.MIN_TAX, TpccConstants.MAX_TAX);
	}
}
