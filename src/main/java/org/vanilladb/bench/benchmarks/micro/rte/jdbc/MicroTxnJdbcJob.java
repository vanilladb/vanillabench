package org.vanilladb.bench.benchmarks.micro.rte.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;
import org.vanilladb.bench.server.param.micro.MicroTxnProcParamHelper;

public class MicroTxnJdbcJob implements JdbcJob {
	private static Logger logger = Logger.getLogger(MicroTxnJdbcJob.class
			.getName());
	
	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		MicroTxnProcParamHelper paramHelper = new MicroTxnProcParamHelper();
		paramHelper.prepareParameters(pars);
		
		// Output message
		StringBuilder outputMsg = new StringBuilder("[");
		
		// Execute logic
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = null;
			
			// SELECT
			for (int i = 0; i < paramHelper.getReadCount(); i++) {
				int iid = paramHelper.getReadItemId(i);
				String sql = "SELECT i_name FROM item WHERE i_id = " + iid;
				rs = statement.executeQuery(sql);
				if (!rs.isBeforeFirst())
					rs.beforeFirst();
				if (rs.next()) {
					outputMsg.append(String.format("'%s', ", rs.getString("i_name")));
				} else
					throw new RuntimeException("cannot find the record with i_id = " + iid);
				rs.close();
			}
			
			// UPDATE
			for (int i = 0; i < paramHelper.getWriteCount(); i++) {
				int iid = paramHelper.getWriteItemId(i);
				double newPrice = paramHelper.getNewItemPrice(i);
				String sql = "UPDATE item SET i_price = " + newPrice + " WHERE i_id =" + iid;
				int count = statement.executeUpdate(sql);
				
				if (count < 1)
					throw new RuntimeException("cannot update the record with i_id = " + iid);
			}
			
			conn.commit();
			
			outputMsg.deleteCharAt(outputMsg.length() - 2);
			outputMsg.append("]");
			
			return new SutResultSet(true, outputMsg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			if (logger.isLoggable(Level.WARNING))
				logger.warning(e.toString());
			return new SutResultSet(false, "");
		}
	}
}
