package org.vanilladb.bench.remote;

public class SutResultSet {
	
	private boolean isCommitted;
	private String outputMsg;

	public SutResultSet(boolean isCommitted, String outputMsg) {
		this.isCommitted = isCommitted;
		this.outputMsg = outputMsg;
	}
	
	public boolean isCommitted() {
		return isCommitted;
	}
	
	public String outputMsg() {
		return outputMsg;
	}
}
