package org.vanilladb.bench.rte;

import org.vanilladb.bench.TransactionType;

public interface TxParamGenerator {
	
	TransactionType getTxnType();

	Object[] generateParameter();
	
}
