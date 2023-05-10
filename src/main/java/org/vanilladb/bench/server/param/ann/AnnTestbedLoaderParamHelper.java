package org.vanilladb.bench.server.param.ann;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnTestbedLoaderParamHelper implements StoredProcedureHelper {
    @Override
    public void prepareParameters(Object... pars) {

    }

    @Override
    public Schema getResultSetSchema() {
        return null;
    }

    @Override
    public SpResultRecord newResultSetRecord() {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }
}
