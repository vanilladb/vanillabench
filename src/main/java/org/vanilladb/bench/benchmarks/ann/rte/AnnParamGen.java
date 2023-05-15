package org.vanilladb.bench.benchmarks.ann.rte;

import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;

public class AnnParamGen implements TxParamGenerator<AnnTransactionType>{

    @Override
    public AnnTransactionType getTxnType() {
        return AnnTransactionType.ANN;
    }

    @Override
    public Object[] generateParameter() {
        // TODO: Generate vector queries
        return null;
    }
    
}
