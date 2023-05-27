package org.vanilladb.bench.server.procedure.ann;

import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.server.param.ann.AnnSearchParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class AnnCalculateRecallProc extends StoredProcedure<AnnSearchParamHelper> {

    public AnnCalculateRecallProc() {
        super(new AnnSearchParamHelper());
    }

    @Override
    protected void executeSql() {
        AnnSearchParamHelper paramHelper = getHelper();
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
