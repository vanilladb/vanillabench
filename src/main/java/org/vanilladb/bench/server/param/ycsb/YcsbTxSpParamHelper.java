package org.vanilladb.bench.server.param.ycsb;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;
import org.vanilladb.bench.benchmarks.ycsb.rte.YcsbTxParamHelper;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class YcsbTxSpParamHelper extends YcsbTxParamHelper
		implements StoredProcedureParamHelper {
	
	private String[] ycsb_1;
	
	public void setYcsb(String s, int idx) {
		ycsb_1[idx] = s;
	}

	@Override
	public void prepareParameters(Object... pars) {
		unpackParameters(pars);
		
		ycsb_1 = new String[getReadCount()];
	}

	@Override
	public Schema getResultSetSchema() {
		Schema sch = new Schema();
		Type ycsb1Type = Type.VARCHAR(YcsbConstants.CHARS_PER_FIELD);
		for (int i = 0; i < getReadCount(); i++)
			sch.addField("ycsb1_" + i, ycsb1Type);
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		Type ycsb1Type = Type.VARCHAR(YcsbConstants.CHARS_PER_FIELD);
		for (int i = 0; i < getReadCount(); i++)
			rec.setVal("ycsb1_" + i, new VarcharConstant(ycsb_1[i], ycsb1Type));
		return rec;
	}
}
