package org.vanilladb.bench.rte.jdbc;

import java.sql.Connection;

import org.vanilladb.bench.remote.SutResultSet;

public interface JdbcJob {
	
	SutResultSet execute(Connection conn, Object[] pars);
	
}
