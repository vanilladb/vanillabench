package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.param.tpce.TradeResultParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;

public class TradeResultProc extends BasicStoredProcedure<TradeResultParamHelper> {
	
	public TradeResultProc() {
		super(new TradeResultParamHelper());
	}

	@Override
	protected void executeSql() {
		// TODO: Implement it
	}
}
