package org.vanilladb.bench.tpcc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.tpcc.rte.TpccRte;

public class TpccBenchmarker extends Benchmarker {
	
	private int nextWid = 0;
	
	public TpccBenchmarker(SutDriver sutDriver) {
		super(sutDriver);
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
	
	protected RemoteTerminalEmulator createRte(SutConnection conn, StatisticMgr statMgr) {
		RemoteTerminalEmulator rte = new TpccRte(conn, statMgr, nextWid + 1);
		nextWid = (nextWid + 1) % TpccConstants.NUM_WAREHOUSES;
		return rte;
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.STOP_PROFILING.ordinal());
	}
	
	@Override
	protected void executePersistingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.NVM_PERSIST.ordinal());
	}
}
