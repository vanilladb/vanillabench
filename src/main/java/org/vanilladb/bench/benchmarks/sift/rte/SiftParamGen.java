package org.vanilladb.bench.benchmarks.sift.rte;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.vanilladb.bench.benchmarks.sift.SiftBenchConstants;
import org.vanilladb.bench.benchmarks.sift.SiftTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.util.RandomValueGenerator;
import org.vanilladb.core.sql.VectorConstant;

public class SiftParamGen implements TxParamGenerator<SiftTransactionType> {

    private static RandomValueGenerator randomGenerator = new RandomValueGenerator(42);

    private VectorConstant query;

    @Override
    public SiftTransactionType getTxnType() {
        return SiftTransactionType.ANN;
    }

    private VectorConstant getSingleVector(int line) {
        String vectorString;
        try (BufferedReader br = new BufferedReader(new FileReader(SiftBenchConstants.DATASET_FILE))) {
            for (int i = 0; i < line; i++) {
                br.readLine();
            }
            vectorString = br.readLine();
            VectorConstant randomNoise = VectorConstant.normal(SiftBenchConstants.NUM_DIMENSION, 0, 1);
            return (VectorConstant) (new VectorConstant(vectorString)).add(randomNoise);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(SiftBenchConstants.DATASET_FILE + " not found.");
        }
    }

    @Override
    public Object[] generateParameter() {
        ArrayList<Object> paramList = new ArrayList<>();

        // =====================
		// Generating Parameters
		// =====================
        paramList.add(SiftBenchConstants.NUM_DIMENSION);

        // Generate a query vector
        query = getSingleVector(randomGenerator.number(0, 1000000 - 1));
        
        for (int i = 0; i < SiftBenchConstants.NUM_DIMENSION; i++) {
            paramList.add(query.get(i));
        }

        return paramList.toArray();
    }
    
    public VectorConstant getQuery() {
        return query;
    }
}
