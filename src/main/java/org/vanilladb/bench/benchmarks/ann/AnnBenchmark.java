package org.vanilladb.bench.benchmarks.ann;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmark;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.VanillaBenchParameters;
import org.vanilladb.bench.benchmarks.ann.rte.AnnRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AnnBenchmark extends Benchmark {

    @Override
    public Set<BenchTransactionType> getBenchmarkingTxTypes() {
        Set<BenchTransactionType> txTypes = new HashSet<>();
        for (AnnTransactionType txType : AnnTransactionType.values()) {
            if (txType.isBenchmarkingProcedure())
                txTypes.add(txType);
        }
        return txTypes;
    }

    @Override
    public void executeLoadingProcedure(SutConnection conn) throws SQLException {
        conn.callStoredProc(AnnTransactionType.TESTBED_LOADER.getProcedureId(), 
                AnnBenchConstants.NUM_ITEMS);
    }

    @Override
    public boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException {
        SutResultSet result = null;
        AnnTransactionType txnType = AnnTransactionType.CHECK_DATABASE;
        Object[] params = new Object[] {AnnBenchConstants.NUM_ITEMS};

        switch (VanillaBenchParameters.CONNECTION_MODE) {
            case JDBC:
                throw new RuntimeException("No JDBC implementation");
            case SP:
                result = conn.callStoredProc(txnType.getProcedureId(), params);
                break;
        }

        return result.isCommitted();
    }

    @Override
    public RemoteTerminalEmulator<?> createRte(SutConnection conn, StatisticMgr statMgr, long rteSleepTime) {
        return new AnnRte(conn, statMgr, rteSleepTime);
    }

    @Override
    public String getBenchmarkName() {
        return "annbench";
    }
}
