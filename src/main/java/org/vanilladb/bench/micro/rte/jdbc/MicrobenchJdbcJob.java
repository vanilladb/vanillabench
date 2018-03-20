package org.vanilladb.bench.micro.rte.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;

public class MicrobenchJdbcJob implements JdbcJob {
	private static Logger logger = Logger.getLogger(MicrobenchJdbcJob.class.getName());

	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		// Parse parameters
		int readCount = (Integer) pars[0];
		int[] itemIds = new int[readCount];
		for (int i = 1; i <= readCount; i++)
			itemIds[i] = (Integer) pars[i++];
		
		// Output message
		StringBuilder outputMsg = new StringBuilder("[");
		
		// Execute logic
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = null;
			for (int i = 0; i < 10; i++) {
				String sql = "SELECT i_name FROM item WHERE i_id = " + itemIds[i];
				rs = statement.executeQuery(sql);
				rs.beforeFirst();
				if (rs.next()) {
					outputMsg.append(String.format("item_name_%d = '%s',", i, rs.getString("i_name")));
				} else
					throw new RuntimeException();
				rs.close();
			}
			conn.commit();
			
			outputMsg.deleteCharAt(outputMsg.length() - 1);
			outputMsg.append("]");
			
			return new VanillaDbJdbcResultSet(true, outputMsg.toString());
		} catch (Exception e) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(e.getMessage());
			
			return new VanillaDbJdbcResultSet(false, "");
		}
	}
}
