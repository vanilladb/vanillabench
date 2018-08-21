package org.vanilladb.bench.benchmarks.tpcc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.benchmarks.tpcc.rte.TpccRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;

public class TpccBenchmarker extends Benchmarker {
	
	private int nextWid = 0;
	
	public TpccBenchmarker(SutDriver sutDriver) {
		super(sutDriver, "tpcc");
	}

	public TpccBenchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "tpcc-" + reportPostfix);
	}
	
	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : TpccTransactionType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}

	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(TpccTransactionType.TESTBED_LOADER.ordinal());
	}
	
	protected TpccRte createRte(SutConnection conn, StatisticMgr statMgr) {
		TpccRte rte = new TpccRte(conn, statMgr, nextWid + 1);
		nextWid = (nextWid + 1) % TpccConstants.NUM_WAREHOUSES;
		return rte;
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.STOP_PROFILING.ordinal());
	}
}
