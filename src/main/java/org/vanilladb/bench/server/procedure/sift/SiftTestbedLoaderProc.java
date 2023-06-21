package org.vanilladb.bench.server.procedure.sift;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.sift.SiftBenchConstants;
import org.vanilladb.bench.server.param.sift.SiftTestbedLoaderParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;
import org.vanilladb.core.storage.tx.recovery.CheckpointTask;
import org.vanilladb.core.storage.tx.recovery.RecoveryMgr;

public class SiftTestbedLoaderProc extends StoredProcedure<SiftTestbedLoaderParamHelper> {
    private static Logger logger = Logger.getLogger(SiftTestbedLoaderProc.class.getName());
    
    public SiftTestbedLoaderProc() {
        super(new SiftTestbedLoaderParamHelper());
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
        generateItems(0);

        // if (logger.isLoggable(Level.INFO))
        //     logger.info("Training IVF index...");

        // StoredProcedureUtils.executeTrainIndex(getHelper().getTableName(), getHelper().getIdxFields(), 
        //     getHelper().getIdxName(), getTransaction());

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
        SiftTestbedLoaderParamHelper paramHelper = getHelper();
        Transaction tx = getTransaction();

        if (logger.isLoggable(Level.FINE))
            logger.info("Creating tables...");

        for (String sql : paramHelper.getTableSchemas())
            StoredProcedureUtils.executeUpdate(sql, tx);

        // if (logger.isLoggable(Level.INFO))
        //     logger.info("Creating indexes...");

        // // Create indexes
        // for (String sql : paramHelper.getIndexSchemas())
        //     StoredProcedureUtils.executeUpdate(sql, tx);
        
        if (logger.isLoggable(Level.FINE))
            logger.info("Finish creating schemas.");
    }

    private void generateItems(int startIId) {
        if (logger.isLoggable(Level.FINE))
            logger.info("Start populating items from SIFT1M dataset");

        Transaction tx = getTransaction();

        try (BufferedReader br = new BufferedReader(new FileReader(SiftBenchConstants.DATASET_FILE))) {
            int iid = startIId;
            String vectorString;

            while (iid < SiftBenchConstants.NUM_ITEMS && (vectorString = br.readLine()) != null) {
                String sql = "INSERT INTO sift(i_id, i_emb) VALUES (" + iid + ", [" + vectorString + "])";
                // logger.info(sql);
                iid++;
                StoredProcedureUtils.executeUpdate(sql, tx);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (logger.isLoggable(Level.FINE))
            logger.info("Finish populating items.");
    }
}
