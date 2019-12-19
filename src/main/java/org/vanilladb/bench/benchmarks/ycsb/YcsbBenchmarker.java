package org.vanilladb.bench.benchmarks.ycsb;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.ycsb.rte.YcsbRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class YcsbBenchmarker extends Benchmarker {
	
	public YcsbBenchmarker(SutDriver sutDriver) {
		super(sutDriver, "ycsb");
	}
	
	public YcsbBenchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "ycsb-" + reportPostfix);
	}
	
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (YcsbTransactionType txType : YcsbTransactionType.values()) {
			if (!txType.isLoadingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}
	
	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(YcsbTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(YcsbTransactionType.TESTBED_LOADER.ordinal());
	}
	
	protected RemoteTerminalEmulator<YcsbTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new YcsbRte(conn, statMgr);
	}
}
