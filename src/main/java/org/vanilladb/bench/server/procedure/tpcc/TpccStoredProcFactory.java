package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.procedure.StartProfilingProc;
import org.vanilladb.bench.server.procedure.StopProfilingProc;
import org.vanilladb.bench.tpcc.TpccTransactionType;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class TpccStoredProcFactory implements StoredProcedureFactory {

	@Override
	public StoredProcedure getStroredProcedure(int pid) {
		StoredProcedure sp;
		switch (TpccTransactionType.fromProcedureId(pid)) {
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
		case NEW_ORDER:
			sp = new NewOrderProc();
			break;
		case PAYMENT:
			sp = new PaymentProc();
			break;
		default:
			throw new UnsupportedOperationException("Procedure " + TpccTransactionType.fromProcedureId(pid) + " is not supported for now");
		}
		return sp;
	}
}
