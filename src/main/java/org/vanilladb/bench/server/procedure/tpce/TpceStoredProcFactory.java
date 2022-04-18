/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
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
package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.benchmarks.tpce.TpceTransactionType;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class TpceStoredProcFactory implements StoredProcedureFactory {

	@Override
	public StoredProcedure<?> getStoredProcedure(int pid) {
		StoredProcedure<?> sp;
		switch (TpceTransactionType.fromProcedureId(pid)) {
		case SCHEMA_BUILDER:
			sp = new TpceSchemaBuilderProc();
			break;
		case TESTBED_LOADER:
			sp = new TpceTestbedLoaderProc();
			break;
		case CHECK_DATABASE:
			sp = new TpceCheckDatabaseProc();
			break;
		case TRADE_ORDER:
			sp = new TradeOrderProc();
			break;
		case TRADE_RESULT:
			sp = new TradeResultProc();
			break;
		default:
			throw new UnsupportedOperationException("The benchmarker does not recognize procedure " + pid + "");
		}
		return sp;
	}
}
