package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.param.tpcc.SchemaBuilderProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.server.VanillaDb;

public class SchemaBuilderProc extends BasicStoredProcedure<SchemaBuilderProcParamHelper> {

	public SchemaBuilderProc() {
		super(new SchemaBuilderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		for (String cmd : paramHelper.getTableSchemas())
			VanillaDb.newPlanner().executeUpdate(cmd, tx);
		for (String cmd : paramHelper.getIndexSchemas())
			VanillaDb.newPlanner().executeUpdate(cmd, tx);
	}
}