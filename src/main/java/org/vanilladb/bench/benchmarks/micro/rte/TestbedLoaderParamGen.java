package org.vanilladb.bench.benchmarks.micro.rte;

import org.vanilladb.bench.benchmarks.micro.MicrobenchmarkTxnType;
import org.vanilladb.bench.benchmarks.micro.MicrobenchConstants;
import org.vanilladb.bench.rte.TxParamGenerator;

public class TestbedLoaderParamGen implements TxParamGenerator<MicrobenchmarkTxnType> {

	@Override
	public MicrobenchmarkTxnType getTxnType() {
		return MicrobenchmarkTxnType.TESTBED_LOADER;
	}

	@Override
	public Object[] generateParameter() {
		// [# of items]
		return new Object[] {MicrobenchConstants.NUM_ITEMS};
	}

}
