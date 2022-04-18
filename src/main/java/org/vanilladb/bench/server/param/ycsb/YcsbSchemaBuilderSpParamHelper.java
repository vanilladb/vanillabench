package org.vanilladb.bench.server.param.ycsb;

import org.vanilladb.bench.benchmarks.ycsb.rte.YcsbSchemaBuilderParamHelper;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class YcsbSchemaBuilderSpParamHelper extends YcsbSchemaBuilderParamHelper
		implements StoredProcedureParamHelper {
	
	@Override
	public void prepareParameters(Object... pars) {
		// nothing to do
	}

	@Override
	public Schema getResultSetSchema() {
		return new Schema();
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		return new SpResultRecord();
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
}
