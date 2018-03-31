package org.vanilladb.bench.benchmarks.tpcc.rte;

import org.vanilladb.bench.benchmarks.tpcc.TpccTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;

public interface TpccTxParamGenerator extends TxParamGenerator<TpccTransactionType> {
	
	long getKeyingTime();

	long getThinkTime();
	
}
