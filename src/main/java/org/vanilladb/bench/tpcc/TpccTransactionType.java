package org.vanilladb.bench.tpcc;

import org.vanilladb.bench.TransactionType;

public enum TpccTransactionType implements TransactionType {
	// Loading procedures
	SCHEMA_BUILDER, TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// TPC-C procedures
	NEW_ORDER, PAYMENT, ORDER_STATUS, DELIVERY, STOCK_LEVEL,
	
	// NVM simulation
	NVM_PERSIST;
	
	public static TpccTransactionType fromProcedureId(int pid) {
		return TpccTransactionType.values()[pid];
	}
	
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isBenchmarkingTx() {
		switch (this) {
		case SCHEMA_BUILDER:
		case TESTBED_LOADER:
		case START_PROFILING:
		case STOP_PROFILING:
			return false;
		default:
			return true;
		}
	}
}
