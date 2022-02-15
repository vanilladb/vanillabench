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

import org.vanilladb.bench.StatisticMgr;
import org.vanilladb.bench.benchmarks.tpce.TpceConstants;
import org.vanilladb.bench.benchmarks.tpce.TpceTransactionType;
import org.vanilladb.bench.benchmarks.tpce.data.TpceDataManager;
import org.vanilladb.bench.benchmarks.tpce.data.Trade;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;
import org.vanilladb.bench.util.RandomValueGenerator;

public class TpceRte extends RemoteTerminalEmulator<TpceTransactionType> {
	
	private static final RandomValueGenerator TX_TYPE_RANDOM = new RandomValueGenerator();
	
	private TpceDataManager dataMgr;
	
	public TpceRte(SutConnection conn, StatisticMgr statMgr, long sleepTime, 
			TpceDataManager dataMgr) {
		super(conn, statMgr, sleepTime);
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
