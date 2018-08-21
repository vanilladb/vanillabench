package org.vanilladb.bench.remote.jdbc;

import org.vanilladb.bench.remote.SutResultSet;

public class VanillaDbJdbcResultSet implements SutResultSet {
	
	private boolean isCommitted;
	private String outputMsg;

	public VanillaDbJdbcResultSet(boolean isCommitted, String outputMsg) {
		this.isCommitted = isCommitted;
		this.outputMsg = outputMsg;
	}
	
	@Override
	public boolean isCommitted() {
		return isCommitted;
	}
	
	@Override
	public String outputMsg() {
		return outputMsg;
	}
}
