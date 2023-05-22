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

        VectorQueryData annQuery = new VectorQueryData(query, paramHelper.getCollectionName(), paramHelper.getEmbeddingField(), true);
        // Execute Top-k ANN search
        Plan approximatePlan = planner.createVectorSearchPlan(annQuery, tx);
        
        Scan approximateScan = approximatePlan.open();
        approximateScan.beforeFirst();
        
        int k = 10;
        
        Set<Integer> approximateResult = new HashSet<>();

        for (int i = 0; i < k; i++) {
            if (approximateScan.next()) {
                approximateResult.add((Integer) approximateScan.getVal("i_id").asJavaVal());
            }
        }
    }
}
