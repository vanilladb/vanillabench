package org.vanilladb.bench.server.param.ycsb;

import java.util.HashMap;

import org.vanilladb.bench.ycsb.YcsbConstants;
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
	private String[] readIds;
	private String[] writeIds;
	private String[] writeVals;
	private String[] insertIds;
	private String[] insertVals; // All fields use the same value
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
	
	public String getReadId(int index) {
		return readIds[index];
	}
	
	public String getWriteId(int index) {
		return writeIds[index];
	}
	
	public String getWriteValue(int index) {
		return writeVals[index];
	}
	
	public String getInsertId(int index) {
		return insertIds[index];
	}
	
	public int getLatestIdInParam() {
		return latestId;
	}
	
	public void setYcsb(String s, int idx) {
		ycsb_1[idx] = s;
	}
	
	public HashMap<String, Constant> getInsertVals(int index) {
		HashMap<String, Constant> fldVals = new HashMap<String, Constant>();
		
		fldVals.put("ycsb_id", new VarcharConstant(insertIds[index]));
		for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++)
			fldVals.put("ycsb_" + count, new VarcharConstant(insertVals[index]));
		
		return fldVals;
	}

	@Override
	public void prepareParameters(Object... pars) {
		int indexCnt = 0;

		readCount = (Integer) pars[indexCnt++];
		readIds = new String[readCount];
		ycsb_1 = new String[readCount];
		for (int i = 0; i < readCount; i++)
			readIds[i] = String.format(YcsbConstants.ID_FORMAT, (Integer) pars[indexCnt++]);

		writeCount = (Integer) pars[indexCnt++];
		writeIds = new String[writeCount];
		for (int i = 0; i < writeCount; i++)
			writeIds[i] = String.format(YcsbConstants.ID_FORMAT, (Integer) pars[indexCnt++]);
		writeVals = new String[writeCount];
		for (int i = 0; i < writeCount; i++)
			writeVals[i] = (String) pars[indexCnt++];
		
		insertCount = (Integer) pars[indexCnt++];
		if (insertCount > 0)
			latestId = (Integer) pars[indexCnt];
		insertIds = new String[insertCount];
		for (int i = 0; i < insertCount; i++)
			insertIds[i] = String.format(YcsbConstants.ID_FORMAT, (Integer) pars[indexCnt++]);
		insertVals = new String[insertCount];
		for (int i = 0; i < insertCount; i++)
			insertVals[i] = (String) pars[indexCnt++];

		if (writeCount == 0 && insertCount == 0)
			setReadOnly(true);
	}

	@Override
	public SpResultSet createResultSet() {
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		Type ycsb1Type = Type.VARCHAR(YcsbConstants.CHARS_PER_FIELD);
		sch.addField("status", statusType);
		for (int i = 0; i < readCount; i++) {
			sch.addField("ycsb1_" + i, ycsb1Type);
		}

		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("status", new VarcharConstant(status, statusType));
		for (int i = 0; i < readCount; i++) {
			rec.setVal("ycsb1_" + i, new VarcharConstant(ycsb_1[i], ycsb1Type));
		}

		return new SpResultSet(sch, rec);
	}
}
