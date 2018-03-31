package org.vanilladb.bench.benchmarks.tpcc.rte;

import org.vanilladb.bench.benchmarks.tpcc.TpccConstants;
import org.vanilladb.bench.benchmarks.tpcc.TpccTransactionType;
import org.vanilladb.bench.benchmarks.tpcc.TpccValueGenerator;

public class OrderStatusParamGen implements TpccTxParamGenerator {

	private int homeWid;
	private TpccValueGenerator valueGen = new TpccValueGenerator();

	public OrderStatusParamGen(int homeWarehouseId) {
		this.homeWid = homeWarehouseId;
	}

	@Override
	public TpccTransactionType getTxnType() {
		return TpccTransactionType.ORDER_STATUS;
	}

	@Override
	public long getKeyingTime() {
		return TpccConstants.KEYING_ORDER_STATUS * 1000;
	}

	@Override
	public Object[] generateParameter() {
		// pars = {cwid, cdid, cid/clast}
		Object[] pars = new Object[3];
		pars[0] = homeWid;
		pars[1] = valueGen.number(1, 10);
		/*
		 * The customer is randomly selected 60% of the time by last name and
		 * 40% of time by id.
		 */
		// if (rg.rng().nextDouble() >= 0.60)
		// pars[2] = rg.makeRandomLastName(false);
		// else
		pars[2] = valueGen.NURand(TpccValueGenerator.NU_CID, 1,
				TpccConstants.CUSTOMERS_PER_DISTRICT);
		return pars;
	}

	@Override
	public long getThinkTime() {
		double r = valueGen.rng().nextDouble();
		return (long) -Math.log(r) * TpccConstants.THINKTIME_ORDER_STATUS
				* 1000;
	}

}
