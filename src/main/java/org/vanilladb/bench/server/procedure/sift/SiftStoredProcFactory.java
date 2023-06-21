package org.vanilladb.bench.server.procedure.sift;

import org.vanilladb.bench.benchmarks.sift.SiftTransactionType;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class SiftStoredProcFactory implements StoredProcedureFactory {

    @Override
    public StoredProcedure<?> getStoredProcedure(int pid) {
        StoredProcedure<?> sp;
        switch (SiftTransactionType.fromProcedureId(pid)) {
            case TESTBED_LOADER:
                sp = new SiftTestbedLoaderProc();
                break;
            case CHECK_DATABASE:
                sp = new SiftCheckDatabaseProc();
                break;
            case ANN:
                sp = new SiftBenchProc();
                break;
            case INSERT:
                sp = new SiftInsertProc();
                break;
            case CALCULATE_RECALL:
                sp = new SiftCalculateRecallProc();
                break;
            default:
                throw new UnsupportedOperationException("Benchmarker does not recognize procedure " + pid);
        }
        return sp;
    }
    
}
