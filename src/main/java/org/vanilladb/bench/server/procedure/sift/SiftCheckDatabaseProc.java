package org.vanilladb.bench.server.procedure.sift;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.server.param.sift.SiftTestbedLoaderParamHelper;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;

public class SiftCheckDatabaseProc extends StoredProcedure<SiftTestbedLoaderParamHelper> {

    private static Logger logger = Logger.getLogger(SiftCheckDatabaseProc.class.getName());
    public SiftCheckDatabaseProc() {
        super(new SiftTestbedLoaderParamHelper());
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
