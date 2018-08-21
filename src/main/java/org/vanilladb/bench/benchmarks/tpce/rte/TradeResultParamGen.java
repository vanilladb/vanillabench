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
import org.vanilladb.bench.benchmarks.tpce.data.Trade;
import org.vanilladb.bench.remote.SutResultSet;

public class TradeResultParamGen implements TpceTxParamGenerator {
	
	private Trade trade;
	
	public TradeResultParamGen(Trade trade) {
		this.trade = trade;
	}

	@Override
	public TpceTransactionType getTxnType() {
		return TpceTransactionType.TRADE_RESULT;
	}

	@Override
	public Object[] generateParameter() {
		Object[] params = new Object[4];
		int idxCntr = 0;

		params[idxCntr++] = trade.getTradeId();
		params[idxCntr++] = trade.getCustomerId();
		params[idxCntr++] = trade.getCustomerAccountId();
		params[idxCntr++] = trade.getBrokerId();

		return params;
	}

	@Override
	public void onResponseReceived(SutResultSet result) {
		// do nothing
	}

}
