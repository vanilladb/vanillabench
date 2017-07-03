package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.param.tpcc.SchemaBuilderProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;

public class SchemaBuilderProc extends BasicStoredProcedure<SchemaBuilderProcParamHelper> {

	public SchemaBuilderProc() {
		super(new SchemaBuilderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		for (String cmd : paramHelper.getTableSchemas())
			executeUpdate(cmd);
		for (String cmd : paramHelper.getIndexSchemas())
			executeUpdate(cmd);
	}
}