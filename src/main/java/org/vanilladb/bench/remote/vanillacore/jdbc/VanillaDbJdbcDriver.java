package org.vanilladb.bench.remote.vanillacore.jdbc;

import java.sql.Driver;
import java.sql.SQLException;

import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.core.remote.jdbc.JdbcDriver;

public class VanillaDbJdbcDriver implements SutDriver {
	
	private static final String URL = "jdbc:vanilladb://" + BenchmarkerParameters.CONNECTION_HOST;
	
	private Driver driver;
	
	public VanillaDbJdbcDriver() {
		driver = new JdbcDriver();
	}
	
	@Override
	public SutConnection connectToSut() throws SQLException {
		return new VanillaDbJdbcConnection(driver.connect(URL, null));
	}
}

