package org.vanilladb.bench.benchmarks.ann.rte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.ann.AnnBenchConstants;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.sp.VanillaDbSpResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.Record;

public class AnnRte extends RemoteTerminalEmulator<AnnTransactionType>{

    private AnnTxExecutor executor;

    static Map<VectorConstant, Set<Integer>> resultMap = new ConcurrentHashMap<>();

    public AnnRte(SutConnection conn, StatisticMgr statMgr, long sleepTime) {
        super(conn, statMgr, sleepTime);
        
        executor = new AnnTxExecutor(new AnnParamGen(), resultMap);
    }

    @Override
    protected AnnTransactionType getNextTxType() {
        return AnnTransactionType.ANN;
    }

    @Override
    protected TransactionExecutor<AnnTransactionType> getTxExeutor(AnnTransactionType type) {
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
            paramList.add(AnnBenchConstants.NUM_DIMENSION);
            for (int i = 0; i < AnnBenchConstants.NUM_DIMENSION; i++) {
                paramList.add(query.get(i));
            }

            VanillaDbSpResultSet recallResultSet = (VanillaDbSpResultSet) conn.callStoredProc(AnnTransactionType.CALCULATE_RECALL.getProcedureId(), paramList.toArray());
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
