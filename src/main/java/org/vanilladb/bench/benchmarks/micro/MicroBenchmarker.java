package org.vanilladb.bench.benchmarks.micro;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.benchmarks.micro.rte.MicrobenchmarkRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class MicroBenchmarker extends Benchmarker {
	
	public MicroBenchmarker(SutDriver sutDriver) {
		super(sutDriver);
	}
	
	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : MicroTransactionType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}

	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(MicroTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(MicroTransactionType.TESTBED_LOADER.ordinal());
	}
	
	protected RemoteTerminalEmulator<MicroTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new MicrobenchmarkRte(conn, statMgr);
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(MicroTransactionType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(MicroTransactionType.STOP_PROFILING.ordinal());
	}
}
