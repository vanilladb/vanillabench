package org.vanilladb.bench.benchmarks.micro.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.micro.MicrobenchmarkTxnType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class MicrobenchmarkRte extends RemoteTerminalEmulator<MicrobenchmarkTxnType> {
	
	private MicrobenchmarkTxExecutor executor;

	public MicrobenchmarkRte(SutConnection conn, StatisticMgr statMgr) {
		super(conn, statMgr);
		executor = new MicrobenchmarkTxExecutor(new MicrobenchmarkParamGen());
	}
	
	protected MicrobenchmarkTxnType getNextTxType() {
		return MicrobenchmarkTxnType.MICRO_TXN;
	}
	
	protected MicrobenchmarkTxExecutor getTxExeutor(MicrobenchmarkTxnType type) {
		return executor;
	}
}
