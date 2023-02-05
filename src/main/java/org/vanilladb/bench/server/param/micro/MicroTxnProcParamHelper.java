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
package org.vanilladb.bench.server.param.micro;

import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class MicroTxnProcParamHelper implements StoredProcedureHelper {

	private int readCount;
	private int writeCount;
	private int[] readItemId;
	private int[] writeItemId;
	private double[] newItemPrice;
	private String[] itemName;
	private double[] itemPrice;

	public int getReadCount() {
		return readCount;
	}

	public int getWriteCount() {
		return writeCount;
	}

	public int getReadItemId(int index) {
		return readItemId[index];
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

	@Override
	public void prepareParameters(Object... pars) {

		// Show the contents of paramters
	   //System.out.println("Params: " + Arrays.toString(pars));

		int indexCnt = 0;

		readCount = (Integer) pars[indexCnt++];
		readItemId = new int[readCount];
		itemName = new String[readCount];
		itemPrice = new double[readCount];

		for (int i = 0; i < readCount; i++)
			readItemId[i] = (Integer) pars[indexCnt++];

		writeCount = (Integer) pars[indexCnt++];
		writeItemId = new int[writeCount];
		for (int i = 0; i < writeCount; i++)
			writeItemId[i] = (Integer) pars[indexCnt++];
		newItemPrice = new double[writeCount];
		for (int i = 0; i < writeCount; i++)
			newItemPrice[i] = (Double) pars[indexCnt++];
	}
	
	@Override
	public boolean isReadOnly() {
		return writeCount == 0;
	}

	@Override
	public Schema getResultSetSchema() {
		Schema sch = new Schema();
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
		rec.setVal("rc", new IntegerConstant(itemName.length));
		for (int i = 0; i < itemName.length; i++) {
			rec.setVal("i_name_" + i, new VarcharConstant(itemName[i], Type.VARCHAR(24)));
			rec.setVal("i_price_" + i, new DoubleConstant(itemPrice[i]));
		}
		return rec;
	}

}
