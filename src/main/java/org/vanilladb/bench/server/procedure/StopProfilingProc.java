package org.vanilladb.bench.server.procedure;

import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class StopProfilingProc extends BasicStoredProcedure<StoredProcedureParamHelper> {

	public StopProfilingProc() {
		super(StoredProcedureParamHelper.DefaultParamHelper());
	}

	@Override
	protected void executeSql() {
		VanillaDb.stopProfilerAndReport();
	}

}
