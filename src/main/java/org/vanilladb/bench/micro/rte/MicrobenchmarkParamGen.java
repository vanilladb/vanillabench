package org.vanilladb.bench.micro.rte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.vanilladb.bench.micro.MicroTransactionType;
import org.vanilladb.bench.micro.MicrobenchConstants;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.tpcc.TpccValueGenerator;
import org.vanilladb.bench.util.BenchProperties;
import org.vanilladb.bench.util.RandomNonRepeatGenerator;

public class MicrobenchmarkParamGen implements TxParamGenerator<MicroTransactionType> {
	
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
	
	// Other parameters
	private static final int RANDOM_SWAP_FACTOR = 20;

	static {
		RW_TX_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(MicrobenchmarkParamGen.class.getName() + ".RW_TX_RATE", 0.0);
		LONG_READ_TX_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(MicrobenchmarkParamGen.class.getName() + ".LONG_READ_TX_RATE", 0.0);
		
		TOTAL_READ_COUNT = BenchProperties.getLoader()
				.getPropertyAsInteger(MicrobenchmarkParamGen.class.getName() + ".TOTAL_READ_COUNT", 10);
		LOCAL_HOT_COUNT = BenchProperties.getLoader()
				.getPropertyAsInteger(MicrobenchmarkParamGen.class.getName() + ".LOCAL_HOT_COUNT", 1);
		
		WRITE_RATIO_IN_RW_TX = BenchProperties.getLoader()
				.getPropertyAsDouble(MicrobenchmarkParamGen.class.getName() + ".WRITE_RATIO_IN_RW_TX", 0.5);
		HOT_CONFLICT_RATE = BenchProperties.getLoader()
				.getPropertyAsDouble(MicrobenchmarkParamGen.class.getName() + ".HOT_CONFLICT_RATE", 0.001);
		
		DATA_SIZE = MicrobenchConstants.NUM_ITEMS;
		HOT_DATA_SIZE = (int) (1.0 / HOT_CONFLICT_RATE);
		COLD_DATA_SIZE = DATA_SIZE - HOT_DATA_SIZE;
	}
	
	
	private Random random = new Random();

	public MicrobenchmarkParamGen() {
		
	}

	@Override
	public MicroTransactionType getTxnType() {
		return MicroTransactionType.MICRO;
	}

	// a main application for debugging
	public static void main(String[] args) {
		MicrobenchmarkParamGen executor = new MicrobenchmarkParamGen();
		
		System.out.println("Parameters:");
		System.out.println("Read Write Tx Rate: " + RW_TX_RATE);
		System.out.println("Long Read Tx Rate: " + LONG_READ_TX_RATE);
		System.out.println("Total Read Count: " + TOTAL_READ_COUNT);
		System.out.println("Local Hot Count: " + LOCAL_HOT_COUNT);
		System.out.println("Write Ratio in RW Tx: " + WRITE_RATIO_IN_RW_TX);
		System.out.println("Hot Conflict Rate: " + HOT_CONFLICT_RATE);
		
		System.out.println("# of items: " + DATA_SIZE);
		System.out.println("# of hot items: " + HOT_DATA_SIZE);
		System.out.println("# of cold items: " + COLD_DATA_SIZE);

		System.out.println();

		for (int i = 0; i < 1000; i++) {
			Object[] params = executor.generateParameter();
			System.out.println(Arrays.toString(params));
		}
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

	private void chooseHotData(List<Object> paramList, int count) {
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(HOT_DATA_SIZE);
		for (int i = 0; i < count; i++) {
			int itemId = rg.next(); // 1 ~ size
			paramList.add(itemId);
		}

	}

	private void chooseColdData(List<Object> paramList, int count) {
		int minMainPartColdData = HOT_DATA_SIZE;
		RandomNonRepeatGenerator rg = new RandomNonRepeatGenerator(COLD_DATA_SIZE);
		for (int i = 0; i < count; i++) {
			int tmp = rg.next(); // 1 ~ size
			int itemId = minMainPartColdData + tmp;
			paramList.add(itemId);
		}
	}
}
