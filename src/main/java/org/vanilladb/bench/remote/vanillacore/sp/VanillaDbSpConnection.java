package org.vanilladb.bench.remote.vanillacore.sp;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.core.remote.storedprocedure.SpConnection;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.Record;
import org.vanilladb.core.sql.Schema;

public class VanillaDbSpConnection implements SutConnection {
	private SpConnection conn;

	public VanillaDbSpConnection(SpConnection conn) {
		this.conn = conn;
	}

	@Override
	public SutResultSet callStoredProc(int pid, Object... pars) throws SQLException {
		SpResultSet rs = conn.callStoredProc(pid, pars);
		
		// Get internal data
		Record[] recs = rs.getRecords();
		Schema sch = rs.getSchema();
		
		// Check if it committed
		boolean isCommitted = false;
		if (!sch.hasField("status"))
			throw new RuntimeException("result set not completed");
		String status = (String) recs[0].getVal("status").asJavaVal();
		isCommitted = status.equals("committed");
		
		// Get the output message
		String msg = recs[0].toString();
		
		return new SutResultSet(isCommitted, msg);
	}
	
	@Override
	public Connection toJdbcConnection() {
		throw new RuntimeException("cannot convert a stored procedure connection to a JDBC connection");
	}
}
