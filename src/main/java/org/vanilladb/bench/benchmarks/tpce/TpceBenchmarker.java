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

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.benchmarks.tpcc.TpccTransactionType;
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

	public Set<TransactionType> getBenchmarkingTxTypes() {
		Set<TransactionType> txTypes = new HashSet<TransactionType>();
		for (TransactionType txType : TpceTransactionType.values()) {
			if (txType.isBenchmarkingTx())
				txTypes.add(txType);
		}
		return txTypes;
	}

	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.SCHEMA_BUILDER.ordinal());
		conn.callStoredProc(TpceTransactionType.TESTBED_LOADER.ordinal());
	}
	
	protected RemoteTerminalEmulator<TpceTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		return new TpceRte(conn, statMgr, dataMgr);
	}
	
	protected void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.START_PROFILING.ordinal());
	}
	
	protected void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpceTransactionType.STOP_PROFILING.ordinal());
	}
}
