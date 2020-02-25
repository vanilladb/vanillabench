package org.vanilladb.bench.benchmarks.ycsb;

import org.vanilladb.bench.BenchTransactionType;

public enum YcsbTransactionType implements BenchTransactionType {
	// Loading procedures
	SCHEMA_BUILDER(false), TESTBED_LOADER(false),
	
	// Database checking procedures
	CHECK_DATABASE(false),
	
	// Benchmark Procedures
	YCSB(true);
	
	public static YcsbTransactionType fromProcedureId(int pid) {
		return YcsbTransactionType.values()[pid];
	}
	
	private boolean isBenchProc;
	
	YcsbTransactionType(boolean isLoadProc) {
		this.isBenchProc = isLoadProc;
	}
	
	@Override
	public int getProcedureId() {
		return this.ordinal();
	}

	@Override
	public boolean isBenchmarkingProcedure() {
		return isBenchProc;
	}
}
