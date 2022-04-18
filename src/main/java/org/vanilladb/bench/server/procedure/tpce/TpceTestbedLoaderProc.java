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
package org.vanilladb.bench.server.procedure.tpce;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.tpce.data.TpceDataManager;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

public class TpceTestbedLoaderProc extends StoredProcedure<StoredProcedureParamHelper> {
	private static Logger logger = Logger.getLogger(TpceTestbedLoaderProc.class.getName());
	
	private static interface RowProcessor {
		void processRow(String[] columns);
	}

	public TpceTestbedLoaderProc() {
		super(StoredProcedureParamHelper.DEFAULT_HELPER);
	}

	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.INFO))
			logger.info("Start loading testbed...");

		// turn off logging set value to speed up loading process
		// TODO: remove this hack code in the future
		RecoveryMgr.enableLogging(false);

		// Load testbed
		loadTestbed();

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

	private void loadTestbed() {
		// Customer Category
		// TODO: watch_item, watch_list
//		loadAccountPermission();
		loadCustomer();
		loadCustomerAccount();
//		loadCustomerTaxrate();
//		loadHolding();
//		loadHoldingHistory();
//		loadSummary();
		
		// Broker Category
		// TODO: cash_transaction, settlement
		loadBroker();
//		loadCharge();
//		loadCommissionRate();
//		loadTrade();
//		loadTradeHistory();
//		loadTradeRequest();
		loadTradeType();
		
		// Market Category
		// TODO: company_competitor, daily_market, exchange, financial, industry,
		// news_item, news_xref, sector
		loadCompany();
		loadLastTrade();
		loadSecurity();
		
		// Dimension Category
		// TODO: address, status_type, taxrate, zip_code
	}
	
	private void loadCustomer() {
		final Transaction tx = getTransaction();
		readRows("Customer.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				long cDob = parseDateString(columns[8]);
				
				String sql = String.format("INSERT INTO customer (c_id, c_tax_id, c_st_id, "
						+ "c_l_name, c_f_name, c_m_name, c_gndr, c_tier, c_dob, c_ad_id, "
						+ "c_ctry_1, c_area_1, c_local_1, c_ext_1, c_ctry_2, c_area_2, "
						+ "c_local_2, c_ext_2, c_ctry_3, c_area_3, c_local_3, c_ext_3, "
						+ "c_email_1, c_email_2) VALUES (%s, '%s', '%s', '%s', '%s', '%s',"
						+ "'%s', %s, %d, %s, '%s', '%s', '%s', '%s', '%s', '%s', '%s',"
						+ "'%s', '%s', '%s', '%s', '%s', '%s', '%s')", columns[0], 
						columns[1], columns[2], columns[3], columns[4], columns[5], 
						columns[6], columns[7], cDob, columns[9], columns[10], columns[11],
						columns[12], columns[13], columns[14], columns[15], columns[16],
						columns[17], columns[18], columns[19], columns[20], columns[21],
						columns[22], columns[23]);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	private void loadCustomerAccount() {
		final Transaction tx = getTransaction();
		readRows("CustomerAccount.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				String sql = String.format("INSERT INTO customer_account (ca_id, ca_b_id, "
						+ "ca_c_id, ca_name, ca_tax_st, ca_bal) VALUES (%s, %s, %s, '%s', "
						+ "%s, %s)", columns[0], columns[1], columns[2], columns[3], 
						columns[4], columns[5]);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	private void loadBroker() {
		final Transaction tx = getTransaction();
		readRows("Broker.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				String sql = String.format("INSERT INTO broker (b_id, b_st_id, b_name, "
						+ "b_num_trades, b_comm_total) VALUES (%s, '%s', '%s', %s, %s)", 
						columns[0], columns[1], columns[2], columns[3], columns[4]);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	private void loadTradeType() {
		final Transaction tx = getTransaction();
		readRows("TradeType.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				String sql = String.format("INSERT INTO trade_type (tt_id, tt_name, "
						+ "tt_is_sell, tt_is_mrkt) VALUES ('%s', '%s', %s, %s)", 
						columns[0], columns[1], columns[2], columns[3]);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	private void loadCompany() {
		final Transaction tx = getTransaction();
		readRows("Company.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				long coOpenDate = parseDateString(columns[8]);
				
				String sql = String.format("INSERT INTO company (co_id, co_st_id, co_name, "
						+ "co_in_id, co_sp_rate, co_ceo, co_ad_id, co_desc, co_open_date) "
						+ "VALUES (%s, '%s', '%s', '%s', '%s', '%s', %s, '%s', %d)", 
						columns[0], columns[1], columns[2], columns[3], columns[4], 
						columns[5], columns[6], columns[7], coOpenDate);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	private void loadLastTrade() {
		final Transaction tx = getTransaction();
		readRows("LastTrade.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				long ltDts = parseDateTimeString(columns[1]);
				
				String sql = String.format("INSERT INTO last_trade (lt_s_symb, lt_dts, "
						+ "lt_price, lt_open_price, lt_vol) VALUES ('%s', %d, %s, %s, %s)", 
						columns[0], ltDts, columns[2], columns[3], columns[4]);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	private void loadSecurity() {
		final Transaction tx = getTransaction();
		readRows("Security.txt", "\\|", new RowProcessor() {

			@Override
			public void processRow(String[] columns) {
				long sStartDate = parseDateString(columns[7]);
				long sExchDate = parseDateString(columns[8]);
				long s52wkHighDate = parseDateString(columns[11]);
				long s52wkLowDate = parseDateString(columns[13]);
				
				String sql = String.format("INSERT INTO security (s_symb, s_issue, s_st_id, "
						+ "s_name, s_ex_id, s_co_id, s_num_out, s_start_date, s_exch_date, "
						+ "s_pe, s_52wk_high, s_52wk_high_date, s_52wk_low, s_52wk_low_date, "
						+ "s_dividend, s_yield) VALUES ('%s', '%s', '%s', '%s', '%s', %s, "
						+ "%s, %d, %d, %s, %s, %d, %s, %d, %s, %s)", columns[0], 
						columns[1], columns[2], columns[3], columns[4], columns[5], 
						columns[6], sStartDate, sExchDate, columns[9], columns[10], 
						s52wkHighDate, columns[12], s52wkLowDate, columns[14], columns[15]);
				StoredProcedureHelper.executeUpdate(sql, tx);
			}
			
		});
	}
	
	// XXX: Maybe the StreamTokenizer will be faster
	private void readRows(String fileName, String delimlier, RowProcessor processor) {
		if (logger.isLoggable(Level.INFO))
			logger.info("Start reading '" + fileName + "'");
		
		int count = 0;
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(TpceDataManager.DATA_DIR, fileName)));
			String line = null;
			while ((line = br.readLine()) != null) {
				// replace ' with (nothing) to prevent SQL syntax error 
				processor.processRow(line.replaceAll("'", "").split(delimlier));
				count++;
				
				if (count % 10000 == 0) {
					if (logger.isLoggable(Level.INFO))
						logger.info(count + " rows have been processed.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (logger.isLoggable(Level.INFO))
			logger.info("'" + fileName + "' has been processed. (" + count + " rows in total)");
	}
	
	private long parseDateString(String dateStr) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
			return formatter.parse(dateStr).getTime();
		} catch (ParseException e) {
			throw new RuntimeException("Cannot parse: " + dateStr);
		}
	}
	
	private long parseDateTimeString(String dateTimeStr) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			return formatter.parse(dateTimeStr).getTime();
		} catch (ParseException e) {
			throw new RuntimeException("Cannot parse: " + dateTimeStr);
		}
	}
}
