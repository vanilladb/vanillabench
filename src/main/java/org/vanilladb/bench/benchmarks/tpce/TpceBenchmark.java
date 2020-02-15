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
package org.vanilladb.bench.benchmarks.tpce;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmark;
import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.tpce.data.TpceDataManager;
import org.vanilladb.bench.benchmarks.tpce.rte.TpceRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class TpceBenchmark extends Benchmark {
	
	private TpceDataManager dataMgr;
	
	@Override
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (TpceTransactionType txType : TpceTransactionType.values()) {
			if (txType.isBenchmarkingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}

	@Override
	public void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.SCHEMA_BUILDER.getProcedureId());
		conn.callStoredProc(TpceTransactionType.TESTBED_LOADER.getProcedureId());
	}

	@Override
	public RemoteTerminalEmulator<TpceTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new TpceRte(conn, statMgr, dataMgr);
	}

	@Override
	public boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException {
		SutResultSet result = null;
		TpceTransactionType txnType = TpceTransactionType.CHECK_DATABASE;
		Object[] params = new Object[0];
		
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			throw new RuntimeException("We do not implement checking procedure for JDBC");
		case SP:
			result = conn.callStoredProc(txnType.getProcedureId(), params);
			break;
		}
		
		return result.isCommitted();
	}

	@Override
	public String getBenchmarkName() {
		return "tpce";
	}
}
