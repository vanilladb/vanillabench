package org.vanilladb.bench.tpcc.rte;

import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.tpcc.TpccTransactionType;

public interface TpccTxParamGenerator extends TxParamGenerator<TpccTransactionType> {
	
	long getKeyingTime();

	long getThinkTime();
	
}
