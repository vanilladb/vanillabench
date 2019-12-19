package org.vanilladb.bench.benchmarks.ycsb;

public class YcsbConstants {

	public static final int NUM_ITEMS = 1000000;
	public static final int FIELD_COUNT = 10; 			// including primary key
	public static final int CHARS_PER_FIELD = 33; 		// each char 3 bytes
	public static final String ID_FORMAT = "%0" + YcsbConstants.CHARS_PER_FIELD + "d";
	
}
