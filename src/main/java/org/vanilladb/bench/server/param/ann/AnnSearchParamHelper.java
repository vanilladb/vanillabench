package org.vanilladb.bench.server.param.ann;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnSearchParamHelper implements StoredProcedureHelper {
    private String collection = "items";
    private String embField = "i_emb";
    private int vecSize;

    @Override
    public void prepareParameters(Object... pars) {
        // TODO: query = something from pars
        // TODO: collection = somethign from pars
        // TODO: embField = something from pars

        vecSize = (Integer) pars[0];
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
        return true;
    }

    public String getCollectionName() {
        return collection;
    }

    public String getEmbeddingField() {
        return embField;
    }

    public VectorConstant getQuery() {
        return new VectorConstant(vecSize);
    }
}
