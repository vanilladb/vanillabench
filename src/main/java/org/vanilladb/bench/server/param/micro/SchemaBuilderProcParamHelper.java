package org.vanilladb.bench.server.param.micro;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class SchemaBuilderProcParamHelper extends StoredProcedureParamHelper {

	private final String TABLES_DDL[] = {
			"CREATE TABLE item ( i_id INT, i_im_id INT, i_name VARCHAR(24), "
					+ "i_price DOUBLE, i_data VARCHAR(50) )" };
	private final String INDEXES_DDL[] = {
			"CREATE INDEX idx_item ON item (i_id)" };

	private final String TABLES_NAMES[] = { "item" };

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
