package org.vanilladb.bench.server.procedure.sift;

import org.vanilladb.bench.server.param.sift.SiftInsertParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;

public class SiftInsertProc extends StoredProcedure<SiftInsertParamHelper> {

    public SiftInsertProc() {
        super(new SiftInsertParamHelper());
    }

    @Override
    protected void executeSql() {
        SiftInsertParamHelper paramHelper = getHelper();
        VectorConstant v = paramHelper.getNewVector();
        String sql = "INSERT INTO sift(i_id, i_emb) VALUES (" + paramHelper.getId() + ", " + v.toString() + ")";
        StoredProcedureUtils.executeUpdate(sql, getTransaction());
    }   
}
