package org.vanilladb.bench.ycsb;

import org.vanilladb.bench.TransactionType;

public enum YcsbTransactionType implements TransactionType {
	// Loading procedures
	SCHEMA_BUILDER, TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// TPC-C procedures
	YCSB;
	
	public static YcsbTransactionType fromProcedureId(int pid) {
		return YcsbTransactionType.values()[pid];
	}
	
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isBenchmarkingTx() {
		if (this == YCSB)
			return true;
		return false;
	}
}
