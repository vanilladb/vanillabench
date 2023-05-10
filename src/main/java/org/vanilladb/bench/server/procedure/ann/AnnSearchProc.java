package org.vanilladb.bench.server.procedure.ann;

import org.vanilladb.bench.server.param.ann.AnnSearchParamHelper;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.parse.QueryData;
import org.vanilladb.core.query.parse.VectorQueryData;
import org.vanilladb.core.query.planner.Planner;
import org.vanilladb.core.query.planner.vector.VectorQueryPlanner;
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

        VectorQueryData annQuery = new VectorQueryData(query, paramHelper.getCollectionName(), true);
        // Execute Top-k ANN search
        Plan approximatePlan = planner.createVectorSearchPlan(annQuery, tx);
        // Execute True KNN search
        VectorQueryData trueKnnQuery = new VectorQueryData(query, paramHelper.getCollectionName(), false);
        Plan truePlan = planner.createVectorSearchPlan(trueKnnQuery, tx);
        
        // Calculate recall

    }
}
