package org.vanilladb.bench.server.param.ann;

import java.util.Set;

import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnSearchParamHelper implements StoredProcedureHelper {
    private final String table = "items";
    private final String embField = "i_emb";
    private VectorConstant query;
    private int numDimension;
    private Integer[] items;
    private int numNeighbors = 20; // Number of top-k

    @Override
    public void prepareParameters(Object... pars) {
        numDimension = (Integer) pars[0];
        int[] rawVector = new int[numDimension];
        for (int i = 0; i < numDimension; i++) {
            rawVector[i] = (int) pars[i+1];
        }
        query = new VectorConstant(rawVector);
        items = new Integer[numNeighbors];
    }

    @Override
    public Schema getResultSetSchema() {
        Schema sch = new Schema();
        sch.addField("rc", Type.INTEGER);
        for (int i = 0; i < numNeighbors; i++) {
            sch.addField("id_" + i, Type.INTEGER);
        }
        return sch;
    }

    @Override
    public SpResultRecord newResultSetRecord() {
        SpResultRecord rec = new SpResultRecord();
        rec.setVal("rc", new IntegerConstant(numNeighbors));

        for (int i = 0; i < numNeighbors; i++) {
            rec.setVal("id_" + i, new IntegerConstant(items[i]));
        }
        return rec;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    public void setNearestNeighbors(Set<Integer> nearestNeighbors) {
        items = nearestNeighbors.toArray(items);
    }

    public String getTableName() {
        return table;
    }

    public String getEmbeddingField() {
        return embField;
    }

    public VectorConstant getQuery() {
        return query;
    }

    public int getK() {
        return numNeighbors;
    }
}
