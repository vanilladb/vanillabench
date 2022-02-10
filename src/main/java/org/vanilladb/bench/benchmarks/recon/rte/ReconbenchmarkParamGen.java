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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.vanilladb.bench.benchmarks.recon.ReconbenchConstants;
import org.vanilladb.bench.benchmarks.recon.ReconbenchTransactionType;
import org.vanilladb.bench.benchmarks.tpcc.TpccValueGenerator;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.util.BenchProperties;
import org.vanilladb.bench.util.RandomNonRepeatGenerator;

public abstract class ReconbenchmarkParamGen implements TxParamGenerator<ReconbenchTransactionType> {
	
	// Transaaction Type
	private static final double RW_TX_RATE;
	private static final double LONG_READ_TX_RATE;
	
	// Read Counts
	private static final int TOTAL_READ_COUNT;
	private static final int LOCAL_HOT_COUNT;
	// The local cold count will be calculated on the fly
	
	// Access Pattern
	private static final double WRITE_RATIO_IN_RW_TX;
	private static final double HOT_CONFLICT_RATE;
	
	// Data Size
	private static final int DATA_SIZE;
	private static final int HOT_DATA_SIZE;
	private static final int COLD_DATA_SIZE;
	
	// Index Update
	protected static final int INDEX_UPDATE_COUNT;
	
	// Other parameters
	private static final int RANDOM_SWAP_FACTOR = 20;

	static {
		RW_TX_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(ReconbenchmarkParamGen.class.getName() + ".RW_TX_RATE", 0.0);
		LONG_READ_TX_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(ReconbenchmarkParamGen.class.getName() + ".LONG_READ_TX_RATE", 0.0);
		
		TOTAL_READ_COUNT = BenchProperties.getLoader()
				.getPropertyAsInteger(ReconbenchmarkParamGen.class.getName() + ".TOTAL_READ_COUNT", 10);
		LOCAL_HOT_COUNT = BenchProperties.getLoader()
				.getPropertyAsInteger(ReconbenchmarkParamGen.class.getName() + ".LOCAL_HOT_COUNT", 1);
		WRITE_RATIO_IN_RW_TX = BenchProperties.getLoader()
				.getPropertyAsDouble(ReconbenchmarkParamGen.class.getName() + ".WRITE_RATIO_IN_RW_TX", 0.5);
		HOT_CONFLICT_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(ReconbenchmarkParamGen.class.getName() + ".HOT_CONFLICT_RATE", 0.001);
		
		DATA_SIZE = ReconbenchConstants.NUM_ITEMS;
		HOT_DATA_SIZE = (int) (1.0 / HOT_CONFLICT_RATE);
		COLD_DATA_SIZE = DATA_SIZE - HOT_DATA_SIZE;
		
		INDEX_UPDATE_COUNT = BenchProperties.getLoader()
				.getPropertyAsInteger(ReconbenchmarkParamGen.class.getName() + ".INDEX_UPDATE_COUNT", 2);
		
	}
	
	
	private Random random = new Random();

	public ReconbenchmarkParamGen() {
		
	}

	@Override
	public Object[] generateParameter() {
		TpccValueGenerator rvg = new TpccValueGenerator();
		ArrayList<Object> paramList = new ArrayList<Object>();

//		updateSessionUser(rvg);
		// System.out.println("sessionUser: "+sessionUser);
		
		// ================================
		// Decide the types of transactions
		// ================================
		
		boolean isReadWriteTx = (rvg.randomChooseFromDistribution(RW_TX_RATE, 1 - RW_TX_RATE) == 0) ? true : false;
		boolean isLongReadTx = (rvg.randomChooseFromDistribution(LONG_READ_TX_RATE, 1 - LONG_READ_TX_RATE) == 0) ? true : false;
		
		// =========================================
		// Decide the counts and the main partitions
		// =========================================

		// Set read counts
		int totalReadCount = TOTAL_READ_COUNT;
		int localHotCount = LOCAL_HOT_COUNT;
		
		// For long read tx
		// 10 times of total records and remote colds
		if (isLongReadTx) {
			totalReadCount *= 10;
		}

		// Local cold
		int localColdCount = totalReadCount - localHotCount;
		
		// Write Count
		int writeCount = 0;
		
		if (isReadWriteTx) {
			writeCount = (int) (totalReadCount * WRITE_RATIO_IN_RW_TX);
		}
		
		
		// =====================
		// Generating Parameters
		// =====================
		
		// Set read count
		paramList.add(totalReadCount);

		// Choose local hot records
		chooseHotData(paramList, localHotCount);

		// Choose local cold records
		chooseColdData(paramList, localColdCount);
		
		// Set write count
		paramList.add(writeCount);

		// Choose write records
		if (writeCount > 0) {
			// A read-write tx must at least update one hot record.
			paramList.add(paramList.get(1));
			
			// Choose some item randomly from the rest of items
			Object[] writeIds = randomlyChooseInParams(paramList, 2,
					totalReadCount + 1, writeCount - 1);
			for (Object id : writeIds)
				paramList.add(id);

			// set the update value
			for (int i = 0; i < writeCount; i++)
				paramList.add(rvg.nextDouble() * 100000);
		}

		return paramList.toArray(new Object[0]);
	}
	
	private Object[] randomlyChooseInParams(List<Object> params, int startIdx, int endIdx, int count) {
		// Create a clone
		Object[] tmps = new Object[endIdx - startIdx];
		for (int i = 0; i < tmps.length; i++)
			tmps[i] = params.get(startIdx + i);

		// Swap
		for (int times = 0; times < tmps.length * RANDOM_SWAP_FACTOR; times++) {
			int pos = random.nextInt(tmps.length - 1);

			Object tmp = tmps[pos];
			tmps[pos] = tmps[pos + 1];
			tmps[pos + 1] = tmp;
		}

		// Retrieve the first {count} results
		Object[] results = new Integer[count];
		for (int i = 0; i < count; i++)
			results[i] = tmps[i];

		return results;
	}

	protected void chooseHotData(List<Object> paramList, int count) {
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(HOT_DATA_SIZE);
		for (int i = 0; i < count; i++) {
			int choosedId = rg.next(); // 1 ~ size
			paramList.add(choosedId);
		}

	}

	private void chooseColdData(List<Object> paramList, int count) {
		int minMainPartColdData = HOT_DATA_SIZE;
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(COLD_DATA_SIZE);
		for (int i = 0; i < count; i++) {
			int tmp = rg.next(); // 1 ~ size
			int choosenId = minMainPartColdData + tmp;
			paramList.add(choosenId);
		}
	}
	
	protected int getIndexUpdateCount() {
		return INDEX_UPDATE_COUNT;
	}
}
