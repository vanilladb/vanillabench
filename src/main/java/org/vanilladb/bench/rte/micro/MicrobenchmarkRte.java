package org.vanilladb.bench.rte.micro;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.micro.MicroTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;

public class MicrobenchmarkRte extends RemoteTerminalEmulator {
	
	private MicrobenchmarkTxExecutor executor;

	public MicrobenchmarkRte(SutConnection conn, StatisticMgr statMgr) {
		super(conn, statMgr);
		executor = new MicrobenchmarkTxExecutor(new MicrobenchmarkParamGen());
	}
	
	protected TransactionType getNextTxType() {
		return MicroTransactionType.MICRO;
	}
	
	protected TransactionExecutor getTxExeutor(TransactionType type) {
		return executor;
	}
}
