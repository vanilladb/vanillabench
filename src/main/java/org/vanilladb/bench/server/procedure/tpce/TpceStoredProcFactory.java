package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.procedure.StartProfilingProc;
import org.vanilladb.bench.server.procedure.StopProfilingProc;
import org.vanilladb.bench.tpce.TpceTransactionType;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class TpceStoredProcFactory implements StoredProcedureFactory {

	@Override
	public StoredProcedure getStroredProcedure(int pid) {
		StoredProcedure sp;
		switch (TpceTransactionType.fromProcedureId(pid)) {
		case SCHEMA_BUILDER:
			sp = new TpceSchemaBuilderProc();
			break;
		case TESTBED_LOADER:
			sp = new TpceTestbedLoaderProc();
			break;
		case START_PROFILING:
			sp = new StartProfilingProc();
			break;
		case STOP_PROFILING:
			sp = new StopProfilingProc();
			break;
		case TRADE_ORDER:
			sp = new TradeOrderProc();
			break;
		case TRADE_RESULT:
			sp = new TradeResultProc();
			break;
		default:
			throw new UnsupportedOperationException("Procedure " + TpceTransactionType.fromProcedureId(pid) + " is not supported for now");
		}
		return sp;
	}
}
