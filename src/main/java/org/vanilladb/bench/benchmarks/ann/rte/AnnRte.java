package org.vanilladb.bench.benchmarks.ann.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;

public class AnnRte extends RemoteTerminalEmulator<AnnTransactionType>{

    private AnnTxExecutor executor;

    public AnnRte(SutConnection conn, StatisticMgr statMgr, long sleepTime) {
        super(conn, statMgr, sleepTime);
        executor = new AnnTxExecutor(new AnnParamGen());
    }

    @Override
    protected AnnTransactionType getNextTxType() {
        return AnnTransactionType.ANN;
    }

    @Override
    protected TransactionExecutor<AnnTransactionType> getTxExeutor(AnnTransactionType type) {
        return executor;
    }
}
