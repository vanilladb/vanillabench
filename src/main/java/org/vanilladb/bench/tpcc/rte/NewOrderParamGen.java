package org.vanilladb.bench.tpcc.rte;

import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.tpcc.TpccConstants;
import org.vanilladb.bench.tpcc.TpccTransactionType;
import org.vanilladb.bench.tpcc.TpccValueGenerator;

public class NewOrderParamGen implements TpccTxParamGenerator {

	private int homeWid;
	private TpccValueGenerator valueGen = new TpccValueGenerator();

	public NewOrderParamGen(int homeWarehouseId) {
		homeWid = homeWarehouseId;
	}

	@Override
	public TransactionType getTxnType() {
		return TpccTransactionType.NEW_ORDER;
	}

	@Override
	public long getKeyingTime() {
		return TpccConstants.KEYING_NEW_ORDER * 1000;
	}

	@Override
	public Object[] generateParameter() {
		/*
		 * The return value of RandomChooseFromDistribution Method start from 1.
		 */
		// if (RemoteTerminalEmulator.IS_BATCH_REQUEST)
		// wid = rg.randomChooseFromDistribution(WAREHOUSE_DISTRIBUTION);

		boolean allLocal = true;
		// pars = {wid, did, cid, olCount, items[15][3], allLocal}

		Object[] pars = new Object[50];
		pars[0] = homeWid;
		pars[1] = valueGen.number(1, 10);
		pars[2] = valueGen.NURand(TpccValueGenerator.NU_CID, 1, TpccConstants.CUSTOMERS_PER_DISTRICT);
		int olCount = valueGen.number(5, 15);
		pars[3] = olCount;

		for (int i = 0; i < olCount; i++) {
			int j = 4 + i * 3;
			/*
			 * ol_i_id. 1% of the New-Order txs are chosen at random to simulate
			 * user data entry errors
			 */
			// if (rg.rng().nextDouble() < 0.01)
			// pars[j] = TpccConstants.NUM_ITEMS + 15; // choose unused item id
			// else
			pars[j] = valueGen.NURand(TpccValueGenerator.NU_OLIID, 1, TpccConstants.NUM_ITEMS);

			// TODO: Verify this
			// ol_supply_w_id. 1% of items are supplied by remote warehouse
			if (valueGen.rng().nextDouble() < 0.05 && TpccConstants.NUM_WAREHOUSES > 1) {
				pars[++j] = valueGen.numberExcluding(1, TpccConstants.NUM_WAREHOUSES, homeWid);
				allLocal = false;
			} else
				pars[++j] = homeWid;

			// ol_quantity
			pars[++j] = valueGen.number(1, 10);
		}
		pars[49] = allLocal;

		return pars;
	}

	@Override
	public long getThinkTime() {
		double r = valueGen.rng().nextDouble();
		return (long) -Math.log(r) * TpccConstants.THINKTIME_NEW_ORDER * 1000;
	}

}
