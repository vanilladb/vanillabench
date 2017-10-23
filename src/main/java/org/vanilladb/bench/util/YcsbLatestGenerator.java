package org.vanilladb.bench.util;

import org.vanilladb.bench.ycsb.YcsbConstants;

public class YcsbLatestGenerator {

	private final YcsbZipfianGenerator zipfian;
	private int recordCount;

	public YcsbLatestGenerator(int recordCount, double skewParameter) {
		this.recordCount = recordCount;
		zipfian = new YcsbZipfianGenerator(1, recordCount, skewParameter);
		nextValue();
	}

	/**
	 * Generate the next string in the distribution, skewed Zipfian favoring the
	 * items most recently returned by the basis generator.
	 */
	public long nextValue() {
		long next = recordCount - zipfian.nextLong(recordCount) + 1;
		return next;
	}
	
	public static void main(String[] args) {
		YcsbLatestGenerator gen = new YcsbLatestGenerator(YcsbConstants.NUM_ITEMS, 0.9);
		
		int numOfTimes = 100;
		int countPerSeg = YcsbConstants.NUM_ITEMS / 100;
		int[] times = new int[numOfTimes];
		
		for (int i = 0; i < YcsbConstants.NUM_ITEMS; i++) {
			int seg = (int) ((gen.nextValue() - 1) / countPerSeg);
			times[seg]++;
		}
		
		for (int i = 0; i < times.length; i++)
			System.out.println((i * countPerSeg) + "\t" + times[i]);
	}
}
