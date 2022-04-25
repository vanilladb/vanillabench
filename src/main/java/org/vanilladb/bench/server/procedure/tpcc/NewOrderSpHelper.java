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
package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.benchmarks.tpcc.TpccConstants;
import org.vanilladb.bench.benchmarks.tpcc.rte.NewOrderParamHelper;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class NewOrderSpHelper extends NewOrderParamHelper
		implements StoredProcedureHelper {

	protected double wTax, dTax, cDiscount, totalAmount;
	protected long oEntryDate;
	protected String cLast, cCredit;
	protected boolean itemNotFound = false;

	public double getwTax() {
		return wTax;
	}

	public double getdTax() {
		return dTax;
	}

	public double getcDiscount() {
		return cDiscount;
	}

	public long getoEntryDate() {
		return oEntryDate;
	}

	public void setWTax(double wTax) {
		this.wTax = wTax;
	}

	public void setdTax(double dTax) {
		this.dTax = dTax;
	}

	public void setcDiscount(double cDiscount) {
		this.cDiscount = cDiscount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setoEntryDate(long oEntryDate) {
		this.oEntryDate = oEntryDate;
	}

	public void setcLast(String cLast) {
		this.cLast = cLast;
	}

	public void setcCredit(String cCredit) {
		this.cCredit = cCredit;
	}

	public void setItemNotFound(boolean itemNotFound) {
		this.itemNotFound = itemNotFound;
	}

	@Override
	public void prepareParameters(Object... pars) {
		unpackParameters(pars);
	}

	@Override
	public Schema getResultSetSchema() {
		/*
		 * TODO The output information is not strictly followed the TPC-C
		 * definition. See the session 2.4.3.5 in TPC-C 5.11 document.
		 */
		Schema sch = new Schema();
		Type cLastType = Type.VARCHAR(16);
		Type cCreditType = Type.VARCHAR(2);
		Type statusMsgType = Type.VARCHAR(30);
		sch.addField("w_tax", Type.DOUBLE);
		sch.addField("d_tax", Type.DOUBLE);
		sch.addField("c_discount", Type.DOUBLE);
		sch.addField("c_last", cLastType);
		sch.addField("c_credit", cCreditType);
		sch.addField("total_amount", Type.DOUBLE);
		sch.addField("o_entry_date", Type.BIGINT);
		sch.addField("status_msg", statusMsgType);
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		Type cLastType = Type.VARCHAR(16);
		Type cCreditType = Type.VARCHAR(2);
		Type statusMsgType = Type.VARCHAR(30);
		rec.setVal("w_tax", new DoubleConstant(wTax));
		rec.setVal("d_tax", new DoubleConstant(dTax));
		rec.setVal("c_discount", new DoubleConstant(cDiscount));
		rec.setVal("c_last", new VarcharConstant(cLast, cLastType));
		rec.setVal("c_credit", new VarcharConstant(cCredit, cCreditType));
		rec.setVal("total_amount", new DoubleConstant(totalAmount));
		rec.setVal("o_entry_date", new BigIntConstant(oEntryDate));
		String statusMsg = itemNotFound ? TpccConstants.INVALID_ITEM_MESSAGE
				: " ";
		rec.setVal("status_msg", new VarcharConstant(statusMsg, statusMsgType));
		return rec;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
}
