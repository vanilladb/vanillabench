package org.vanilladb.bench.server.procedure.ann;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.server.param.ann.AnnTestbedLoaderParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.parse.InsertData;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

public class AnnTestbedLoaderProc extends StoredProcedure<AnnTestbedLoaderParamHelper> {
    private static Logger logger = Logger.getLogger(AnnTestbedLoaderProc.class.getName());
    
    public AnnTestbedLoaderProc() {
        super(new AnnTestbedLoaderParamHelper());
    }

    @Override
    protected void executeSql() {
        if (logger.isLoggable(Level.INFO))
            logger.info("Start loading testbed...");

        // turn off logging set value to speed up loading process
        RecoveryMgr.enableLogging(false);

        dropOldData();
        createSchemas();

        // Generate item records
        generateItems(1, getHelper().getNumberOfItems());

        if (logger.isLoggable(Level.INFO))
            logger.info("Loading completed. Flush all loading data to disks...");

        RecoveryMgr.enableLogging(true);

        // Create a checkpoint
        CheckpointTask cpt = new CheckpointTask();
        cpt.createCheckpoint();

        // Delete the log file and create a new one
        VanillaDb.logMgr().removeAndCreateNewLog();

        if (logger.isLoggable(Level.INFO))
            logger.info("Loading procedure finished.");
    }

    private void dropOldData() {
        if (logger.isLoggable(Level.WARNING))
            logger.warning("Dropping is skipped.");
    }

    private void createSchemas() {
        AnnTestbedLoaderParamHelper paramHelper = getHelper();
        Transaction tx = getTransaction();

        if (logger.isLoggable(Level.FINE))
            logger.info("Creating collections...");

        for (String sql : paramHelper.getTableSchemas())
            StoredProcedureUtils.executeUpdate(sql, tx);
        
        if (logger.isLoggable(Level.FINE))
            logger.info("Finish creating schemas.");
    }

    private void generateItems(int startIId, int endIId) {
        if (logger.isLoggable(Level.FINE))
            logger.info("Start populating items from i_id " + startIId + " to " + endIId + "...");

        Transaction tx = getTransaction();

        int dim = getHelper().getVecDimension();

        for (int i = startIId; i <= endIId; i++) {
            int iid = i;
            List<String> fields = new ArrayList<>(Arrays.asList("i_id", "i_emb"));
            List<Constant> vals = new ArrayList<>(Arrays.asList(new IntegerConstant(iid), new VectorConstant(dim)));

            InsertData sql = new InsertData(getHelper().getCollectionName(), fields, vals);
            StoredProcedureUtils.executeInsert(sql, tx);
        }
        
        if (logger.isLoggable(Level.FINE))
            logger.info("Finish populating items.");
    }
}
