package org.vanilladb.bench.benchmarks.tpce.rte;

import org.vanilladb.bench.benchmarks.tpce.TpceTransactionType;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TxParamGenerator;

public interface TpceTxParamGenerator extends TxParamGenerator<TpceTransactionType> {

	void onResponseReceived(SutResultSet result);
	
}
