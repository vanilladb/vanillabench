package org.vanilladb.bench.server.param.sift;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class SiftInsertParamHelper implements StoredProcedureHelper {

    private int numDimension;
    private VectorConstant query;
    private int id;

    @Override
    public void prepareParameters(Object... pars) {
        numDimension = (Integer) pars[0];
        float[] rawVector = new float[numDimension];
        int i = 0;
        for (; i < numDimension; i++) {
            rawVector[i] = (float) pars[i+1];
        }
        query = new VectorConstant(rawVector);
        id = (int) pars[i+1];
    }

    public int getId() {
        return id;
    }

    public VectorConstant getNewVector() {
        return query;
    }

    @Override
    public Schema getResultSetSchema() {
        return new Schema();
    }

    @Override
    public SpResultRecord newResultSetRecord() {
        return new SpResultRecord();
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
    
}
