package org.vanilladb.bench.server.param.ycsb;

import java.util.HashMap;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class YcsbBenchmarkProcParamHelper extends StoredProcedureParamHelper {
	
	private int readCount;
	private int writeCount;
	private int insertCount;
	private Integer[] readIds;
	private Integer[] writeIds;
	private Integer[] insertIds;
	private String[] writeVals;
	private String[] insertVals; // All fields use the same value to reduce transmission cost
	private String[] ycsb_1;
	
	private int latestId = -1;

	public int getReadCount() {
		return readCount;
	}

	public int getWriteCount() {
		return writeCount;
	}
	
	public int getInsertCount() {
		return insertCount;
	}
	
	public Integer getReadId(int index) {
		return readIds[index];
	}
	
	public String getReadIdStr(int index) {
		return String.format(YcsbConstants.ID_FORMAT, readIds[index]);
	}
	
	public Integer getWriteId(int index) {
		return writeIds[index];
	}
	
	public String getWriteIdStr(int index) {
		return String.format(YcsbConstants.ID_FORMAT, writeIds[index]);
	}
	
	public String getWriteValue(int index) {
		return writeVals[index];
	}
	
	public Integer getInsertId(int index) {
		return insertIds[index];
	}
	
	public String getInsertIdStr(int index) {
		return String.format(YcsbConstants.ID_FORMAT, insertIds[index]);
	}
	
	public int getLatestIdInParam() {
		return latestId;
	}
	
	public void setYcsb(String s, int idx) {
		ycsb_1[idx] = s;
	}
	
	public HashMap<String, Constant> getInsertVals(int index) {
		HashMap<String, Constant> fldVals = new HashMap<String, Constant>();
		
		fldVals.put("ycsb_id", new VarcharConstant(
				String.format(YcsbConstants.ID_FORMAT, insertIds[index])));
		for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++)
			fldVals.put("ycsb_" + count, new VarcharConstant(insertVals[index]));
		
		return fldVals;
	}

	@Override
	public void prepareParameters(Object... pars) {
		int indexCnt = 0;

		readCount = (Integer) pars[indexCnt++];
		readIds = new Integer[readCount];
		ycsb_1 = new String[readCount];
		for (int i = 0; i < readCount; i++)
			readIds[i] = (Integer) pars[indexCnt++];

		writeCount = (Integer) pars[indexCnt++];
		writeIds = new Integer[readCount];
		for (int i = 0; i < writeCount; i++)
			writeIds[i] = (Integer) pars[indexCnt++];
		writeVals = new String[writeCount];
		for (int i = 0; i < writeCount; i++)
			writeVals[i] = (String) pars[indexCnt++];
		
		insertCount = (Integer) pars[indexCnt++];
		if (insertCount > 0)
			latestId = (Integer) pars[indexCnt];
		insertIds = new Integer[readCount];
		for (int i = 0; i < insertCount; i++)
			insertIds[i] = (Integer) pars[indexCnt++];
		insertVals = new String[insertCount];
		for (int i = 0; i < insertCount; i++)
			insertVals[i] = (String) pars[indexCnt++];

		if (writeCount == 0 && insertCount == 0)
			setReadOnly(true);
	}

	@Override
	public SpResultSet createResultSet() {
		Schema sch = new Schema();
		Type ycsb1Type = Type.VARCHAR(YcsbConstants.CHARS_PER_FIELD);
		for (int i = 0; i < readCount; i++)
			sch.addField("ycsb1_" + i, ycsb1Type);

		SpResultRecord rec = new SpResultRecord();
		for (int i = 0; i < readCount; i++)
			rec.setVal("ycsb1_" + i, new VarcharConstant(ycsb_1[i], ycsb1Type));

		return new SpResultSet(isCommitted(), sch, rec);
	}
}
