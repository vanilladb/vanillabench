/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
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
package org.vanilladb.bench.remote.sp;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Record;

public class VanillaDbSpResultSet implements SutResultSet {
	
	private SpResultSet result;
	private String message;

	public VanillaDbSpResultSet(SpResultSet result) {
		this.result = result;
	}
	
	@Override
	public boolean isCommitted() {
		return result.isCommitted();
	}
	
	@Override
	public String outputMsg() {
		// Lazy evaluation
		if (message == null) {
			Record[] records = result.getRecords();
			if (records.length > 0)
				message = records[0].toString();
			else
				message = "";
		}
		
		return message;
	}

	public Record[] getRecords() {
		return result.getRecords();
	}
}
