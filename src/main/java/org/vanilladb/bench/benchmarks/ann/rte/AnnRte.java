package org.vanilladb.bench.benchmarks.ann.rte;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.core.sql.VectorConstant;

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

        // TODO: BUILD A NEW TRUE RESULT MAP
        // Object[] params = new Object[] { AnnBenchConstants.NUM_NEIGHBORS, resultMap };
		// VanillaDbSpResultSet recallResultSet = (VanillaDbSpResultSet) conn.callStoredProc(AnnTransactionType.CALCULATE_RECALL.getProcedureId(), params);

        // for (String fld : recallResultSet.getSchema().fields()) {
        //     System.out.println("Average recall rate: " + fld + ": " + recallResultSet.getRecords()[0].getVal(fld));
        // }

        // statMgr.setRecallRate();
        System.out.println("RECALL RATE: ");
        System.out.println(resultMap);
	}
}
