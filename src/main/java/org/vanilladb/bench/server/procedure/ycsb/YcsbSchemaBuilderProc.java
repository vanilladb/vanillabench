package org.vanilladb.bench.server.procedure.ycsb;

import org.vanilladb.bench.server.param.ycsb.YcsbSchemaBuilderProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;

public class YcsbSchemaBuilderProc extends BasicStoredProcedure<YcsbSchemaBuilderProcParamHelper> {

	public YcsbSchemaBuilderProc() {
		super(new YcsbSchemaBuilderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		for (String cmd : paramHelper.getTableSchemas())
			executeUpdate(cmd);
		for (String cmd : paramHelper.getIndexSchemas())
			executeUpdate(cmd);
	}
}
