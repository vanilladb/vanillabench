/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.param.tpcc.TpccSchemaBuilderSpHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class TpccSchemaBuilderProc extends StoredProcedure<TpccSchemaBuilderSpHelper> {

	public TpccSchemaBuilderProc() {
		super(new TpccSchemaBuilderSpHelper());
	}

	@Override
	protected void executeSql() {
		TpccSchemaBuilderSpHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();
		for (String sql : paramHelper.getTableSchemas())
			StoredProcedureUtils.executeUpdate(sql, tx);
		for (String sql : paramHelper.getIndexSchemas())
			StoredProcedureUtils.executeUpdate(sql, tx);
	}
}