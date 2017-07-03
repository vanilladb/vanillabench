package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.param.tpce.TradeResultParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.server.VanillaDb;

public class TradeResultProc extends BasicStoredProcedure<TradeResultParamHelper> {
	
	public TradeResultProc() {
		super(new TradeResultParamHelper());
	}

	@Override
	protected void executeSql() {
		// TODO: Implement it
	}
	
	private Scan executeQuery(String sql) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		Scan s = p.open();
		s.beforeFirst();
		if (s.next()) {
			return s;
		} else
			throw new RuntimeException("Query: " + sql + " fails.");
	}
	
	private void executeUpdate(String sql) {
		int count = VanillaDb.newPlanner().executeUpdate(sql, tx);
		
		if (count > 1)
			throw new RuntimeException("Update: " + sql + " affect more than 1 record.");
		else if (count < 1)
			throw new RuntimeException("Update: " + sql + " fails.");
	}
}
