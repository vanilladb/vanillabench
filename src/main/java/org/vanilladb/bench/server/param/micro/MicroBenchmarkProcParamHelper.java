package org.vanilladb.bench.server.param.micro;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.IntegerConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class MicroBenchmarkProcParamHelper extends StoredProcedureParamHelper {

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

		if (writeCount == 0)
			setReadOnly(true);
	}

	@Override
	public SpResultSet createResultSet() {
		/*
		 * TODO The output information is not strictly followed the TPC-C
		 * definition. See the session 2.4.3.5 in TPC-C 5.11 document.
		 */
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		Type intType = Type.INTEGER;
		Type itemPriceType = Type.DOUBLE;
		Type itemNameType = Type.VARCHAR(24);
		sch.addField("status", statusType);
		sch.addField("rc", intType);
		int l = itemName.length;
		for (int i = 0; i < l; i++) {
			sch.addField("i_name_" + i, itemNameType);
			sch.addField("i_price_" + i, itemPriceType);
		}

		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, statusType));
		rec.setVal("rc", new IntegerConstant(l));
		for (int i = 0; i < l; i++) {
			rec.setVal("i_name_" + i, new VarcharConstant(itemName[i], itemNameType));
			rec.setVal("i_price_" + i, new DoubleConstant(itemPrice[i]));
		}

		return new SpResultSet(sch, rec);
	}

}
