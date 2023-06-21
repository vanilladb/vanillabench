package org.vanilladb.bench.benchmarks.ann;

import org.vanilladb.bench.util.BenchProperties;

public class AnnBenchConstants {

    public static final int NUM_ITEMS;
    public static final int NUM_DIMENSION;
    public static final String DATASET_FILE;

    static {
        NUM_ITEMS = BenchProperties.getLoader().getPropertyAsInteger(
                AnnBenchConstants.class.getName() + ".NUM_ITEMS", 900000);
        NUM_DIMENSION = BenchProperties.getLoader().getPropertyAsInteger(
                AnnBenchConstants.class.getName() + ".NUM_DIMENSIONS", 128);
        DATASET_FILE = BenchProperties.getLoader().getPropertyAsString(
            AnnBenchConstants.class.getName() + ".DATASET_FILE", "sift.txt");
    }
    
}
