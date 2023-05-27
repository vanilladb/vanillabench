package org.vanilladb.bench.benchmarks.ann;

import org.vanilladb.bench.BenchTransactionType;

public enum AnnTransactionType implements BenchTransactionType {
    // Loading procedures
    TESTBED_LOADER(false),
    // Database checking procedures
    CHECK_DATABASE(false),
    // Benchmark procedures
    ANN(true),
    
    CALCULATE_RECALL(false);

    public static AnnTransactionType fromProcedureId(int pid) {
        return AnnTransactionType.values()[pid];
    }

    private boolean isBenchProc;

    AnnTransactionType(boolean isBenchProc) {
        this.isBenchProc = isBenchProc;
    }

    @Override
    public int getProcedureId() {
        return this.ordinal();
    }

    @Override
    public boolean isBenchmarkingProcedure() {
        return this.isBenchProc;
    }
    
}
