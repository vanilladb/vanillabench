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
package org.vanilladb.bench.benchmarks.recon.rte;

import org.vanilladb.bench.benchmarks.recon.ReconbenchConstants;
import org.vanilladb.bench.benchmarks.recon.ReconbenchTransactionType;
import org.vanilladb.bench.rte.TxParamGenerator;

public class TestbedLoaderParamGen implements TxParamGenerator<ReconbenchTransactionType> {

	@Override
	public ReconbenchTransactionType getTxnType() {
		return ReconbenchTransactionType.TESTBED_LOADER;
	}

	@Override
	public Object[] generateParameter() {
		// [# of items]
		return new Object[] {ReconbenchConstants.NUM_ITEMS};
	}

}
