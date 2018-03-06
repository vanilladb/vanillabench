package org.vanilladb.bench.ycsb;

import org.vanilladb.bench.TransactionType;

public enum YcsbTransactionType implements TransactionType {
	// Loading procedures
	SCHEMA_BUILDER, TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// TPC-C procedures
	YCSB,
	
	// CLAY
	LAUNCH_CLAY, BROADCAST_MIGRAKEYS,

	// Migration
	START_MIGRATION, STOP_MIGRATION, MIGRATION_ANALYSIS, ASYNC_MIGRATE;
	
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
