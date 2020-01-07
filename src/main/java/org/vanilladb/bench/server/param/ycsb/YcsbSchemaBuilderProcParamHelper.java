package org.vanilladb.bench.server.param.ycsb;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class YcsbSchemaBuilderProcParamHelper extends StoredProcedureParamHelper {
	private static final String YCSB_DDL;
	
	static {
		String sql = "CREATE TABLE ycsb ( ycsb_id VARCHAR("
				+ YcsbConstants.CHARS_PER_FIELD + ")";
		
		for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
			sql += ", ycsb_" + count
					+ " VARCHAR(" + YcsbConstants.CHARS_PER_FIELD + ")";
		}
		sql += ")";
		
		YCSB_DDL = sql;
	}
	
	private final String TABLES_DDL[] = { YCSB_DDL };
	
	private final String INDEXES_DDL[] = {
			"CREATE INDEX idx_ycsb ON ycsb (ycsb_id)" };

	private final String TABLES_NAMES[] = { "ycsb" };

	public String[] getTableSchemas() {
		return TABLES_DDL;
	}

	public String[] getIndexSchemas() {
		return INDEXES_DDL;
	}
	
	public String[] getTableNames() {
		return TABLES_NAMES;
	}

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
}