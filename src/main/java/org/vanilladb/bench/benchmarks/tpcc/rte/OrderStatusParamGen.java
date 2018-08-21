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
