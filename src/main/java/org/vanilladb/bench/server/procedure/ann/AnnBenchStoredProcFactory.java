package org.vanilladb.bench.server.procedure.ann;

import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureFactory;

public class AnnBenchStoredProcFactory implements StoredProcedureFactory {
    
    @Override
    public StoredProcedure<?> getStoredProcedure(int pid) {
        StoredProcedure<?> sp;
        switch (AnnTransactionType.fromProcedureId(pid)) {
            case TESTBED_LOADER:
                sp = new AnnTestbedLoaderProc();
                break;
            case CHECK_DATABASE:
                sp = new AnnCheckDatabaseProc();
                break;
            case ANN:
                sp = new AnnSearchProc();
                break;
            case CALCULATE_RECALL:
                sp = new AnnCalculateRecallProc();
                break;
            default:
                throw new UnsupportedOperationException("The benchmarker does not recognize procedure " + pid + "");
        }
        return sp;
    }
}
