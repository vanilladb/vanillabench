package org.vanilladb.bench.tpce.rte;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.tpce.TpceTransactionType;

public interface TpceTxParamGenerator extends TxParamGenerator<TpceTransactionType> {

	void onResponseReceived(SutResultSet result);
	
}
