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
package org.vanilladb.bench.benchmarks.recon.rte;

import java.util.HashMap;
import java.util.Map;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.recon.ReconbenchTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.util.BenchProperties;

public class ReconbenchmarkRte extends RemoteTerminalEmulator<ReconbenchTransactionType> {
	
	private Map<BenchTransactionType, ReconbenchmarkTxExecutor> executors;
	private long priviousTime = 0l;
	
	// Index Update Frequency
	private static final int UPDATE_TIME_PER_SECOND;
	private static final int UPDATE_PERIOD;
	
	static {
		UPDATE_TIME_PER_SECOND = BenchProperties.getLoader()
				.getPropertyAsInteger(ReconbenchmarkRte.class.getName() + ".UPDATE_TIME_PER_SECOND", 1);
		UPDATE_PERIOD = 1000 / UPDATE_TIME_PER_SECOND;
	}
	
	public ReconbenchmarkRte(SutConnection conn, StatisticMgr statMgr) {
		super(conn, statMgr);
		executors = new HashMap<BenchTransactionType, ReconbenchmarkTxExecutor>();
		executors.put(ReconbenchTransactionType.RECON, new ReconbenchmarkTxExecutor(new ReconParamGen()));
		executors.put(ReconbenchTransactionType.EXECUTE, new ReconbenchmarkTxExecutor(new ExecuteParamGen()));
		executors.put(ReconbenchTransactionType.UPDATE, new ReconbenchmarkTxExecutor(new UpdateParamGen()));
	}
	
	protected ReconbenchTransactionType getNextTxType() {
		if (rteId == 0)
			return ReconbenchTransactionType.UPDATE;
		else
			return ReconbenchTransactionType.RECON;
	}

	protected ReconbenchmarkTxExecutor getTxExeutor(ReconbenchTransactionType type) {
		return executors.get(type);
	}
	
	@ Override
	protected void sleep() {
		if (rteId == 0) {
			while(System.currentTimeMillis() - priviousTime < UPDATE_PERIOD) {
				try {
					Thread.sleep(UPDATE_PERIOD / 10);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
			priviousTime = System.currentTimeMillis();
		} else {
			if (BenchmarkerParameters.RTE_SLEEP_TIME > 0) {
				try {
					Thread.sleep(BenchmarkerParameters.RTE_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
}
