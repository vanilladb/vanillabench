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
package org.vanilladb.bench.benchmarks.micro.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.micro.MicrobenchTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class MicrobenchmarkRte extends RemoteTerminalEmulator<MicrobenchTransactionType> {
	
	private MicrobenchmarkTxExecutor executor;

	public MicrobenchmarkRte(SutConnection conn, StatisticMgr statMgr) {
		super(conn, statMgr);
		executor = new MicrobenchmarkTxExecutor(new MicrobenchmarkParamGen());
	}
	
	protected MicrobenchTransactionType getNextTxType() {
		return MicrobenchTransactionType.MICRO_TXN;
	}
	
	protected MicrobenchmarkTxExecutor getTxExeutor(MicrobenchTransactionType type) {
		return executor;
	}
}
