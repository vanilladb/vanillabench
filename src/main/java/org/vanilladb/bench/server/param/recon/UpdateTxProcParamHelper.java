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

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class UpdateTxProcParamHelper extends StoredProcedureParamHelper {

	private int updateCount;
	private int[] refId;
	private int[] refIid;
	
	public int getUpdateCount() {
		return updateCount;
	}
	
	public int getRefId(int index) {
		return refId[index];
	}

	public void setRefIid(int s, int idx) {
		refIid[idx] = s;
	}
	
	public int getRefIid(int idx) {
		return refIid[idx];
	}
	
	public int getNewRefIid(int idx) {
		return refIid[updateCount - idx - 1];
	}

	@Override
	public void prepareParameters(Object... pars) {
		
		int indexCnt = 0;
//		System.out.println("Params: " + Arrays.toString(pars));
		
		updateCount = (Integer) pars[indexCnt++];
		refId = new int[updateCount];
		refIid = new int[updateCount];
		for (int i = 0; i < updateCount; i++) {
			refId[i] = (Integer) pars[indexCnt++];
		}

		setReadOnly(true);	

	}

	@Override
	public Schema getResultSetSchema() {
		return new Schema();
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		return new SpResultRecord();
	}

}
