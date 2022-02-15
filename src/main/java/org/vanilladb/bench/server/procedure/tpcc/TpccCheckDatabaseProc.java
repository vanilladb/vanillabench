package org.vanilladb.bench.server.procedure.tpcc;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.tpcc.TpccConstants;
import org.vanilladb.bench.benchmarks.tpcc.TpccParameters;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TpccCheckDatabaseProc extends StoredProcedure<StoredProcedureParamHelper> {
	private static Logger logger = Logger.getLogger(TpccCheckDatabaseProc.class.getName());
	
	public TpccCheckDatabaseProc() {
		super(StoredProcedureParamHelper.newDefaultParamHelper());
	}

	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.INFO))
			logger.info("Checking database for the TPC-C benchmarks...");

		// Checking item records
		if (!checkItemTable())
			abort("item table is not ready");
		
		// Checking each warehouse
		for (int wid = 1; wid <= TpccParameters.NUM_WAREHOUSES; wid++)
			if (!checkWarehouse(wid))
				abort("warehouse " + wid + " is not ready");

		if (logger.isLoggable(Level.INFO))
			logger.info("Checking completed.");
	}

	private boolean checkItemTable() {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking items table");
		
		// Scan the table
		String sql = "SELECT i_id FROM item";
		boolean result = checkUniqueness(sql, "i_id", 1, TpccConstants.NUM_ITEMS);

		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking items completed.");
		
		return result;
	}

	private boolean checkWarehouse(int wid) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking warehouse with w_id=" + wid);
		
		// Check warehouse records
		String sql = "SELECT w_id FROM warehouse WHERE w_id = " + wid;
		Scan s = StoredProcedureHelper.executeQuery(sql, getTransaction());
		s.beforeFirst();
		if (!s.next())
			return false;
		s.close();
		
		// Check sub-tables
		boolean result = checkStocks(wid) && checkDistricts(wid);
		
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking warehouse with w_id=" + wid + " completed.");
		
		return result;
	}
	
	private boolean checkStocks(int wid) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking stocks with s_w_id=" + wid);
		
		// Scan the table
		String sql = "SELECT s_i_id FROM stock WHERE s_w_id = " + wid;
		return checkUniqueness(sql, "s_i_id", 1, TpccConstants.NUM_ITEMS);
	}
	
	private boolean checkDistricts(int wid) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking districts with d_w_id=" + wid);
		
		// Scan the table
		String sql = "SELECT d_id FROM district WHERE d_w_id = " + wid;
		if (!checkUniqueness(sql, "d_id", 1, TpccConstants.DISTRICTS_PER_WAREHOUSE))
			return false;
		
		// Check sub-tables
		for (int did = 1; did <= TpccConstants.DISTRICTS_PER_WAREHOUSE; did++)
			if (!checkCustomers(wid, did) || !checkCustomerHistories(wid, did) ||
					!checkOrders(wid, did) || !checkOrderLines(wid, did) ||
					!checkNewOrders(wid, did))
				return false;
		
		return true;
	}
	
	private boolean checkCustomers(int wid, int did) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking customers with c_w_id=" + wid + ", c_d_id=" + did);
		
		// Scan the table
		String sql = "SELECT c_id FROM customer WHERE c_w_id = " + wid + " AND c_d_id = " + did;
		return checkUniqueness(sql, "c_id", 1, TpccConstants.CUSTOMERS_PER_DISTRICT);
	}
	
	private boolean checkCustomerHistories(int wid, int did) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking histories with h_c_w_id=" + wid + ", h_c_d_id=" + did);
		
		// Scan the table
		String sql = "SELECT h_c_id FROM history WHERE h_c_w_id = " + wid + " AND h_c_d_id = " + did;
		return checkUniqueness(sql, "h_c_id", 1, TpccConstants.CUSTOMERS_PER_DISTRICT);
	}
	
	private boolean checkOrders(int wid, int did) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking orders with o_w_id=" + wid + ", o_d_id=" + did);
		
		// Scan the table
		String sql = "SELECT o_id FROM orders WHERE o_w_id = " + wid + " AND o_d_id = " + did;
		return checkUniqueness(sql, "o_id", 1, TpccConstants.CUSTOMERS_PER_DISTRICT);
	}
	
	private boolean checkOrderLines(int wid, int did) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking order lines with ol_w_id=" + wid + ", ol_d_id=" + did);
		
		// Scan the table
		String sql = "SELECT ol_o_id FROM order_line"
				+ " WHERE ol_w_id = " + wid + " AND ol_d_id = " + did;
		return checkExistence(sql, "ol_o_id", 1, TpccConstants.CUSTOMERS_PER_DISTRICT);
	}
	
	private boolean checkNewOrders(int wid, int did) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking new orders with no_w_id=" + wid + ", no_d_id=" + did);
		
		// Scan the table
		String sql = "SELECT no_o_id FROM new_order WHERE no_w_id = " + wid + " AND no_d_id = " + did;
		return checkUniqueness(sql, "no_o_id", TpccConstants.NEW_ORDER_START_ID, TpccConstants.CUSTOMERS_PER_DISTRICT);
	}
	
	private boolean checkUniqueness(String sql, String checkField, int startId, int endId) {
		// Use a bit array to record existence
		int total = endId - startId + 1;
		boolean[] checked = new boolean[total];
		for (int i = 0; i < total; i++)
			checked[i] = false;
		
		// Check records
		Scan scan = StoredProcedureHelper.executeQuery(sql, getTransaction());
		scan.beforeFirst();
		for (int count = 0; count < total; count++) {
			if (!scan.next()) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("Only %d records are found (there should be %d records)",
							count, total));
				return false;
			}
			
			int id = (Integer) scan.getVal(checkField).asJavaVal();
			if (checked[id - startId]) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("Found duplicated record (%s = %d)", checkField, id));
				return false;
			}
			checked[id - startId] = true;
		}
		scan.close();
		
		return true;
	}
	
	private boolean checkExistence(String sql, String checkField, int startId, int endId) {
		// Use a bit array to record existence
		int total = endId - startId + 1;
		boolean[] checked = new boolean[total];
		for (int i = 0; i < total; i++)
			checked[i] = false;
		
		// Check records
		Scan scan = StoredProcedureHelper.executeQuery(sql, getTransaction());
		scan.beforeFirst();
		while (scan.next()) {
			int id = (Integer) scan.getVal(checkField).asJavaVal();
			checked[id - startId] = true;
		}
		scan.close();
		
		// Check if there is any record missing
		for (int i = 0; i < checked.length; i++) {
			if (!checked[i]) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("%s = %d is missing.", checkField, i + startId));
				return false;
			}
		}
		return true;
	}

}
