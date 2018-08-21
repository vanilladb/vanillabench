package org.vanilladb.bench.remote.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;

public class VanillaDbJdbcConnection implements SutConnection {
	private Connection conn;

	public VanillaDbJdbcConnection(Connection conn) {
		this.conn = conn;
	}

	@Override
	public SutResultSet callStoredProc(int pid, Object... pars) throws SQLException {
		throw new RuntimeException("cannot call a stored procedure from a JDBC connection");
	}
	
	@Override
	public Connection toJdbcConnection() {
		return conn;
	}
}

