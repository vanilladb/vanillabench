package org.vanilladb.bench.tpce.rte;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.tpce.TpceConstants;
import org.vanilladb.bench.tpce.TpceTransactionType;
import org.vanilladb.bench.tpce.data.TpceDataManager;
import org.vanilladb.bench.tpce.data.Trade;
import org.vanilladb.bench.util.RandomValueGenerator;

public class TpceRte extends RemoteTerminalEmulator<TpceTransactionType> {
	
	private static final RandomValueGenerator TX_TYPE_RANDOM = new RandomValueGenerator();
	
	private TpceDataManager dataMgr;
	
	public TpceRte(SutConnection conn, StatisticMgr statMgr, TpceDataManager dataMgr) {
		super(conn, statMgr);
		this.dataMgr = dataMgr;
	}

	@Override
	protected TpceTransactionType getNextTxType() {
		boolean isTradeOrder = TX_TYPE_RANDOM.randomChooseFromDistribution(
				TpceConstants.TARDE_ORDER_PERCENTAGE, 1 - TpceConstants.TARDE_ORDER_PERCENTAGE)
				== 0 ? true : false;
		
		if (isTradeOrder)
			return TpceTransactionType.TRADE_ORDER;
		else
			return TpceTransactionType.TRADE_RESULT;
	}

	@Override
	protected TpceTxExecutor getTxExeutor(TpceTransactionType type) {
		if (type == TpceTransactionType.TRADE_ORDER) {
			return new TpceTxExecutor(new TradeOrderParamGen(dataMgr));
		} else if (type == TpceTransactionType.TRADE_RESULT) {
			Trade trade = dataMgr.getOldestTrade();
			if (trade != null)
				return new TpceTxExecutor(new TradeResultParamGen(trade));
			else
				return new TpceTxExecutor(new TradeOrderParamGen(dataMgr));
		} else {
			throw new UnsupportedOperationException("Transaction executor for " + type 
					+ " is not implemented.");
		}
	}

}
