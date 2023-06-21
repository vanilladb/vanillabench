package org.vanilladb.bench.benchmarks.sift;

import org.vanilladb.bench.BenchTransactionType;

public enum SiftTransactionType implements BenchTransactionType{
    // Loading procedures
    TESTBED_LOADER(false),
    CHECK_DATABASE(false),
    ANN(true), INSERT(true),
    CALCULATE_RECALL(false);

    public static SiftTransactionType fromProcedureId(int pid) {
        return SiftTransactionType.values()[pid];
    }

    SiftTransactionType(boolean isBenchProc) {
        this.isBenchProc = isBenchProc;
    }

    private boolean isBenchProc;

    @Override
    public int getProcedureId() {
        return this.ordinal();
    }

    @Override
    public boolean isBenchmarkingProcedure() {
        return this.isBenchProc;
    }
    
}
