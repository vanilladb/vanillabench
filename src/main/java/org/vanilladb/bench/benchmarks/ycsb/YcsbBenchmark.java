package org.vanilladb.bench.benchmarks.ycsb;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmark;
import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.ycsb.rte.YcsbRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class YcsbBenchmark extends Benchmark {

	@Override
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (YcsbTransactionType txType : YcsbTransactionType.values()) {
			if (txType.isBenchmarkingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}

	@Override
	public void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(YcsbTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(YcsbTransactionType.TESTBED_LOADER.ordinal());
	}

	@Override
	public RemoteTerminalEmulator<YcsbTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new YcsbRte(conn, statMgr);
	}

	@Override
	public boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException {
		SutResultSet result = null;
		YcsbTransactionType txnType = YcsbTransactionType.CHECK_DATABASE;
		Object[] params = new Object[0];
		
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			throw new RuntimeException("We do not implement checking procedure for JDBC");
		case SP:
			result = conn.callStoredProc(txnType.getProcedureId(), params);
			break;
		}
		
		return result.isCommitted();
	}

	@Override
	public String getBenchmarkName() {
		return "ycsb";
	}
}
