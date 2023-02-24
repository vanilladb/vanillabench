package org.vanilladb.bench.server.procedure.tpce;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class TpceCheckDatabaseProc extends StoredProcedure<StoredProcedureHelper> {
	private static Logger logger = Logger.getLogger(TpceCheckDatabaseProc.class.getName());
	
	public TpceCheckDatabaseProc() {
		super(StoredProcedureHelper.DEFAULT_HELPER);
	}

	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.INFO))
			logger.info("Checking database for the TPC-E benchmarks...");

		// TODO: Implement verification
		abort("TODO: implement the checking procedure for TPC-E");

		if (logger.isLoggable(Level.INFO))
			logger.info("Checking completed.");
	}
}
