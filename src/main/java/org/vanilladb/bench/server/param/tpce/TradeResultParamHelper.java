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

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TradeResultParamHelper extends StoredProcedureParamHelper {
	
	// Inputs
	private long tradeId;
	private double tradePrice;
	
	// Outputs
	private double acctBal;
	private long acctId;
	private int loadUnit;
	
	// XXX: inputs that are not in the spec.
	protected long customerId;
	protected long brokerId;

	@Override
	public void prepareParameters(Object... pars) {
		if (pars.length != 4)
			throw new RuntimeException("wrong pars list");
		int idxCntr = 0;
		tradeId = (Long) pars[idxCntr++];
		customerId = (Long) pars[idxCntr++];
		acctId = (Long) pars[idxCntr++];
		brokerId = (Long) pars[idxCntr++];
	}

	@Override
	public SpResultSet createResultSet() {
		Schema sch = new Schema();
		sch.addField("acct_bal", Type.DOUBLE);
		sch.addField("acct_id", Type.BIGINT);
		sch.addField("load_unit", Type.INTEGER);

		SpResultRecord rec = new SpResultRecord();
		rec.setVal("acct_bal", new DoubleConstant(acctBal));
		rec.setVal("acct_id", new BigIntConstant(acctId));
		rec.setVal("load_unit", new IntegerConstant(loadUnit));

		return new SpResultSet(isCommitted(), sch, rec);
	}

	public long getCustomerId() {
		return customerId;
	}

	public long getAcctId() {
		return acctId;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public long getTradeId() {
		return tradeId;
	}
	
	// Outputs

	public void setAcctBal(double acctBal) {
		this.acctBal = acctBal;
	}

	public void setAcctId(long acctId) {
		this.acctId = acctId;
	}
	
	public void setLoadUnit(int loadUnit) {
		this.loadUnit = loadUnit;
	}
}