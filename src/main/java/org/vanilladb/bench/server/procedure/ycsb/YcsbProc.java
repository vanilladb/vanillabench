package org.vanilladb.bench.server.procedure.ycsb;

import java.util.HashMap;

import org.vanilladb.bench.server.param.ycsb.YcsbBenchmarkProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.bench.ycsb.YcsbConstants;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.Constant;

public class YcsbProc extends BasicStoredProcedure<YcsbBenchmarkProcParamHelper> {
	
	public YcsbProc() {
		super(new YcsbBenchmarkProcParamHelper());
	}
	
	@Override
	protected void executeSql() {

		for (int idx = 0; idx < paramHelper.getReadCount(); idx++) {
			String id = paramHelper.getReadId(idx);
			Scan s = executeQuery("SELECT ycsb_id, ycsb_1 FROM ycsb WHERE ycsb_id = '" + id + "'");
			s.beforeFirst();
			if (s.next()) {
				String ycsb_1 = (String) s.getVal("ycsb_1").asJavaVal();

				paramHelper.setYcsb(ycsb_1, idx);
			} else
				throw new RuntimeException("Cloud not find item record with i_id = " + id);

			s.close();
		}

		for (int idx = 0; idx < paramHelper.getWriteCount(); idx++) {
			String id = paramHelper.getWriteId(idx);
			String newYcsbVal = paramHelper.getWriteValue(idx);
			executeUpdate("UPDATE ycsb SET ycsb_1 = '" + newYcsbVal + "' WHERE ycsb_id = '" + id + "'");
		}
		
		for (int idx = 0; idx < paramHelper.getInsertCount(); idx++) {
			HashMap<String, Constant> fldVals = paramHelper.getInsertVals(idx);
			
			// Generate the field names of YCSB table
			String sql = "INSERT INTO ycsb (ycsb_id";
			for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
				sql += ", ycsb_" + count;
			}
			sql += ") VALUES (";
			
			sql = sql + "'" + fldVals.get("ycsb_id") + "'";
			
			for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
				sql += ", '" + fldVals.get("ycsb_"+count) + "'";
			}
			sql += ")";
		}
	}
}
