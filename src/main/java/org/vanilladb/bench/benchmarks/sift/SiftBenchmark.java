package org.vanilladb.bench.benchmarks.sift;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmark;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.VanillaBenchParameters;
import org.vanilladb.bench.benchmarks.sift.rte.SiftRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class SiftBenchmark extends Benchmark {

    @Override
    public Set<BenchTransactionType> getBenchmarkingTxTypes() {
        Set<BenchTransactionType> txTypes = new HashSet<>();
        for (SiftTransactionType txType : SiftTransactionType.values()) {
            if (txType.isBenchmarkingProcedure())
                txTypes.add(txType);
        }
        return txTypes;
    }

    @Override
    public void executeLoadingProcedure(SutConnection conn) throws SQLException {
        Object[] params = new Object[] {SiftBenchConstants.NUM_ITEMS};
        conn.callStoredProc(SiftTransactionType.TESTBED_LOADER.getProcedureId(), params);
    }

    @Override
    public boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException {
        SutResultSet result = null;
        SiftTransactionType txnType = SiftTransactionType.CHECK_DATABASE;
        Object[] params = new Object[] {SiftBenchConstants.NUM_ITEMS, SiftBenchConstants.NUM_DIMENSION};
        
        switch (VanillaBenchParameters.CONNECTION_MODE) {
            case JDBC:
                throw new RuntimeException("JDBC unimplemented");
            case SP:
                result = conn.callStoredProc(txnType.getProcedureId(), params);
                break;
        }
        return result.isCommitted();
    }

    @Override
    public RemoteTerminalEmulator<?> createRte(SutConnection conn, StatisticMgr statMgr, long rteSleepTime) {
        return new SiftRte(conn, statMgr, rteSleepTime);
    }

    @Override
    public String getBenchmarkName() {
        return "siftbench";
    }
}
