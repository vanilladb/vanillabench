package org.vanilladb.bench.benchmarks.ycsb;

import org.vanilladb.bench.util.BenchProperties;

public class YcsbConstants {

	public static final int NUM_ITEMS;
	public static final int FIELD_COUNT = 10; 			// including primary key
	public static final int CHARS_PER_FIELD = 33; 		// each char 3 bytes
	public static final String ID_FORMAT = "%0" + YcsbConstants.CHARS_PER_FIELD + "d";
	
	static {
		NUM_ITEMS = BenchProperties.getLoader().getPropertyAsInteger(
				YcsbConstants.class.getName() + ".NUM_ITEMS", 100000);
	}
	
}
