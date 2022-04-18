package org.vanilladb.bench.benchmarks.tpcc.rte;

public class PaymentParamHelper {

	private int wid, did, cwid, cdid, cid;
	private double hAmount;

	public void unpackParameters(Object... pars) {
		if (pars.length != 6)
			throw new RuntimeException("wrong pars list");
		wid = (Integer) pars[0];
		did = (Integer) pars[1];
		cwid = (Integer) pars[2];
		cdid = (Integer) pars[3];
		cid = (Integer) pars[4];
		hAmount = (Double) pars[5];
	}

	public int getWid() {
		return wid;
	}

	public int getDid() {
		return did;
	}

	public int getCwid() {
		return cwid;
	}

	public int getCdid() {
		return cdid;
	}

	public double getHamount() {
		return hAmount;
	}

	public int getCid() {
		return cid;
	}
}
