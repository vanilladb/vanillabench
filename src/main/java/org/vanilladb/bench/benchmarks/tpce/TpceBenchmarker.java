package org.vanilladb.bench.benchmarks.tpce;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.benchmarks.tpce.data.TpceDataManager;
import org.vanilladb.bench.benchmarks.tpce.rte.TpceRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;

public class TpceBenchmarker extends Benchmarker {
	
	private TpceDataManager dataMgr;

	public TpceBenchmarker(SutDriver sutDriver) {
		super(sutDriver);
		dataMgr = new TpceDataManager(TpceConstants.CUSTOMER_COUNT, 
				TpceConstants.COMPANY_COUNT, TpceConstants.SECURITY_COUNT);
	}

	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : TpceTransactionType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}

	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(TpceTransactionType.TESTBED_LOADER.ordinal());
	}
	
	protected TpceRte createRte(SutConnection conn, StatisticMgr statMgr) {
		return new TpceRte(conn, statMgr, dataMgr);
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.STOP_PROFILING.ordinal());
	}
}
