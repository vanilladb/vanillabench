package org.vanilladb.bench.benchmarks.ann.rte;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.VanillaBenchParameters;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.sp.VanillaDbSpResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.Record;
import org.vanilladb.core.sql.Schema;

public class AnnTxExecutor extends TransactionExecutor<AnnTransactionType>{

    private Map<VectorConstant, Set<Integer>> resultMap;

    public AnnTxExecutor(TxParamGenerator<AnnTransactionType> pg, Map<VectorConstant, Set<Integer>> resultMap) {
        this.pg = pg;
        this.resultMap = resultMap;
    }

    @Override
    public TxnResultSet execute(SutConnection conn) {
        try {
            // generate parameters
            Object[] params = pg.generateParameter();
            
            VectorConstant query = ((AnnParamGen) pg).getQuery();

            // send txn request and start measuring txn response time
            long txnRT = System.nanoTime();

            VanillaDbSpResultSet result = (VanillaDbSpResultSet) executeTxn(conn, params);

            // measure response time
            long txnEndTime = System.nanoTime();
            txnRT = txnEndTime - txnRT;

            // display output
            if (VanillaBenchParameters.SHOW_TXN_RESPONSE_ON_CONSOLE)
                System.out.println(pg.getTxnType() + " " + result.outputMsg());

            Set<Integer> approximateNeighbors = new HashSet<>();
            Schema sch = result.getSchema();
            Record rec = result.getRecords()[0];

            for (String fld : sch.fields()) {
                if (fld.equals("rc")) {
                    // For record count
                    continue;
                }
                approximateNeighbors.add((Integer) rec.getVal(fld).asJavaVal());
            }

            if (resultMap != null)
                resultMap.put(query, approximateNeighbors);

            return new TxnResultSet(pg.getTxnType(), txnRT, txnEndTime, result.isCommitted(), result.outputMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());

        }
    }

    @Override
    protected JdbcExecutor<AnnTransactionType> getJdbcExecutor() {
        throw new UnsupportedOperationException("no JDBC implementation for ANN");
    }
    
    
}
