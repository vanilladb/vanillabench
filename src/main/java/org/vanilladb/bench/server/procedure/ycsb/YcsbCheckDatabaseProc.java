package org.vanilladb.bench.server.procedure.ycsb;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class YcsbCheckDatabaseProc extends StoredProcedure<StoredProcedureHelper> {
	private static Logger logger = Logger.getLogger(YcsbCheckDatabaseProc.class.getName());
	
	public YcsbCheckDatabaseProc() {
		super(StoredProcedureHelper.DEFAULT_HELPER);
	}

	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.INFO))
			logger.info("Checking database for the YCSB benchmarks...");

		// Checking YCSB records
		if (!checkYcsbTable(1, YcsbConstants.NUM_RECORDS))
			abort("checking database fails");

		if (logger.isLoggable(Level.INFO))
			logger.info("Checking completed.");
	}

	private boolean checkYcsbTable(int startIId, int endIId) {
		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking YCSB records from ycsb_id=" + startIId + " to ycsb_id=" + endIId);
		
		// Use a bit array to record existence
		int total = endIId - startIId + 1;
		boolean[] checked = new boolean[total];
		for (int i = 0; i < total; i++)
			checked[i] = false;
		
		// Scan the table
		String sql = "SELECT ycsb_id FROM ycsb";
		Scan scan = StoredProcedureUtils.executeQuery(sql, getTransaction());
		scan.beforeFirst();
		for (int i = startIId, count = 0; i <= endIId; i++) {
			if (!scan.next()) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("Only %d records are found (there should be %d records)",
							count, total));
				return false;
			}
			
			String idStr = (String) scan.getVal("ycsb_id").asJavaVal();
			int id = Integer.parseInt(idStr);
			if (checked[id - 1]) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("Found duplicated record (i_id = %d)", id));
				return false;
			}
			checked[id - 1] = true;
			count++;
		}
		scan.close();

		if (logger.isLoggable(Level.FINE))
			logger.fine("Checking YCSB records completed.");
		
		return true;
	}

}
