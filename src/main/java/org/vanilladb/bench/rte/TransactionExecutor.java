package org.vanilladb.bench.rte;

import org.vanilladb.bench.TxnResultSet;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.util.BenchProperties;

public abstract class TransactionExecutor {

	public static final boolean DISPLAY_RESULT;

	static {
		DISPLAY_RESULT = BenchProperties.getLoader()
				.getPropertyAsBoolean(TransactionExecutor.class.getName() + ".DISPLAY_RESULT", false);
	}

	protected TxParamGenerator pg;

	public abstract TxnResultSet execute(SutConnection conn);

	protected SutResultSet callStoredProc(SutConnection spc, Object[] pars) {
		try {
			SutResultSet result = spc.callStoredProc(pg.getTxnType().getProcedureId(), pars);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
