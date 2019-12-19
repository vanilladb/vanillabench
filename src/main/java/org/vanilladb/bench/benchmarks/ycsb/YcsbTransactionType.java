package org.vanilladb.bench.benchmarks.ycsb;

import org.vanilladb.bench.BenchTransactionType;

public enum YcsbTransactionType implements BenchTransactionType {
	// Loading procedures
	SCHEMA_BUILDER(true), TESTBED_LOADER(true),
	
	// Benchmark Procedures
	YCSB(false);
	
	public static YcsbTransactionType fromProcedureId(int pid) {
		return YcsbTransactionType.values()[pid];
	}
	
	private boolean isLoadProc;
	
	YcsbTransactionType(boolean isLoadProc) {
		this.isLoadProc = isLoadProc;
	}
	
	@Override
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isLoadingProcedure() {
		return isLoadProc;
	}
}
