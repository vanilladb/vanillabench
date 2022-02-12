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
package org.vanilladb.bench.rte;

import java.util.concurrent.atomic.AtomicInteger;

import org.vanilladb.bench.BenchTransactionType;
import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.remote.SutConnection;

public abstract class RemoteTerminalEmulator<T extends BenchTransactionType> extends Thread {

	private static AtomicInteger rteCount = new AtomicInteger(0);

	private volatile boolean stopBenchmark;
	private volatile boolean isWarmingUp = true;
	private SutConnection conn;
	private StatisticMgr statMgr;
	private long sleepTime;
	
	public RemoteTerminalEmulator(SutConnection conn, StatisticMgr statMgr,
			long sleepTime) {
		this.conn = conn;
		this.statMgr = statMgr;
		this.sleepTime = sleepTime;
		
		// Set the thread name
		setName("RTE-" + rteCount.getAndIncrement());
	}

	@Override
	public void run() {
		while (!stopBenchmark) {
			TxnResultSet rs = executeTxnCycle(conn);
			if (!isWarmingUp)
				statMgr.processTxnResult(rs);
			
			// Sleep for a while
			if (sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	public void startRecordStatistic() {
		statMgr.setRecordStartTime();
		isWarmingUp = false;
	}

	public void stopBenchmark() {
		stopBenchmark = true;
	}

	protected abstract T getNextTxType();
	
	protected abstract TransactionExecutor<T> getTxExeutor(T type);

	private TxnResultSet executeTxnCycle(SutConnection conn) {
		T txType = getNextTxType();
		TransactionExecutor<T> executor = getTxExeutor(txType);
		return executor.execute(conn);
	}
}