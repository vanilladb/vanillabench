package org.vanilladb.bench.benchmarks.sift;

import org.vanilladb.bench.util.BenchProperties;

public class SiftBenchConstants {

    public static final int NUM_ITEMS;
    public static final int NUM_DIMENSION;
    public static final String DATASET_FILE;
    public static final double READ_INSERT_TX_RATE;

    static {
        NUM_ITEMS = BenchProperties.getLoader().getPropertyAsInteger(
                SiftBenchConstants.class.getName() + ".NUM_ITEMS", 10);
        NUM_DIMENSION = BenchProperties.getLoader().getPropertyAsInteger(
                SiftBenchConstants.class.getName() + ".NUM_DIMENSIONS", 128);
        DATASET_FILE = BenchProperties.getLoader().getPropertyAsString(
                SiftBenchConstants.class.getName() + ".DATASET_FILE", "sift.txt");
        READ_INSERT_TX_RATE = BenchProperties.getLoader().getPropertyAsDouble(
                SiftBenchConstants.class.getName() + ".READ_INSERT_TX_RATE", 0.7);
        
    }
    
}
