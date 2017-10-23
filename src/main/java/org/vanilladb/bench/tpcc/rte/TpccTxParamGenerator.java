package org.vanilladb.bench.tpcc.rte;

import org.vanilladb.bench.rte.TxParamGenerator;

public interface TpccTxParamGenerator extends TxParamGenerator {
	
	long getKeyingTime();

	long getThinkTime();
	
}
