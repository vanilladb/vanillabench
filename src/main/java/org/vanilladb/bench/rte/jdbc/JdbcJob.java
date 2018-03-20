package org.vanilladb.bench.rte.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.remote.SutResultSet;

public interface JdbcJob {
	
	SutResultSet execute(Connection conn, Object[] pars) throws SQLException;
	
}
