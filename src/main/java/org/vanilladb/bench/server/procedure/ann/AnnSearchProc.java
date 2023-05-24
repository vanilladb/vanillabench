package org.vanilladb.bench.server.procedure.ann;

import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.server.param.ann.AnnSearchParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.algebra.Scan;
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

        String nnQuery = "SELECT i_id FROM " + paramHelper.getCollectionName() + 
            " ORDER BY " + paramHelper.getEmbeddingField() + " <EUC> " + query.toString() + " LIMIT 20";

        // Execute nearest neighbor search
        Scan approximateScan = StoredProcedureUtils.executeQuery(nnQuery, tx);
        
        approximateScan.beforeFirst();
        
        Set<Integer> approximateResult = new HashSet<>();

        while(approximateScan.next()) {
            approximateResult.add((Integer) approximateScan.getVal("i_id").asJavaVal());
        }
    }
}
