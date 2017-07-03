package org.vanilladb.bench.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.server.procedure.micro.MicrobenchStoredProcFactory;
import org.vanilladb.bench.server.procedure.tpcc.TpccStoredProcFactory;
import org.vanilladb.bench.server.procedure.tpce.TpceStoredProcFactory;
import org.vanilladb.core.remote.storedprocedure.SpStartUp;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class VanillaDbSpStartUp implements SutStartUp {
	private static Logger logger = Logger.getLogger(VanillaDbSpStartUp.class
			.getName());

	public void startup(String[] args) {
		if (logger.isLoggable(Level.INFO))
			logger.info("initing...");
		
		VanillaDb.init(args[0], getStoredProcedureFactory());
		
		if (logger.isLoggable(Level.INFO))
			logger.info("VanillaBench server ready");
		
		try {
			SpStartUp.startUp(1099);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private StoredProcedureFactory getStoredProcedureFactory() {
		StoredProcedureFactory factory = null;
		switch (BenchmarkerParameters.BENCH_TYPE) {
		case MICRO:
			if (logger.isLoggable(Level.INFO))
				logger.info("using Micro-benchmark stored procedures");
			factory = new MicrobenchStoredProcFactory();
			break;
		case TPCC:
			if (logger.isLoggable(Level.INFO))
				logger.info("using TPC-C stored procedures");
			factory = new TpccStoredProcFactory();
			break;
		case TPCE:
			if (logger.isLoggable(Level.INFO))
				logger.info("using TPC-E stored procedures");
			factory = new TpceStoredProcFactory();
			break;
		}
		return factory;
	}

}
