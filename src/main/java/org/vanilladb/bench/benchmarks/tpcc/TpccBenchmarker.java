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
package org.vanilladb.bench.benchmarks.tpcc;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.Benchmarker;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.benchmarks.tpcc.rte.TpccRte;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class TpccBenchmarker extends Benchmarker {
	
	private int nextWid = 0;
	
	public TpccBenchmarker(SutDriver sutDriver) {
		super(sutDriver, "tpcc");
	}

	public TpccBenchmarker(SutDriver sutDriver, String reportPostfix) {
		super(sutDriver, "tpcc-" + reportPostfix);
	}
	
	@Override
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (TpccTransactionType txType : TpccTransactionType.values()) {
			if (!txType.isLoadingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}

	@Override
	protected void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(TpccTransactionType.SCHEMA_BUILDER.getProcedureId());
		conn.callStoredProc(TpccTransactionType.TESTBED_LOADER.getProcedureId());
	}

	@Override
	protected RemoteTerminalEmulator<TpccTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		TpccRte rte = new TpccRte(conn, statMgr, nextWid + 1);
		nextWid = (nextWid + 1) % TpccConstants.NUM_WAREHOUSES;
		return rte;
	}
}
