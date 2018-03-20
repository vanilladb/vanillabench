package org.vanilladb.bench.tpcc.rte;

import org.vanilladb.bench.tpcc.TpccConstants;
import org.vanilladb.bench.tpcc.TpccTransactionType;
import org.vanilladb.bench.tpcc.TpccValueGenerator;

public class StockLevelParamGen implements TpccTxParamGenerator {

	private int homeWid;
	private TpccValueGenerator valueGen = new TpccValueGenerator();

	public StockLevelParamGen(int homeWarehouseId) {
		this.homeWid = homeWarehouseId;
	}

	@Override
	public TpccTransactionType getTxnType() {
		return TpccTransactionType.STOCK_LEVEL;
	}

	@Override
	public Object[] generateParameter() {
		// pars = {wid, did, threshold}
		Object[] pars = new Object[3];
		pars[0] = homeWid;
		pars[1] = valueGen.number(1, 10);
		pars[2] = valueGen.number(10, 20);
		return pars;
	}

	@Override
	public long getKeyingTime() {
		return TpccConstants.KEYING_STOCK_LEVEL * 1000;
	}

	@Override
	public long getThinkTime() {
		double r = valueGen.rng().nextDouble();
		return (long) -Math.log(r) * TpccConstants.THINKTIME_STOCK_LEVEL * 1000;
	}

}
