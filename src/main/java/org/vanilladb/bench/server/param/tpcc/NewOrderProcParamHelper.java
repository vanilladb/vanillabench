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
package org.vanilladb.bench.server.param.tpcc;

import org.vanilladb.bench.benchmarks.tpcc.TpccConstants;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class NewOrderProcParamHelper implements StoredProcedureHelper {
	
	// input parameters
	protected int wid, did, cid, olCount;
	protected int[][] items; // {item_id, warehouse_id, quantities} for each item
	protected boolean allLocal; // Is it a local transaction ?

	protected double wTax, dTax, cDiscount, totalAmount;
	protected long oEntryDate;
	protected String cLast, cCredit;
	protected boolean itemNotFound = false;

	@Override
	public void prepareParameters(Object... pars) {
		if (pars.length != 50)
			throw new RuntimeException("wrong pars list");
		wid = (Integer) pars[0];
		did = (Integer) pars[1];
		cid = (Integer) pars[2];
		olCount = (Integer) pars[3];
		items = new int[15][3];
		int j = 3;
		for (int i = 0; i < olCount; i++) {
			items[i][0] = (Integer) pars[++j];
			items[i][1] = (Integer) pars[++j];
			items[i][2] = (Integer) pars[++j];
		}
		allLocal = (Boolean) pars[49];
	}
	
	@Override
	public boolean isReadOnly() {
		return false;
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

	public int getWid() {
		return wid;
	}

	public int getDid() {
		return did;
	}

	public int getCid() {
		return cid;
	}

	public int getOlCount() {
		return olCount;
	}

	public int[][] getItems() {
		return items;
	}

	public boolean isAllLocal() {
		return allLocal;
	}

	public double getwTax() {
		return wTax;
	}

	public double getdTax() {
		return dTax;
	}

	public double getcDiscount() {
		return cDiscount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public long getoEntryDate() {
		return oEntryDate;
	}

	public String getcLast() {
		return cLast;
	}

	public String getcCredit() {
		return cCredit;
	}

	public boolean isItemNotFound() {
		return itemNotFound;
	}

	public void setWid(int wid) {
		this.wid = wid;
	}

	public void setDid(int did) {
		this.did = did;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public void setOlCount(int olCount) {
		this.olCount = olCount;
	}

	public void setItems(int[][] items) {
		this.items = items;
	}

	public void setAllLocal(boolean allLocal) {
		this.allLocal = allLocal;
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

}
