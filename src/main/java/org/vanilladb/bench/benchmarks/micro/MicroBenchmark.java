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
package org.vanilladb.bench.benchmarks.micro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.Benchmark;
import org.vanilladb.bench.VanillaBenchParameters;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.micro.rte.MicrobenchmarkRte;
import org.vanilladb.bench.benchmarks.micro.rte.jdbc.MicrobenchJdbcExecutor;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class MicroBenchmark extends Benchmark {

	@Override
	public Set<BenchTransactionType> getBenchmarkingTxTypes() {
		Set<BenchTransactionType> txTypes = new HashSet<BenchTransactionType>();
		for (MicrobenchTransactionType txType : MicrobenchTransactionType.values()) {
			if (txType.isBenchmarkingProcedure())
				txTypes.add(txType);
		}
		return txTypes;
	}

	@Override
	public void executeLoadingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(MicrobenchTransactionType.TESTBED_LOADER.getProcedureId(),
				MicrobenchConstants.NUM_ITEMS);
	}

	@Override
	public RemoteTerminalEmulator<MicrobenchTransactionType> createRte(SutConnection conn, StatisticMgr statMgr,
			long rteSleepTime) {
		return new MicrobenchmarkRte(conn, statMgr, rteSleepTime);
	}

	@Override
	public boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException {
		SutResultSet result = null;
		MicrobenchTransactionType txnType = MicrobenchTransactionType.CHECK_DATABASE;
		Object[] params = new Object[] {MicrobenchConstants.NUM_ITEMS};
		
		switch (VanillaBenchParameters.CONNECTION_MODE) {
		case JDBC:
			Connection jdbcConn = conn.toJdbcConnection();
			jdbcConn.setAutoCommit(false);
			MicrobenchJdbcExecutor executor = new MicrobenchJdbcExecutor();
			result = executor.execute(jdbcConn, txnType, params);
			break;
		case SP:
			result = conn.callStoredProc(txnType.getProcedureId(), params);
			break;
		}
		
		return result.isCommitted();
	}

	@Override
	public String getBenchmarkName() {
		return "microbench";
	}
}
