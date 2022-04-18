package org.vanilladb.bench.benchmarks.ycsb.rte;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;

public class YcsbSchemaBuilderParamHelper {
	
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
	
	private static final String TABLES_DDL[] = { YCSB_DDL };
	
	private static final String INDEXES_DDL[] = {
			"CREATE INDEX idx_ycsb ON ycsb (ycsb_id)" };

	private static final String TABLES_NAMES[] = { "ycsb" };

	public String[] getTableSchemas() {
		return TABLES_DDL;
	}

	public String[] getIndexSchemas() {
		return INDEXES_DDL;
	}
	
	public String[] getTableNames() {
		return TABLES_NAMES;
	}
}
