package org.vanilladb.bench.server.param.ycsb;

import org.vanilladb.bench.ycsb.YcsbConstants;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
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
	public SpResultSet createResultSet() {
		// create schema
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		sch.addField("status", statusType);

		// create record
		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, statusType));

		// create result set
		return new SpResultSet(sch, rec);
	}
}
