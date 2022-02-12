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
package org.vanilladb.bench;

import java.sql.SQLException;
import java.util.Set;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public abstract class Benchmark {
	
	public void startProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(ControlTransactionType.START_PROFILING.getProcedureId());
	}
	
	public void stopProfilingProcedure(SutConnection conn) throws SQLException {
		conn.callStoredProc(ControlTransactionType.STOP_PROFILING.getProcedureId());
	}
	
	public int getNumOfRTEs() {
		return VanillaBenchParameters.NUM_RTES;
	}
	
	public abstract Set<BenchTransactionType> getBenchmarkingTxTypes();
	
	public abstract void executeLoadingProcedure(SutConnection conn) throws SQLException;
	
	public abstract boolean executeDatabaseCheckProcedure(SutConnection conn) throws SQLException;
	
	public abstract RemoteTerminalEmulator<?> createRte(SutConnection conn, StatisticMgr statMgr,
			long rteSleepTime);
	
	public abstract String getBenchmarkName();
}
