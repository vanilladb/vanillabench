package org.vanilladb.bench.remote.postgresql;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;

public class PostgresqlConnection implements SutConnection {
	private Connection conn;

	public PostgresqlConnection(Connection conn) {
		this.conn = conn;
	}

	@Override
	public SutResultSet callStoredProc(int pid, Object... pars) throws SQLException {
		throw new RuntimeException("VanillaBench does not support stored procedures for PostgreSQL");
	}
	
	@Override
	public Connection toJdbcConnection() {
		return conn;
	}
}
