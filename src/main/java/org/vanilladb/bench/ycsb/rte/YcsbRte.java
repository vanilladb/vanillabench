package org.vanilladb.bench.ycsb.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.ycsb.YcsbTransactionType;

public class YcsbRte extends RemoteTerminalEmulator {
	
	private YcsbTxExecutor executor;
	
	public YcsbRte(SutConnection conn, StatisticMgr statMgr) {
		super(conn, statMgr);
		executor = new YcsbTxExecutor(new YcsbParamGen());
	}
	
	protected TransactionType getNextTxType() {
		return YcsbTransactionType.YCSB;
	}
	
	protected TransactionExecutor getTxExeutor(TransactionType type) {
		return executor;
	}
}
