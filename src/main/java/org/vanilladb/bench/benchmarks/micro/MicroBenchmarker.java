package org.vanilladb.bench.benchmarks.micro;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.benchmarks.micro.rte.MicrobenchmarkRte;
import org.vanilladb.bench.benchmarks.micro.rte.MicrobenchmarkTxExecutor;
import org.vanilladb.bench.benchmarks.micro.rte.TestbedLoaderParamGen;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class MicroBenchmarker extends Benchmarker {
	
	public MicroBenchmarker(SutDriver sutDriver) {
		super(sutDriver, "microbench");
	}
	
	public MicroBenchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "microbench-" + reportPostfix);
	}
	
	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : MicrobenchmarkTxnType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}

	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		MicrobenchmarkTxExecutor loader = new MicrobenchmarkTxExecutor(new TestbedLoaderParamGen());
		loader.execute(conn);
	}
	
	protected RemoteTerminalEmulator<MicrobenchmarkTxnType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new MicrobenchmarkRte(conn, statMgr);
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(MicrobenchmarkTxnType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(MicrobenchmarkTxnType.STOP_PROFILING.ordinal());
	}
}
