package org.vanilladb.bench.tpce.rte;

import org.vanilladb.bench.TransactionType;
import org.vanilladb.bench.tpce.TpceTransactionType;
import org.vanilladb.bench.tpce.data.Customer;
import org.vanilladb.bench.tpce.data.CustomerAccount;
import org.vanilladb.bench.tpce.data.TpceDataManager;

public class TradeOrderParamGen implements TpceTxParamGenerator {

	private TpceDataManager dataMgr;
	
	private long tradeId;
	private long customerId;
	private long customerAccountId;
	private long brokerId;
	
	public TradeOrderParamGen(TpceDataManager dataMgr) {
		this.dataMgr = dataMgr;
	}
	
	@Override
	public TransactionType getTxnType() {
		return TpceTransactionType.TRADE_ORDER;
	}

	@Override
	public Object[] generateParameter() {
		Object[] params = new Object[10];
		int idx = 0;
		
		// Non-uniformly random a customer
		Customer customer = dataMgr.getNonUniformRandomCustomer();
		CustomerAccount account = customer.getRandomAccount();
		
		// Generate the key of a trade
		tradeId = dataMgr.getNextTradeId();
		customerId = customer.getCustomerId();
		customerAccountId = account.getAccountId();
		brokerId = account.getBrokerId();

		// Assemble the parameters
		params[idx++] = customerAccountId;
		params[idx++] = customerId;
		params[idx++] = brokerId;
		params[idx++] = dataMgr.getRandomCompanyName();
		params[idx++] = dataMgr.getRandomRequestPrice();
		params[idx++] = dataMgr.getRandomRollback();
		params[idx++] = dataMgr.getRandomSymbol();
		params[idx++] = dataMgr.getRandomTradeQuantity();
		params[idx++] = dataMgr.getRandomTradeType();
		params[idx++] = tradeId;

		return params;
	}

	@Override
	public void onResponseReceived() {
		dataMgr.addNewTrade(tradeId, customerId, customerAccountId, brokerId);
	}

}
