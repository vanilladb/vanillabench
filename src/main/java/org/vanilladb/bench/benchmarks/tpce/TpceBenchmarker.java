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
import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.tpce.data.TpceDataManager;
import org.vanilladb.bench.benchmarks.tpce.rte.TpceRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class TpceBenchmarker extends Benchmarker {
	
	private TpceDataManager dataMgr;

	public TpceBenchmarker(SutDriver sutDriver) {
		super(sutDriver, "tpce");
		dataMgr = new TpceDataManager(TpceConstants.CUSTOMER_COUNT, 
				TpceConstants.COMPANY_COUNT, TpceConstants.SECURITY_COUNT);
	}

	public TpceBenchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "tpce-" + reportPostfix);
		dataMgr = new TpceDataManager(TpceConstants.CUSTOMER_COUNT, 
				TpceConstants.COMPANY_COUNT, TpceConstants.SECURITY_COUNT);
	}
	
	@Override
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (TpceTransactionType txType : TpceTransactionType.values()) {
			if (!txType.isLoadingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}

	@Override
	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.SCHEMA_BUILDER.getProcedureId());
		conn.callStoredProc(TpceTransactionType.TESTBED_LOADER.getProcedureId());
	}

	@Override
	protected RemoteTerminalEmulator<TpceTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new TpceRte(conn, statMgr, dataMgr);
	}
}
