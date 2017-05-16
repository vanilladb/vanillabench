package org.vanilladb.bench.server.procedure.micro;

import org.vanilladb.bench.micro.MicroTransactionType;
import org.vanilladb.bench.server.procedure.StartProfilingProc;
import org.vanilladb.bench.server.procedure.StopProfilingProc;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class MicrobenchStoredProcFactory implements StoredProcedureFactory {

	@Override
	public StoredProcedure getStroredProcedure(int pid) {
		StoredProcedure sp;
		switch (MicroTransactionType.fromProcedureId(pid)) {
		case SCHEMA_BUILDER:
			sp = new SchemaBuilderProc();
			break;
		case TESTBED_LOADER:
			sp = new TestbedLoaderProc();
			break;
		case START_PROFILING:
			sp = new StartProfilingProc();
			break;
		case STOP_PROFILING:
			sp = new StopProfilingProc();
			break;
		case MICRO:
			sp = new MicroBenchmarkProc();
			break;
		default:
			sp = null;
		}
		return sp;
	}
}
