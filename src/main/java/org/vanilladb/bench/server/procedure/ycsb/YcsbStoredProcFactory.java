package org.vanilladb.bench.server.procedure.ycsb;

import org.vanilladb.bench.benchmarks.ycsb.YcsbTransactionType;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class YcsbStoredProcFactory implements StoredProcedureFactory {

	@Override
	public StoredProcedure<?> getStroredProcedure(int pid) {
		StoredProcedure<?> sp;
		switch (YcsbTransactionType.fromProcedureId(pid)) {
			case SCHEMA_BUILDER:
				sp = new YcsbSchemaBuilderProc();
				break;
			case TESTBED_LOADER:
				sp = new YcsbTestbedLoaderProc();
				break;
			case YCSB:
				sp = new YcsbProc();
				break;
			default:
				throw new UnsupportedOperationException("The benchmarker does not recognize procedure " + pid + "");
		}
		return sp;
	}
}
