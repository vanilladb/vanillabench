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
package org.vanilladb.bench.benchmarks.recon;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmark;
import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class ReconBenchmark extends Benchmark {

	@Override
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (ReconbenchTransactionType txType : ReconbenchTransactionType.values()) {
			if (txType.isBenchmarkingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}

	@Override
	public void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(ReconbenchTransactionType.TESTBED_LOADER.getProcedureId(),
				ReconbenchConstants.NUM_ITEMS);
	}

	@Override
	public RemoteTerminalEmulator<ReconbenchTransactionType> createRte(SutConnection conn, StatisticMgr statMgr) {
		throw new RuntimeException("We do not implement procedure for Recon in Vanilla");
//		return new ReconbenchmarkRte(conn, statMgr);
	}

	@Override
	public boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException {
		SutResultSet result = null;
		ReconbenchTransactionType txnType = ReconbenchTransactionType.CHECK_DATABASE;
		Object[] params = new Object[] {ReconbenchConstants.NUM_ITEMS};
		
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			throw new RuntimeException("We do not implement checking procedure for Recon");
		case SP:
			result = conn.callStoredProc(txnType.getProcedureId(), params);
			break;
		}
		
		return result.isCommitted();
	}

	@Override
	public String getBenchmarkName() {
		return "reconbench";
	}
}
