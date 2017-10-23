package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.param.tpcc.TpccSchemaBuilderProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;

public class TpccSchemaBuilderProc extends BasicStoredProcedure<TpccSchemaBuilderProcParamHelper> {

	public TpccSchemaBuilderProc() {
		super(new TpccSchemaBuilderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		for (String cmd : paramHelper.getTableSchemas())
			executeUpdate(cmd);
		for (String cmd : paramHelper.getIndexSchemas())
			executeUpdate(cmd);
	}
}