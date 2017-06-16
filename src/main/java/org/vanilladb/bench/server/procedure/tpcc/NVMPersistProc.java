package org.vanilladb.bench.server.procedure.tpcc;

import java.util.logging.Logger;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;

public class NVMPersistProc implements StoredProcedure {
	private static Logger logger = Logger.getLogger(NVMPersistProc.class
			.getName());

	@Override
	public void prepare(Object... pars) {
		
	}

	@Override
	public SpResultSet execute() {
		logger.info("Start persisting data structure in NVM");
		VanillaDb.nvmLogMgr().persist();
		logger.info("Data structure in NVM has been persisted");

		return new SpResultSet(null);
	}

}
