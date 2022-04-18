package org.vanilladb.bench.server.procedure.ycsb;

import java.util.HashMap;

import org.vanilladb.bench.benchmarks.ycsb.YcsbConstants;
import org.vanilladb.bench.server.param.ycsb.YcsbTxSpParamHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureHelper;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class YcsbProc extends StoredProcedure<YcsbTxSpParamHelper> {
	
	public YcsbProc() {
		super(new YcsbTxSpParamHelper());
	}
	
	@Override
	protected void executeSql() {
		YcsbTxSpParamHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();

		for (int idx = 0; idx < paramHelper.getReadCount(); idx++) {
			String id = paramHelper.getReadIdStr(idx);
			String sql = "SELECT ycsb_id, ycsb_1 FROM ycsb WHERE ycsb_id = '" + id + "'";
			Scan s = StoredProcedureHelper.executeQuery(sql, tx);
			s.beforeFirst();
			if (s.next()) {
				String ycsb_1 = (String) s.getVal("ycsb_1").asJavaVal();

				paramHelper.setYcsb(ycsb_1, idx);
			} else
				throw new RuntimeException("Cloud not find item record with i_id = " + id);

			s.close();
		}

		for (int idx = 0; idx < paramHelper.getWriteCount(); idx++) {
			String id = paramHelper.getWriteIdStr(idx);
			String newYcsbVal = paramHelper.getWriteValue(idx);
			String sql = "UPDATE ycsb SET ycsb_1 = '" + newYcsbVal + "' WHERE ycsb_id = '" + id + "'";
			StoredProcedureHelper.executeUpdate(sql, tx);
		}
		
		for (int idx = 0; idx < paramHelper.getInsertCount(); idx++) {
			HashMap<String, Constant> fldVals = paramHelper.getInsertVals(idx);
			
			// Generate the field names of YCSB table
			StringBuilder sql = new StringBuilder("INSERT INTO ycsb (ycsb_id");
			for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
				sql.append(", ycsb_");
				sql.append(count);
			}
			sql.append(") VALUES (");
			
			// ID
			sql.append("'");
			sql.append(fldVals.get("ycsb_id"));
			sql.append("'");
			
			// Values
			for (int count = 1; count < YcsbConstants.FIELD_COUNT; count++) {
				sql.append(", '");
				sql.append(fldVals.get("ycsb_"+count));
				sql.append("'");
			}
			sql.append(")");
			
			StoredProcedureHelper.executeUpdate(sql.toString(), tx);
		}
	}
}
