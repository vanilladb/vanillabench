package org.vanilladb.bench.rte;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;
import org.vanilladb.bench.util.BenchProperties;

public abstract class TransactionExecutor<T extends TransactionType> {

	public static final boolean DISPLAY_RESULT;

	static {
		DISPLAY_RESULT = BenchProperties.getLoader()
				.getPropertyAsBoolean(TransactionExecutor.class.getName() + ".DISPLAY_RESULT", false);
	}

	protected TxParamGenerator<T> pg;

	public abstract TxnResultSet execute(SutConnection conn);
	
	protected abstract JdbcExecutor<T> getJdbcExecutor();
	
	protected SutResultSet executeTxn(SutConnection conn, Object[] pars) throws SQLException {
		SutResultSet result = null;
		
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			Connection jdbcConn = conn.toJdbcConnection();
			jdbcConn.setAutoCommit(false);
			result = getJdbcExecutor().execute(jdbcConn, pg.getTxnType(), pars);
			break;
		case SP:
			result = conn.callStoredProc(pg.getTxnType().getProcedureId(), pars);
			break;
		}
		
		return result;
	}
}
