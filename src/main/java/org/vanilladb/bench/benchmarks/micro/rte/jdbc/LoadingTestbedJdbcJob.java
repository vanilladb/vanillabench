package org.vanilladb.bench.benchmarks.micro.rte.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.tpcc.TpccConstants;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;
import org.vanilladb.bench.server.param.micro.TestbedLoaderParamHelper;

public class LoadingTestbedJdbcJob implements JdbcJob {
	private static Logger logger = Logger.getLogger(LoadingTestbedJdbcJob.class.getName());
	
	TestbedLoaderParamHelper paramHelper;

	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		// Parse parameters
		paramHelper = new TestbedLoaderParamHelper();
		paramHelper.prepareParameters(pars);
		
		// Execute logic
		try {
			Statement stat = conn.createStatement();
			dropOldData(stat);
			createSchemas(stat);
			generateItems(stat, 1, paramHelper.getNumberOfItems());
			conn.commit();
			
			return new SutResultSet(true, "Success");
		} catch (Exception e) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(e.toString());
			return new SutResultSet(false, "");
		}
	}
	
	private void dropOldData(Statement stat) throws SQLException {
		// TODO: Implement this
		if (logger.isLoggable(Level.WARNING))
			logger.warning("Dropping is skipped.");
	}
	
	private void createSchemas(Statement stat) throws SQLException {
		if (logger.isLoggable(Level.FINE))
			logger.info("Create tables...");
		
		for (String cmd : paramHelper.getTableSchemas())
			stat.executeUpdate(cmd);
		
		if (logger.isLoggable(Level.FINE))
			logger.info("Create indexes...");

		for (String cmd : paramHelper.getIndexSchemas())
			stat.executeUpdate(cmd);
		
		if (logger.isLoggable(Level.FINE))
			logger.info("Finish creating schemas.");
	}

	private void generateItems(Statement stat, int startIId, int endIId) throws SQLException {
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
			stat.executeUpdate(sql);
		}

		if (logger.isLoggable(Level.FINE))
			logger.info("Populating items completed.");
	}
}
