package org.vanilladb.bench.server.procedure.micro;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.micro.MicrobenchConstants;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.bench.tpcc.TpccConstants;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

public class TestbedLoaderProc extends BasicStoredProcedure<StoredProcedureParamHelper> {
	private static Logger logger = Logger.getLogger(TestbedLoaderProc.class.getName());

	public TestbedLoaderProc() {
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
		generateItems(1, MicrobenchConstants.NUM_ITEMS);

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
		for (int i = startIId; i <= endIId; i++) {
			iid = i;

			// Deterministic value generation by item id
			iimid = iid % (TpccConstants.MAX_IM - TpccConstants.MIN_IM) + TpccConstants.MIN_IM;
			iname = String.format("%0" + TpccConstants.MIN_I_NAME + "d", iid);
			iprice = (iid % (int) (TpccConstants.MAX_PRICE - TpccConstants.MIN_PRICE)) + TpccConstants.MIN_PRICE;
			idata = String.format("%0" + TpccConstants.MIN_I_DATA + "d", iid);

			sql = "INSERT INTO item(i_id, i_im_id, i_name, i_price, i_data) VALUES (" + iid + ", " + iimid + ", '"
					+ iname + "', " + iprice + ", '" + idata + "' )";

			int result = VanillaDb.newPlanner().executeUpdate(sql, tx);
			if (result <= 0)
				throw new RuntimeException();
		}

		if (logger.isLoggable(Level.FINE))
			logger.info("Populating items completed.");
	}
}
