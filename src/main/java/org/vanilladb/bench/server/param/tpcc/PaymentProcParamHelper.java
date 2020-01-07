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
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class PaymentProcParamHelper extends StoredProcedureParamHelper {

	protected int wid, did, cwid, cdid, cidInt;
	protected String cDataStr, cLast, cMiddle, cFirst, cStreet1, cStreet2, cCity, cState, cZip, cPhone, cCredit;
	protected int cid;
	protected long cSince;
	protected double cBalance, cCreditLim, cDiscount;
	String wName, wStreet1, wStreet2, wCity, wState, wZip;

	protected long hDateLong;
	protected double hAmount;
	protected boolean isBadCredit = false;

	@Override
	public void prepareParameters(Object... pars) {
		if (pars.length != 6)
			throw new RuntimeException("wrong pars list");
		wid = (Integer) pars[0];
		did = (Integer) pars[1];
		cwid = (Integer) pars[2];
		cdid = (Integer) pars[3];
		cidInt = (Integer) pars[4];
		hAmount = (Double) pars[5];
		cid = cidInt;
		
	}

	@Override
	public Schema getResultSetSchema() {
		/*
		 * TODO The output information is not strictly followed the TPC-C
		 * definition. See the session 2.5.3.4 in TPC-C 5.11 document.
		 */
		Schema sch = new Schema();
		sch.addField("cid", Type.INTEGER);
		sch.addField("c_first", Type.VARCHAR(16));
		sch.addField("c_last", Type.VARCHAR(16));
		sch.addField("c_middle", Type.VARCHAR(2));
		sch.addField("c_street_1", Type.VARCHAR(20));
		sch.addField("c_street_2", Type.VARCHAR(20));
		sch.addField("c_city", Type.VARCHAR(20));
		sch.addField("c_state", Type.VARCHAR(2));
		sch.addField("c_zip", Type.VARCHAR(9));
		sch.addField("c_phone", Type.VARCHAR(16));
		sch.addField("c_credit", Type.VARCHAR(2));
		sch.addField("c_since", Type.BIGINT);
		sch.addField("c_balance", Type.DOUBLE);
		sch.addField("c_credit_lim", Type.DOUBLE);
		sch.addField("c_discount", Type.DOUBLE);
		sch.addField("h_date", Type.BIGINT);
		if (isBadCredit)
			sch.addField("c_data", Type.VARCHAR(200));
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		rec.setVal("cid", new IntegerConstant(cid));
		rec.setVal("c_first", new VarcharConstant(cFirst, Type.VARCHAR(16)));
		rec.setVal("c_last", new VarcharConstant(cLast, Type.VARCHAR(16)));
		rec.setVal("c_middle", new VarcharConstant(cMiddle, Type.VARCHAR(2)));
		rec.setVal("c_street_1", new VarcharConstant(cStreet1, Type.VARCHAR(20)));
		rec.setVal("c_street_2", new VarcharConstant(cStreet2, Type.VARCHAR(20)));
		rec.setVal("c_city", new VarcharConstant(cCity, Type.VARCHAR(20)));
		rec.setVal("c_state", new VarcharConstant(cState, Type.VARCHAR(2)));
		rec.setVal("c_zip", new VarcharConstant(cZip, Type.VARCHAR(9)));
		rec.setVal("c_phone", new VarcharConstant(cPhone, Type.VARCHAR(16)));
		rec.setVal("c_credit", new VarcharConstant(cCredit, Type.VARCHAR(2)));
		rec.setVal("c_since", new BigIntConstant(cSince));
		rec.setVal("c_balance",new DoubleConstant( cBalance));
		rec.setVal("c_credit_lim", new DoubleConstant(cCreditLim));
		rec.setVal("c_discount", new DoubleConstant(cDiscount));
		rec.setVal("h_date", new BigIntConstant(hDateLong));
		if (isBadCredit)
			rec.setVal("c_data", new VarcharConstant(cDataStr, Type.VARCHAR(200)));
		return rec;
	}

	public int getWid() {
		return wid;
	}

	public int getDid() {
		return did;
	}

	public int getCwid() {
		return cwid;
	}

	public int getCdid() {
		return cdid;
	}

	public double getHamount() {
		return hAmount;
	}

	public int getcid() {
		return cidInt;
	}

	public void setcLast(String x) {
		this.cLast = x;
	}

	public void setcMiddle(String x) {
		this.cMiddle = x;
	}

	public void setcFirst(String x) {
		this.cFirst = x;
	}

	public void setcStreet1(String x) {
		this.cStreet1 = x;
	}

	public void setcStreet2(String x) {
		this.cStreet2 = x;
	}

	public void setcCity(String x) {
		this.cCity = x;
	}

	public void setcState(String x) {
		this.cState = x;
	}

	public void setcZip(String x) {
		this.cZip = x;
	}

	public void setcPhone(String x) {
		this.cPhone = x;
	}

	public void setcCredit(String x) {
		this.cCredit = x;
	}

	public void setcSince(long x) {
		this.cSince = x;
	}

	public void setcBalance(double x) {
		this.cBalance = x;
	}

	public void setcCreditLim(double x) {
		this.cCreditLim = x;
	}

	public void setcDiscount(double x) {
		this.cDiscount = x;
	}
	public void setisBadCredit(boolean bc){
		this.isBadCredit = bc;
	}
	
	public void setcData(String cDataStr){
		this.cDataStr = cDataStr;
	}
	public void sethDate(long hDateLong){
		this.hDateLong = hDateLong;
	}

}
