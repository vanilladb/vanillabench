package org.vanilladb.bench.server.procedure.ann;

import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.server.param.ann.AnnSearchParamHelper;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.query.parse.VectorQueryData;
import org.vanilladb.core.query.planner.Planner;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class AnnSearchProc extends StoredProcedure<AnnSearchParamHelper> {
    public AnnSearchProc() {
        super(new AnnSearchParamHelper());
    }

    @Override
    protected void executeSql() {
        AnnSearchParamHelper paramHelper = getHelper();
        VectorConstant query = paramHelper.getQuery();
        Transaction tx = getTransaction();
        Planner planner = VanillaDb.newPlanner();

        VectorQueryData annQuery = new VectorQueryData(query, paramHelper.getCollectionName(), paramHelper.getEmbeddingField(), false);
        // Execute Top-k ANN search
        Plan approximatePlan = planner.createVectorSearchPlan(annQuery, tx);
        // Execute True KNN search
        VectorQueryData trueKnnQuery = new VectorQueryData(query, paramHelper.getCollectionName(), paramHelper.getEmbeddingField(), false);
        Plan truePlan = planner.createVectorSearchPlan(trueKnnQuery, tx);
        
        Scan approximateScan = approximatePlan.open();
        approximateScan.beforeFirst();
        Scan trueScan = truePlan.open();
        trueScan.beforeFirst();

        // Calculate recall
        double recall = 0.0;
        
        int k = 10;
        
        Set<Integer> groundTruth = new HashSet<>();
        Set<Integer> approximateResult = new HashSet<>();

        for (int i = 0; i < k; i++) {
            if (approximateScan.next()) {
                approximateResult.add((Integer) approximateScan.getVal("i_id").asJavaVal());
            }
            if (trueScan.next()) {
                groundTruth.add((Integer) trueScan.getVal("i_id").asJavaVal());
            }
        }

        if (groundTruth.size() != approximateResult.size()) {
            throw new IllegalArgumentException("Ground truth size: " + groundTruth.size() + ", approximate result size: " + approximateResult.size());
        }

        for (Integer id : groundTruth) {
            if (approximateResult.contains(id)) {
                recall += 1.0;
            }
        }

        System.out.println("Recall: " + recall / groundTruth.size());
    }
}
