package org.vanilladb.bench.benchmarks.tpcc.rte;

public class OrderStatusParamHelper {
	
	private int cwid, cdid, cid;
	private String cLast;
	private boolean selectByCLast;
	
	public void unpackParameters(Object... pars) {
		if (pars.length != 3)
			throw new RuntimeException("wrong pars list");
		
		cwid = (Integer) pars[0];
		cdid = (Integer) pars[1];
		if (pars[2] instanceof String) {
			selectByCLast = true;
			cLast = (String) pars[2];
		} else
			cid = (Integer) pars[2];
	}

	public int getCwid() {
		return cwid;
	}

	public int getCdid() {
		return cdid;
	}

	public int getCid() {
		return cid;
	}

	public String getcLast() {
		return cLast;
	}

	public boolean isSelectByCLast() {
		return selectByCLast;
	}

}
