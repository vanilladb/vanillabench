package org.vanilladb.bench.ycsb;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.ycsb.rte.YcsbRte;

public class YcsbBenchmarker extends Benchmarker {
	
	public YcsbBenchmarker(SutDriver sutDriver) {
		super(sutDriver);
	}
	
	public YcsbBenchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "ycsb-" + reportPostfix);
	}
	
	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : YcsbTransactionType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}
	
	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(YcsbTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(YcsbTransactionType.TESTBED_LOADER.ordinal());
	}
	
	protected RemoteTerminalEmulator createRte(SutConnection conn, StatisticMgr statMgr) {
		return new YcsbRte(conn, statMgr);
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(YcsbTransactionType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(YcsbTransactionType.STOP_PROFILING.ordinal());
	}
}
