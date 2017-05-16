package org.vanilladb.bench.rte.tpcc;

import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.tpcc.TpccConstants;
import org.vanilladb.bench.tpcc.TpccTransactionType;
import org.vanilladb.bench.tpcc.TpccValueGenerator;

public class DeliveryParamGen implements TpccTxParamGenerator {

	private int homeWid;
	private TpccValueGenerator valueGen = new TpccValueGenerator();

	public DeliveryParamGen(int homeWarehouseId) {
		this.homeWid = homeWarehouseId;
	}

	@Override
	public TransactionType getTxnType() {
		return TpccTransactionType.DELIVERY;
	}

	@Override
	public Object[] generateParameter() {
		Object[] pars = new Object[2];
		pars[0] = homeWid;
		pars[1] = valueGen.number(1, 10);
		return pars;
	}

	@Override
	public long getKeyingTime() {
		return TpccConstants.KEYING_DELIVERY * 1000;
	}

	@Override
	public long getThinkTime() {
		double r = valueGen.rng().nextDouble();
		return (long) -Math.log(r) * TpccConstants.THINKTIME_DELIVERY * 1000;
	}

}
