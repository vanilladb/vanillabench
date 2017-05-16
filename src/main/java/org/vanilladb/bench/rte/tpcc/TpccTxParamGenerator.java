package org.vanilladb.bench.rte.tpcc;

import org.vanilladb.bench.rte.TxParamGenerator;

public interface TpccTxParamGenerator extends TxParamGenerator {
	
	long getKeyingTime();

	long getThinkTime();
	
}
