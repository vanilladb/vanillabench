package org.vanilladb.bench.benchmarks.ann.rte;

import java.util.ArrayList;

import org.vanilladb.bench.benchmarks.ann.AnnBenchConstants;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.core.sql.VectorConstant;

public class AnnParamGen implements TxParamGenerator<AnnTransactionType>{

    @Override
    public AnnTransactionType getTxnType() {
        return AnnTransactionType.ANN;
    }

    @Override
    public Object[] generateParameter() {
        ArrayList<Object> paramList = new ArrayList<>();

        // =====================
		// Generating Parameters
		// =====================
        paramList.add(AnnBenchConstants.NUM_DIMENSION);

        // Generate a query vector
        VectorConstant query = new VectorConstant(AnnBenchConstants.NUM_DIMENSION);
        
        for (int i = 0; i < AnnBenchConstants.NUM_DIMENSION; i++) {
            paramList.add(query.get(i));
        }

        return paramList.toArray();
    }
    
}
