package org.vanilladb.bench.benchmarks.tpcc.rte;

public class NewOrderParamHelper {
	
	private int wid, did, cid, olCount;
	private int[][] items; // {item_id, warehouse_id, quantities} for each item
	private boolean allLocal; // Is it a local transaction ?

	public void unpackParameters(Object... pars) {
		if (pars.length != 50)
			throw new RuntimeException("wrong pars list");
		wid = (Integer) pars[0];
		did = (Integer) pars[1];
		cid = (Integer) pars[2];
		olCount = (Integer) pars[3];
		items = new int[15][3];
		int j = 3;
		for (int i = 0; i < olCount; i++) {
			items[i][0] = (Integer) pars[++j];
			items[i][1] = (Integer) pars[++j];
			items[i][2] = (Integer) pars[++j];
		}
		allLocal = (Boolean) pars[49];
	}

	public int getWid() {
		return wid;
	}

	public int getDid() {
		return did;
	}

	public int getCid() {
		return cid;
	}

	public int getOlCount() {
		return olCount;
	}

	public int[][] getItems() {
		return items;
	}

	public boolean isAllLocal() {
		return allLocal;
	}
}
