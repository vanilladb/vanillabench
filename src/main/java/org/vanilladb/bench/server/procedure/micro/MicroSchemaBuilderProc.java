package org.vanilladb.bench.server.procedure.micro;

import org.vanilladb.bench.server.param.micro.MicroSchemaBuilderProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;

public class MicroSchemaBuilderProc extends BasicStoredProcedure<MicroSchemaBuilderProcParamHelper> {

	public MicroSchemaBuilderProc() {
		super(new MicroSchemaBuilderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		for (String cmd : paramHelper.getTableSchemas())
			executeUpdate(cmd);
		for (String cmd : paramHelper.getIndexSchemas())
			executeUpdate(cmd);
	}
}