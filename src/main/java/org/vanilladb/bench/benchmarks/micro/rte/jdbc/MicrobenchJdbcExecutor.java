package org.vanilladb.bench.benchmarks.micro.rte.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.benchmarks.micro.MicroTransactionType;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;

public class MicrobenchJdbcExecutor implements JdbcExecutor<MicroTransactionType> {

	@Override
	public SutResultSet execute(Connection conn, MicroTransactionType txType, Object[] pars)
			throws SQLException {
		switch (txType) {
		case MICRO:
			return new MicrobenchJdbcJob().execute(conn, pars);
		default:
			throw new UnsupportedOperationException(
					String.format("no JDCB implementation for '%s'", txType));
		}
	}

}
