/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.vanilladb.bench.benchmarks.tpce.rte;

import org.vanilladb.bench.benchmarks.tpce.TpceTransactionType;
import org.vanilladb.bench.benchmarks.tpce.data.Customer;
import org.vanilladb.bench.benchmarks.tpce.data.CustomerAccount;
import org.vanilladb.bench.benchmarks.tpce.data.TpceDataManager;
import org.vanilladb.bench.remote.SutResultSet;

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
	public TpceTransactionType getTxnType() {
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
	public void onResponseReceived(SutResultSet result) {
		if (result.isCommitted())
			dataMgr.addNewTrade(tradeId, customerId, customerAccountId, brokerId);
	}

}
