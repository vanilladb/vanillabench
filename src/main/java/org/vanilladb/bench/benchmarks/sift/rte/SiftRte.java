package org.vanilladb.bench.benchmarks.sift.rte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.sift.SiftBenchConstants;
import org.vanilladb.bench.benchmarks.sift.SiftTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.sp.VanillaDbSpResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.util.RandomValueGenerator;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.Record;

public class SiftRte extends RemoteTerminalEmulator<SiftTransactionType> {

    private SiftTxExecutor executor;
    private static final int precision = 100;

    static Map<VectorConstant, Set<Integer>> resultMap = new ConcurrentHashMap<>();

    public SiftRte(SutConnection conn, StatisticMgr statMgr, long sleepTime) {
        super(conn, statMgr, sleepTime);
    }

    @Override
    protected SiftTransactionType getNextTxType() {
        RandomValueGenerator rvg = new RandomValueGenerator();

        int flag = (int) (SiftBenchConstants.READ_INSERT_TX_RATE * precision);

        if (rvg.number(0, precision - 1) < flag) {
            return SiftTransactionType.ANN;
        }

        return SiftTransactionType.INSERT;
    }

    @Override
    protected TransactionExecutor<SiftTransactionType> getTxExeutor(SiftTransactionType type) {
        TxParamGenerator<SiftTransactionType> paramGen;
        switch(type) {
            case ANN:
                paramGen = new SiftParamGen();
                break;
            case INSERT:
                paramGen = new SiftInsertParamGen();
                break;
            default:
                paramGen = new SiftParamGen();
                break;
        }
        executor = new SiftTxExecutor(paramGen, resultMap);
        return executor;
    }

    public void executeCalculateRecall(SutConnection conn) throws SQLException {
        List<Double> recallList = new ArrayList<>();

        // iterate over resultMap
        for (Map.Entry<VectorConstant, Set<Integer>> entry : resultMap.entrySet()) {
            VectorConstant query = entry.getKey();
            Set<Integer> approximateNeighbors = entry.getValue();

            ArrayList<Object> paramList = new ArrayList<>();
            // =====================
            // Generating Parameters
            // =====================
            paramList.add(SiftBenchConstants.NUM_DIMENSION);
            for (int i = 0; i < SiftBenchConstants.NUM_DIMENSION; i++) {
                paramList.add(query.get(i));
            }

            VanillaDbSpResultSet recallResultSet = (VanillaDbSpResultSet) conn.callStoredProc(SiftTransactionType.CALCULATE_RECALL.getProcedureId(), paramList.toArray());
            Schema sch = recallResultSet.getSchema();
            Record rec = recallResultSet.getRecords()[0];

            Set<Integer> trueNeighbors = new HashSet<>();

            for (String fld : sch.fields()) {
                if (fld.equals("rc")) {
                    // For record count
                    continue;
                }
                trueNeighbors.add((Integer) rec.getVal(fld).asJavaVal());
            }

            approximateNeighbors.retainAll(trueNeighbors);
            double recallRate = (double) approximateNeighbors.size() / trueNeighbors.size();

            recallList.add(recallRate);
        }

        double sum = 0;
        for (double recallRate : recallList) {
            sum += recallRate;
        }
        double averageRecallRate = sum / recallList.size();

        statMgr.setRecall(averageRecallRate);
    }
    
}
