package org.vanilladb.bench.benchmarks.ycsb.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.ycsb.YcsbTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;

public class YcsbRte extends RemoteTerminalEmulator<YcsbTransactionType> {
	
	private YcsbTxExecutor executor;
	
	public YcsbRte(SutConnection conn, StatisticMgr statMgr, long sleepTime) {
		super(conn, statMgr, sleepTime);
		executor = new YcsbTxExecutor(new YcsbParamGen());
	}
	
	protected YcsbTransactionType getNextTxType() {
		return YcsbTransactionType.YCSB;
	}
	
	protected TransactionExecutor<YcsbTransactionType> getTxExeutor(YcsbTransactionType type) {
		return executor;
	}
}
