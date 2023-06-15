package org.vanilladb.bench.server.param.ann;

import java.util.ArrayList;
import java.util.List;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnTestbedLoaderParamHelper implements StoredProcedureHelper {

    private static final String TABLES_DDL[] = new String[1];
    private static final String INDEXES_DDL[] = new String[1];

    private int numOfItems, numDimension;

    public String getTableName() {
        return "items";
    }

    public String getIdxName() {
        return "idx_items";
    }

    public List<String> getIdxFields() {
        List<String> embFields = new ArrayList<String>(1);
        embFields.add("i_emb");
        return embFields;
    }

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
        TABLES_DDL[0] = "CREATE TABLE "+ getTableName() + " (i_id INT, i_emb VECTOR(" + numDimension + "), i_name VARCHAR(24))";
        INDEXES_DDL[0] = "CREATE INDEX " + getIdxName()+ " ON items (" + getIdxFields().get(0) + ") USING IVF";
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
