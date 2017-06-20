package org.vanilladb.bench.tpce.rte;

import org.vanilladb.bench.rte.TxParamGenerator;

public interface TpceTxParamGenerator extends TxParamGenerator {

	void onResponseReceived();
	
}
