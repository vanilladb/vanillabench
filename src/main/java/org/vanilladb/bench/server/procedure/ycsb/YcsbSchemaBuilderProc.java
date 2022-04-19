package org.vanilladb.bench.server.procedure.ycsb;

import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class YcsbSchemaBuilderProc extends StoredProcedure<YcsbSchemaBuilderSpHelper> {

	public YcsbSchemaBuilderProc() {
		super(new YcsbSchemaBuilderSpHelper());
	}

	@Override
	protected void executeSql() {
		YcsbSchemaBuilderSpHelper helper = getHelper();
		Transaction tx = getTransaction();
		for (String sql : helper.getTableSchemas())
			StoredProcedureUtils.executeUpdate(sql, tx);
		for (String sql : helper.getIndexSchemas())
			StoredProcedureUtils.executeUpdate(sql, tx);
	}
}
