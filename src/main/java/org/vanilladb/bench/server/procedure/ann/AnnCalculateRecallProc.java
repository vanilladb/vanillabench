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
        super(new AnnSearchParamHelper()); // TODO: change to AnnCalculateRecallParamHelper
    }

    @Override
    protected void executeSql() {
        AnnSearchParamHelper paramHelper = getHelper();
        VectorConstant query = paramHelper.getQuery();
        Transaction tx = getTransaction();

        String nnQuery = "SELECT i_id FROM " + paramHelper.getTableName() + 
            " ORDER BY " + paramHelper.getEmbeddingField() + " <EUC> " + query.toString() + " LIMIT 20";

        // Execute nearest neighbor search
        Scan nearestNeighborScan = StoredProcedureUtils.executeQuery(nnQuery, tx);
        
        nearestNeighborScan.beforeFirst();
        
        Set<Integer> nearestNeighbors = new HashSet<>();

        int count = 0;
        while (nearestNeighborScan.next()) {
            nearestNeighbors.add((Integer) nearestNeighborScan.getVal("i_id").asJavaVal());
            count++;
        }

        nearestNeighborScan.close();

        if (count == 0)
            throw new RuntimeException("Nearest neighbor query execution failed for " + query.toString());
        
        paramHelper.setNearestNeighbors(nearestNeighbors);
    }
}
