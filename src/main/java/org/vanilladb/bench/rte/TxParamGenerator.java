package org.vanilladb.bench.rte;

import org.vanilladb.bench.TransactionType;

public interface TxParamGenerator<T extends TransactionType> {
	
	T getTxnType();

	Object[] generateParameter();
	
}
