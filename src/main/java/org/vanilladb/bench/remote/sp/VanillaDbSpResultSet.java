package org.vanilladb.bench.remote.sp;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Record;
import org.vanilladb.core.sql.Schema;

public class VanillaDbSpResultSet implements SutResultSet {
	private Record[] recs;
	private Schema sch;

	public VanillaDbSpResultSet(SpResultSet result) {
		recs = result.getRecords();
		sch = result.getSchema();
	}
	
	@Override
	public boolean isCommitted() {
		if (!sch.hasField("status"))
			throw new RuntimeException("result set not completed");
		String status = (String) recs[0].getVal("status").asJavaVal();
		return status.equals("committed");
	}
	
	@Override
	public String outputMsg() {
		return recs[0].toString();
	}
}
