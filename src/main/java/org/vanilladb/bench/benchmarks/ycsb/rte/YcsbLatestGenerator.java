package org.vanilladb.bench.benchmarks.ycsb.rte;

public class YcsbLatestGenerator {

	private final YcsbZipfianGenerator zipfian;
	private int recordCount;

	public YcsbLatestGenerator(int recordCount, double skewParameter) {
		this.recordCount = recordCount;
		zipfian = new YcsbZipfianGenerator(1, recordCount, skewParameter);
		nextValue();
	}
	
	public YcsbLatestGenerator(YcsbLatestGenerator origin) {
		this.zipfian = new YcsbZipfianGenerator(origin.zipfian);
		this.recordCount = origin.recordCount;
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
		int dataSize = 10_000_000;
		int sampleTimes = dataSize;
		YcsbLatestGenerator gen = new YcsbLatestGenerator(dataSize, 0.9999);

		// Create bounds
		long[] bounds = new long[(int) Math.log10(dataSize) + 2];
		int[] counts = new int[(int) Math.log10(dataSize) + 1];
		long level = 1;
		bounds[0] = dataSize;
		for (int i = 1; i < bounds.length; i++) {
			bounds[i] = dataSize - level;
			level *= 10;
		}
		
		// Sample from the distribution
		for (int i = 0; i < sampleTimes; i++) {
			long sample = gen.nextValue();
			int index = 0;
			while (bounds[index] != 0) {
				if (sample <= bounds[index] && sample > bounds[index + 1]) {
					counts[index]++;
					break;
				} else
					index++;
			}
		}
		
		// Print results
		for (int i = 0; i < bounds.length - 1; i++) {
			System.out.println(String.format("%d ~ %d\t%d", bounds[i], bounds[i + 1], counts[i]));
		}
	}
}
