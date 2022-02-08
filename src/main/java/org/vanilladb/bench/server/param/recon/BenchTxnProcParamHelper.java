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

import java.util.ArrayList;

import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class BenchTxnProcParamHelper extends StoredProcedureParamHelper {

	protected int reconCount = 1;
	protected int readCount;
	protected int writeCount;
	protected int[] readRefId;
	protected int[] RefIid;
	protected int[] readItemId;
	protected int[] writeItemId;
	protected double[] newItemPrice;
	protected String[] itemName;
	protected double[] itemPrice;
	
	public int getReconCount() {
		return reconCount;
	}
	
	public int getReadCount() {
		return readCount;
	}

	public int getWriteCount() {
		return writeCount;
	}
	
	public int getReadRefId(int index) {
		return readRefId[index];
	}

	public void setRefIid(int s, int idx) {
		RefIid[idx] = s;
	}
	
	public int getRefIid(int idx) {
		return RefIid[idx];
	}
	
	public void setReadItemId(int s, int idx) {
		readItemId[idx] = s;
	}
	
	public int getReadItemId(int index) {
		return readItemId[index];
	}
	
	public void setWriteItemId(int s, int idx) {
		writeItemId[idx] = s;
	}

	public int getWriteItemId(int index) {
		return writeItemId[index];
	}

	public double getNewItemPrice(int index) {
		return newItemPrice[index];
	}

	public void setItemName(String s, int idx) {
		itemName[idx] = s;
	}

	public void setItemPrice(double d, int idx) {
		itemPrice[idx] = d;
	}
	
	public Object[] generateParameter() {
	
		ArrayList<Object> paramList = new ArrayList<Object>();
				
		paramList.add(reconCount);
		for (int i = 0; i < reconCount; i++) {
			paramList.add(readRefId[i]);
		}
	
		paramList.add(readCount);
		for (int i = 0; i < readCount; i++) {
			paramList.add(readItemId[i]);
		}
		
		paramList.add(writeCount);
		for (int i = 0; i < writeCount; i++) {
			paramList.add(writeItemId[i]);
			paramList.add(newItemPrice[i]);
		}

		return paramList.toArray(new Object[0]);
	}
	
	@Override
	public void prepareParameters(Object... pars) {
		throw new UnsupportedOperationException("should not call this");
	}

	@Override
	public Schema getResultSetSchema() {
		Schema sch = new Schema();
		if (itemName == null) {
			return sch; 
		}
		Type intType = Type.INTEGER;
		Type itemPriceType = Type.DOUBLE;
		Type itemNameType = Type.VARCHAR(24);
		sch.addField("rc", intType);
		for (int i = 0; i < itemName.length; i++) {
			sch.addField("i_name_" + i, itemNameType);
			sch.addField("i_price_" + i, itemPriceType);
		}
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		if (itemName == null) {
			return rec; 
		}
		rec.setVal("rc", new IntegerConstant(itemName.length));
		for (int i = 0; i < itemName.length; i++) {
			rec.setVal("i_name_" + i, new VarcharConstant(itemName[i], Type.VARCHAR(24)));
			rec.setVal("i_price_" + i, new DoubleConstant(itemPrice[i]));
		}
		return rec;
	}

}
