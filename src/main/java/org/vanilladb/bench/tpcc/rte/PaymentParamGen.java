package org.vanilladb.bench.tpcc.rte;

import org.vanilladb.bench.tpcc.TpccConstants;
import org.vanilladb.bench.tpcc.TpccTransactionType;
import org.vanilladb.bench.tpcc.TpccValueGenerator;

public class PaymentParamGen implements TpccTxParamGenerator {

	private int homeWid;
	private TpccValueGenerator valueGen = new TpccValueGenerator();

	public PaymentParamGen(int homeWarehouseId) {
		this.homeWid = homeWarehouseId;
	}

	@Override
	public TpccTransactionType getTxnType() {
		return TpccTransactionType.PAYMENT;
	}

	@Override
	public long getKeyingTime() {
		return TpccConstants.KEYING_PAYMENT * 1000;
	}

	@Override
	public Object[] generateParameter() {
		// pars = {wid, did, cwid, cdid, cid/clast, hAmount}
		Object[] pars = new Object[6];
		pars[0] = homeWid;
		pars[1] = valueGen.number(1, 10);
		/*
		 * Customer resident warehouse is the home warehouse 85% of the time and
		 * is a randomly selected remote warehouse 15% of the time.
		 */
		if (valueGen.rng().nextDouble() >= 0.85 && TpccConstants.NUM_WAREHOUSES > 1) {
			pars[2] = valueGen.numberExcluding(1, TpccConstants.NUM_WAREHOUSES, homeWid);
			pars[3] = valueGen.number(1, 10);
		} else {
			pars[2] = homeWid;
			pars[3] = pars[1];
		}

		/*
		 * The customer is randomly selected 60% of the time by last name and
		 * 40% of time by id.
		 */
		// XXX: ElaSQL doesn't support selecting by the last name
//		if (rg.rng().nextDouble() >= 0.60)
//			pars[4] = rg.makeRandomLastName(false);
//		else
			pars[4] = valueGen.NURand(TpccValueGenerator.NU_CID, 1,
					TpccConstants.CUSTOMERS_PER_DISTRICT);
		pars[5] = valueGen.fixedDecimalNumber(2, 1.00, 5000.00);
		return pars;
	}

	@Override
	public long getThinkTime() {
		double r = valueGen.rng().nextDouble();
		return (long) -Math.log(r) * TpccConstants.THINKTIME_PAYMENT * 1000;
	}

}
