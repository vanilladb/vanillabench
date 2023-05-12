package org.vanilladb.bench.server.param.ann;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnTestbedLoaderParamHelper implements StoredProcedureHelper {

    private static final String TABLES_DDL[] = {
        "CREATE TABLE items (i_id INT, i_emb VECTOR(128))"
    };

    private int numOfItems;

    public String[] getTableSchemas() {
        return TABLES_DDL;
    }

    public int getNumberOfItems() {
        return numOfItems;
    }

    public String getCollectionName() {
        return "items";
    }

    @Override
    public void prepareParameters(Object... pars) {
        numOfItems = (Integer) pars[0];
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