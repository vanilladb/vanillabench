package org.vanilladb.bench.benchmarks.micro;

import org.vanilladb.bench.util.BenchProperties;

public class MicrobenchConstants {

public static final int NUM_ITEMS;
	
	static {
		NUM_ITEMS = BenchProperties.getLoader().getPropertyAsInteger(
				MicrobenchConstants.class.getName() + ".NUM_ITEMS", 100000);
	}

}
