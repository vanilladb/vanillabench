package org.vanilladb.bench.rte.tpcc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.rte.TransactionExecutor;
import org.vanilladb.bench.tpcc.TpccConstants;
import org.vanilladb.bench.tpcc.TpccTransactionType;

public class TpccRte extends RemoteTerminalEmulator {
	
	private int homeWid;
	private static Random txnTypeRandom;
	private Map<TransactionType, TpccTxExecutor> executors;

	public TpccRte(SutConnection conn, StatisticMgr statMgr, int homeWarehouseId) {
		super(conn, statMgr);
		homeWid = homeWarehouseId;
		txnTypeRandom = new Random();
		executors = new HashMap<TransactionType, TpccTxExecutor>();
		executors.put(TpccTransactionType.NEW_ORDER, new TpccTxExecutor(new NewOrderParamGen(homeWid)));
		executors.put(TpccTransactionType.PAYMENT, new TpccTxExecutor(new PaymentParamGen(homeWid)));
		executors.put(TpccTransactionType.ORDER_STATUS, new TpccTxExecutor(new OrderStatusParamGen(homeWid)));
		executors.put(TpccTransactionType.DELIVERY, new TpccTxExecutor(new DeliveryParamGen(homeWid)));
		executors.put(TpccTransactionType.STOCK_LEVEL, new TpccTxExecutor(new StockLevelParamGen(homeWid)));
	}
	
	protected TransactionType getNextTxType() {
		int index = txnTypeRandom.nextInt(TpccConstants.FREQUENCY_TOTAL);
		if (index < TpccConstants.RANGE_NEW_ORDER)
			return TpccTransactionType.NEW_ORDER;
		else if (index < TpccConstants.RANGE_PAYMENT)
			return TpccTransactionType.PAYMENT;
		else if (index < TpccConstants.RANGE_ORDER_STATUS)
			return TpccTransactionType.ORDER_STATUS;
		else if (index < TpccConstants.RANGE_DELIVERY)
			return TpccTransactionType.DELIVERY;
		else
			return TpccTransactionType.STOCK_LEVEL;
	}
	
	protected TransactionExecutor getTxExeutor(TransactionType type) {
		return executors.get(type);
	}
}
