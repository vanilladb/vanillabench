package org.vanilladb.bench.server.procedure.sift;


import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.server.param.sift.SiftBenchParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class SiftCalculateRecallProc extends StoredProcedure<SiftBenchParamHelper> {

    public SiftCalculateRecallProc() {
        super(new SiftBenchParamHelper());
    }

    @Override
    protected void executeSql() {
        SiftBenchParamHelper paramHelper = getHelper();
        VectorConstant query = paramHelper.getQuery();
        Transaction tx = getTransaction();

        // Execute true earest neighbor search
        Scan trueNeighborScan = StoredProcedureUtils.executeCalculateRecall(
            query, paramHelper.getTableName(), paramHelper.getEmbeddingField(), paramHelper.getK(), tx);
        
        trueNeighborScan.beforeFirst();
        
        Set<Integer> nearestNeighbors = new HashSet<>();

        int count = 0;
        while (trueNeighborScan.next()) {
            nearestNeighbors.add((Integer) trueNeighborScan.getVal("i_id").asJavaVal());
            count++;
        }

        trueNeighborScan.close();

        if (count == 0)
            throw new RuntimeException("Nearest neighbor query execution failed for " + query.toString());
        
        paramHelper.setNearestNeighbors(nearestNeighbors);
    }
}
