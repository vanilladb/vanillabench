package org.vanilladb.bench.server.procedure.ycsb;

import org.vanilladb.bench.ycsb.YcsbTransactionType;
import org.vanilladb.bench.server.procedure.StartProfilingProc;
import org.vanilladb.bench.server.procedure.StopProfilingProc;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class YcsbStoredProcFactory implements StoredProcedureFactory {

	@Override
	public StoredProcedure getStroredProcedure(int pid) {
		StoredProcedure sp;
		switch (YcsbTransactionType.fromProcedureId(pid)) {
			case SCHEMA_BUILDER:
				sp = new YcsbSchemaBuilderProc();
				break;
			case TESTBED_LOADER:
				sp = new YcsbTestbedLoaderProc();
				break;
			case START_PROFILING:
				sp = new StartProfilingProc();
				break;
			case STOP_PROFILING:
				sp = new StopProfilingProc();
				break;
			case YCSB:
				sp = new YcsbProc();
				break;
			default:
				sp = null;
		}
		return sp;
	}
}
