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
package org.vanilladb.bench.server.param.tpce;

import org.vanilladb.bench.benchmarks.tpce.rte.TradeResultParamHelper;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class TradeResultSpHelper extends TradeResultParamHelper
		implements StoredProcedureHelper {
	
	// Outputs
	private double acctBal;
	private int loadUnit;
	private long newAcctId;

	@Override
	public void prepareParameters(Object... pars) {
		unpackParameters(pars);
	}

	@Override
	public Schema getResultSetSchema() {
		Schema sch = new Schema();
		sch.addField("acct_bal", Type.DOUBLE);
		sch.addField("acct_id", Type.BIGINT);
		sch.addField("load_unit", Type.INTEGER);
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		rec.setVal("acct_bal", new DoubleConstant(acctBal));
		rec.setVal("acct_id", new BigIntConstant(newAcctId));
		rec.setVal("load_unit", new IntegerConstant(loadUnit));
		return rec;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
	
	// Outputs

	public void setAcctBal(double acctBal) {
		this.acctBal = acctBal;
	}

	public void setAcctId(long acctId) {
		this.newAcctId = acctId;
	}
	
	public void setLoadUnit(int loadUnit) {
		this.loadUnit = loadUnit;
	}
}