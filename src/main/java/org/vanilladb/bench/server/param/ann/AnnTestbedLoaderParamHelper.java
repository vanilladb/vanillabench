package org.vanilladb.bench.server.param.ann;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnTestbedLoaderParamHelper implements StoredProcedureHelper {

    private static final String TABLES_DDL[] = new String[1];

    private static final String INDEXES_DDL[] = {
        // Always provide the id column as the first column for vector indexes
        "CREATE INDEX idx_item ON items (i_id, i_emb, i_name) USING LSH"
    };

    private int numOfItems, numDimension;

    public String[] getTableSchemas() {
        return TABLES_DDL;
    }

    public String[] getIndexSchemas() {
        return INDEXES_DDL;
    }

    public int getNumberOfItems() {
        return numOfItems;
    }

    public int getVecDimension() {
        return numDimension;
    }

    @Override
    public void prepareParameters(Object... pars) {
        numOfItems = (Integer) pars[0];
        numDimension = (Integer) pars[1];
        TABLES_DDL[0] = "CREATE TABLE items (i_id INT, i_emb VECTOR(" + numDimension + "), i_name VARCHAR(24))";
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
