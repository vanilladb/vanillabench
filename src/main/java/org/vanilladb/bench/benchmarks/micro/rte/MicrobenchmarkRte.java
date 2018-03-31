package org.vanilladb.bench.benchmarks.micro.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.micro.MicroTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class MicrobenchmarkRte extends RemoteTerminalEmulator<MicroTransactionType> {
	
	private MicrobenchmarkTxExecutor executor;

	public MicrobenchmarkRte(SutConnection conn, StatisticMgr statMgr) {
		super(conn, statMgr);
		executor = new MicrobenchmarkTxExecutor(new MicrobenchmarkParamGen());
	}
	
	protected MicroTransactionType getNextTxType() {
		return MicroTransactionType.MICRO;
	}
	
	protected MicrobenchmarkTxExecutor getTxExeutor(MicroTransactionType type) {
		return executor;
	}
}
