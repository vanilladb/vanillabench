package org.vanilladb.bench.benchmarks.ann.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.VanillaBenchParameters;
import org.vanilladb.bench.benchmarks.ann.AnnTransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.rte.TxParamGenerator;
import org.vanilladb.bench.rte.jdbc.JdbcExecutor;

public class AnnTxExecutor extends TransactionExecutor<AnnTransactionType>{

    public AnnTxExecutor(TxParamGenerator<AnnTransactionType> pg) {
        this.pg = pg;
    }

    @Override
    public TxnResultSet execute(SutConnection conn) {
        try {
            // generate parameters
            Object[] params = pg.generateParameter();

            // send txn request and start measuring txn response time
            long txnRT = System.nanoTime();

            SutResultSet result = executeTxn(conn, params);

            // measure response time
            long txnEndTime = System.nanoTime();
            txnRT = txnEndTime - txnRT;

            // display output
            if (VanillaBenchParameters.SHOW_TXN_RESPONSE_ON_CONSOLE)
                System.out.println(pg.getTxnType() + " " + result.outputMsg());
            
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
