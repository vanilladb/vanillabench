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
package org.vanilladb.bench.server.param.tpcc;

import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class OrderStatusProcParamHelper implements StoredProcedureHelper {
	protected boolean selectByCLast;
	protected int cwid, cdid, cid, oid, carrierId;
	protected String cLast, cMiddle, cFirst;
	protected long oEntryDate;
	protected double cBalance;

	@Override
	public void prepareParameters(Object... pars) {
		if (pars.length != 3)
			throw new RuntimeException("wrong pars list");
		cwid = (Integer) pars[0];
		cdid = (Integer) pars[1];
		if (pars[2] instanceof String) {
			selectByCLast = true;
			cLast = (String) pars[2];
		} else
			cid = (Integer) pars[2];
	}
	
	@Override
	public boolean isReadOnly() {
		// TODO: Check if this is correct
		return true;
	}

	@Override
	public Schema getResultSetSchema() {
		/*
		 * TODO The output information is not strictly followed the TPC-C
		 * definition. See the session 2.6.3.4 in TPC-C 5.11 document.
		 */
		Schema sch = new Schema();
		Type var16 = Type.VARCHAR(16);
		Type var2 = Type.VARCHAR(2);
		sch.addField("cid", Type.INTEGER);
		sch.addField("c_first", var16);
		sch.addField("c_last", var16);
		sch.addField("c_middle", var2);
		sch.addField("c_balance", Type.DOUBLE);
		sch.addField("o_entry_date", Type.BIGINT);
		sch.addField("o_carrier_id", Type.INTEGER);
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		Type var16 = Type.VARCHAR(16);
		Type var2 = Type.VARCHAR(2);
		rec.setVal("cid", new IntegerConstant(cid));
		rec.setVal("c_first", new VarcharConstant(cFirst, var16));
		rec.setVal("c_last", new VarcharConstant(cLast, var16));
		rec.setVal("c_middle", new VarcharConstant(cMiddle, var2));
		rec.setVal("c_balance", new DoubleConstant(cBalance));
		rec.setVal("o_entry_date", new BigIntConstant(oEntryDate));
		rec.setVal("o_carrier_id", new IntegerConstant(carrierId));
		return rec;
	}

	public boolean isSelectByCLast() {
		return selectByCLast;
	}

	public void setSelectByCLast(boolean selectByCLast) {
		this.selectByCLast = selectByCLast;
	}

	public int getCwid() {
		return cwid;
	}

	public void setCwid(int cwid) {
		this.cwid = cwid;
	}

	public int getCdid() {
		return cdid;
	}

	public void setCdid(int cdid) {
		this.cdid = cdid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public int getOid() {
		return oid;
	}

	public void setOid(int oid) {
		this.oid = oid;
	}

	public int getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(int carrierId) {
		this.carrierId = carrierId;
	}

	public String getcLast() {
		return cLast;
	}

	public void setcLast(String cLast) {
		this.cLast = cLast;
	}

	public String getcMiddle() {
		return cMiddle;
	}

	public void setcMiddle(String cMiddle) {
		this.cMiddle = cMiddle;
	}

	public String getcFirst() {
		return cFirst;
	}

	public void setcFirst(String cFirst) {
		this.cFirst = cFirst;
	}

	public long getoEntryDate() {
		return oEntryDate;
	}

	public void setoEntryDate(long oEntryDate) {
		this.oEntryDate = oEntryDate;
	}

	public double getcBalance() {
		return cBalance;
	}

	public void setcBalance(double cBalance) {
		this.cBalance = cBalance;
	}

}
