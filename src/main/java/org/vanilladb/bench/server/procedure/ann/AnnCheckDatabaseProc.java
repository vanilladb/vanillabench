package org.vanilladb.bench.server.procedure.ann;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.server.param.ann.AnnTestbedLoaderParamHelper;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;

public class AnnCheckDatabaseProc extends StoredProcedure<AnnTestbedLoaderParamHelper> {

    private static Logger logger = Logger.getLogger(AnnCheckDatabaseProc.class.getName());
    public AnnCheckDatabaseProc() {
        super(new AnnTestbedLoaderParamHelper());
    }

    @Override
    protected void executeSql() {
        if (logger.isLoggable(Level.INFO))
            logger.info("Checking database...");

        // This does nothing for now. 
        // TODO: Implement checking procedure
        
        if (logger.isLoggable(Level.INFO))
            logger.info("Checking completed.");
    }
}
