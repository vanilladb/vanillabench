package org.vanilladb.bench.server.procedure.ann;

import org.vanilladb.bench.server.param.ann.AnnTestbedLoaderParamHelper;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class AnnTestbedLoaderProc extends StoredProcedure<AnnTestbedLoaderParamHelper> {
    public AnnTestbedLoaderProc() {
        super(new AnnTestbedLoaderParamHelper());
    }

    @Override
    protected void executeSql() {

    }
}
