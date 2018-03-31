package org.vanilladb.bench.benchmarks.micro.rte.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.benchmarks.micro.MicrobenchmarkTxnType;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;

public class MicrobenchJdbcExecutor implements JdbcExecutor<MicrobenchmarkTxnType> {

	@Override
	public SutResultSet execute(Connection conn, MicrobenchmarkTxnType txType, Object[] pars)
			throws SQLException {
		switch (txType) {
		case TESTBED_LOADER:
			return new LoadingTestbedJdbcJob().execute(conn, pars);
		case MICRO_TXN:
			return new MicroTxnJdbcJob().execute(conn, pars);
		default:
			throw new UnsupportedOperationException(
					String.format("no JDCB implementation for '%s'", txType));
		}
	}

}
