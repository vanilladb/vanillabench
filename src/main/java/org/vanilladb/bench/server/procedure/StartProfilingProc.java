package org.vanilladb.bench.server.procedure;

import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class StartProfilingProc extends BasicStoredProcedure<StoredProcedureParamHelper> {

	public StartProfilingProc() {
		super(StoredProcedureParamHelper.DefaultParamHelper());
	}

	@Override
	protected void executeSql() {
		VanillaDb.initAndStartProfiler();
	}
}
