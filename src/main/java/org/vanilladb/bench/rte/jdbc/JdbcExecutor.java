package org.vanilladb.bench.rte.jdbc;

import java.sql.Connection;

import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.remote.SutResultSet;

public interface JdbcExecutor<T extends TransactionType> {
	
	SutResultSet execute(Connection conn, T txType, Object[] pars);
	
}
