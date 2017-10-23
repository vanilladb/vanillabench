package org.vanilladb.bench.tpce;

import org.vanilladb.bench.TransactionType;

public enum TpceTransactionType implements TransactionType {
	// Loading procedures
	SCHEMA_BUILDER, TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// TPC-E procedures
	BROKER_VOLUME, CUSTOMER_POSITION, MARKET_FEED, MARKET_WATCH,
	SECURITY_DETAIL, TRADE_LOOKUP, TRADE_ORDER, TRADE_RESULT,
	TRADE_STATUS, TRADE_UPDATE, DATA_MAINTENANCE, TRADE_CLEANUP;
	
	public static TpceTransactionType fromProcedureId(int pid) {
		return TpceTransactionType.values()[pid];
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
