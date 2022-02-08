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
package org.vanilladb.bench.server.param.recon;

public class BenchReconTxnProcParamHelper extends BenchTxnProcParamHelper {

	@Override
	public void prepareParameters(Object... pars) {
		
		int indexCnt = 0;
//		System.out.println("Params: " + Arrays.toString(pars));

		readCount = (Integer) pars[indexCnt++];
		
		readRefId = new int[reconCount];
		readItemId = new int[readCount];
		
		for (int i = 0; i < reconCount; i++) {
			readRefId[i] = (Integer) pars[indexCnt++];
			readItemId[i] = -1;
		}

		for (int i = reconCount; i < readCount; i++)
			readItemId[i] = (Integer) pars[indexCnt++];

		writeCount = (Integer) pars[indexCnt++];
		writeItemId = new int[writeCount];
		
		indexCnt++;
		// first write item id is first read item id in micro benchmark
		if (writeCount > 0)
			writeItemId[0] = -1;
		for (int i = 1; i < writeCount; i++)
			writeItemId[i] = (Integer) pars[indexCnt++];
		newItemPrice = new double[writeCount];
		for (int i = 0; i < writeCount; i++)
			newItemPrice[i] = (Double) pars[indexCnt++];

		if (writeCount == 0)
			setReadOnly(true);
	}

}
