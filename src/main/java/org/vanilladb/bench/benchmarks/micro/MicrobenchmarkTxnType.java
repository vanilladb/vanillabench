package org.vanilladb.bench.benchmarks.micro;

import org.vanilladb.bench.TransactionType;

public enum MicrobenchmarkTxnType implements TransactionType {
	// Loading procedures
	TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// Benchmarking procedures
	MICRO_TXN;
	
	public static MicrobenchmarkTxnType fromProcedureId(int pid) {
		return MicrobenchmarkTxnType.values()[pid];
	}
	
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isBenchmarkingTx() {
		if (this == MICRO_TXN)
			return true;
		return false;
	}
}
