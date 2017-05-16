package org.vanilladb.bench;

public interface TransactionType {
	
	int getProcedureId();
	
	boolean isBenchmarkingTx();
	
}
