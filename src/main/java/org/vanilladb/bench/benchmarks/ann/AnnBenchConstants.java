package org.vanilladb.bench.benchmarks.ann;

import org.vanilladb.bench.util.BenchProperties;

public class AnnBenchConstants {

    public static final int NUM_ITEMS;
    public static final int NUM_DIMENSION;

    static {
        NUM_ITEMS = BenchProperties.getLoader().getPropertyAsInteger(
                AnnBenchConstants.class.getName() + ".NUM_ITEMS", 100000);
        NUM_DIMENSION = BenchProperties.getLoader().getPropertyAsInteger(
                AnnBenchConstants.class.getName() + ".NUM_DIMENSIONS", 8);
    }
    
}
