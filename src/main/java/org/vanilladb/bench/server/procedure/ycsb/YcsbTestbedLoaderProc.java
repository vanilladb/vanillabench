package org.vanilladb.bench.server.procedure.ycsb;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

public class YcsbTestbedLoaderProc extends StoredProcedure<StoredProcedureParamHelper> {
	private static Logger logger = Logger.getLogger(YcsbTestbedLoaderProc.class.getName());
	
	public YcsbTestbedLoaderProc() {
		super(StoredProcedureParamHelper.DefaultParamHelper());
	}
	
	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.INFO))
			logger.info("Start loading testbed...");

		// turn off logging set value to speed up loading process
		// TODO: remove this hack code in the future
		RecoveryMgr.enableLogging(false);

		// Generate item records
		generateItems(1, YcsbConstants.NUM_ITEMS);

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

	private void generateItems(int startId, int recordCount) {
		Transaction tx = getTransaction();
		int endId = startId + recordCount - 1;
		
		if (logger.isLoggable(Level.FINE))
			logger.info("Start populating YCSB table from ycsb_id=" + startId + " to ycsb_id=" + endId);
		
		// Generate the field names of YCSB table
		String sqlPrefix = "INSERT INTO ycsb (ycsb_id";
		for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
			sqlPrefix += ", ycsb_" + count;
		}
		sqlPrefix += ") VALUES (";
		
		String sql;
		String ycsbId, ycsbValue;
		for (int id = startId, recCount = 1; id <= endId; id++, recCount++) {
			
			// The primary key of YCSB is the string format of id
			ycsbId = String.format(YcsbConstants.ID_FORMAT, id);
			
			sql = sqlPrefix + "'" + ycsbId + "'";
			
			// All values of the fields use the same value
			ycsbValue = ycsbId;
			
			for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
				sql += ", '" + ycsbValue + "'";
			}
			sql += ")";

			StoredProcedureHelper.executeUpdate(sql, tx);
			
			if (recCount % 50000 == 0)
				if (logger.isLoggable(Level.INFO))
					logger.info(recCount + " YCSB records has been populated.");
		}

		if (logger.isLoggable(Level.INFO))
			logger.info("Populating YCSB table completed.");
	}
}
