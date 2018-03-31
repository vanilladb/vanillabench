package org.vanilladb.bench.benchmarks.micro;

import org.vanilladb.bench.TransactionType;

public enum MicroTransactionType implements TransactionType {
	// Loading procedures
	SCHEMA_BUILDER, TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// TPC-C procedures
	MICRO;
	
	public static MicroTransactionType fromProcedureId(int pid) {
		return MicroTransactionType.values()[pid];
	}
	
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isBenchmarkingTx() {
		if (this == MICRO)
			return true;
		return false;
	}
}
