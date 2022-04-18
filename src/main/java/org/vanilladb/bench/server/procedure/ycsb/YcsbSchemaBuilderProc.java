package org.vanilladb.bench.server.procedure.ycsb;

import org.vanilladb.bench.server.param.ycsb.YcsbSchemaBuilderSpParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class YcsbSchemaBuilderProc extends StoredProcedure<YcsbSchemaBuilderSpParamHelper> {

	public YcsbSchemaBuilderProc() {
		super(new YcsbSchemaBuilderSpParamHelper());
	}

	@Override
	protected void executeSql() {
		YcsbSchemaBuilderSpParamHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();
		for (String sql : paramHelper.getTableSchemas())
			StoredProcedureHelper.executeUpdate(sql, tx);
		for (String sql : paramHelper.getIndexSchemas())
			StoredProcedureHelper.executeUpdate(sql, tx);
	}
}
