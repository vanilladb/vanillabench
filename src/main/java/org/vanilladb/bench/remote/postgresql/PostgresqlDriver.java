package org.vanilladb.bench.remote.postgresql;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;

public class PostgresqlDriver implements SutDriver {
	
	private static final String URL;
	
	static {
		StringBuilder sb = new StringBuilder();
		
		sb.append("jdbc:postgresql://");
		sb.append(BenchmarkerParameters.CONNECTION_HOST);
		
		if (BenchmarkerParameters.CONNECTION_PORT > 0)
			sb.append(String.format(":%d", BenchmarkerParameters.CONNECTION_PORT));
		
		sb.append("/");
		
		if (!BenchmarkerParameters.CONNECTION_DBNAME.isEmpty())
			sb.append(BenchmarkerParameters.CONNECTION_DBNAME);
		
		sb.append("?ssl=");
		sb.append(BenchmarkerParameters.CONNECTION_SSL);
		
		if (!BenchmarkerParameters.CONNECTION_USER.isEmpty()) {
			sb.append("&user=");
			sb.append(BenchmarkerParameters.CONNECTION_USER);
			sb.append("&password=");
			sb.append(BenchmarkerParameters.CONNECTION_PASSWORD);
		}
		
		URL = sb.toString();
	}
	
	@Override
	public SutConnection connectToSut() throws SQLException {
		return new PostgresqlConnection(DriverManager.getConnection(URL));
	}
}

