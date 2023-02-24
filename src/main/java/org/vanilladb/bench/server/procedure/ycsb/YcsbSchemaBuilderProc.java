package org.vanilladb.bench.server.procedure.ycsb;

import org.vanilladb.bench.server.param.ycsb.YcsbSchemaBuilderProcParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class YcsbSchemaBuilderProc extends StoredProcedure<YcsbSchemaBuilderProcParamHelper> {

	public YcsbSchemaBuilderProc() {
		super(new YcsbSchemaBuilderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		YcsbSchemaBuilderProcParamHelper paramHelper = getHelper();
		Transaction tx = getTransaction();
		for (String sql : paramHelper.getTableSchemas())
			StoredProcedureUtils.executeUpdate(sql, tx);
		for (String sql : paramHelper.getIndexSchemas())
			StoredProcedureUtils.executeUpdate(sql, tx);
	}
}
