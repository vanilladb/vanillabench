package org.vanilladb.bench.benchmarks.tpce.rte;

public class TradeResultParamHelper {
	
	// Inputs
	private long tradeId;
	private double tradePrice;
	private long acctId;
	
	// XXX: inputs that are not in the spec.
	protected long customerId;
	protected long brokerId;

	public void unpackParameters(Object... pars) {
		if (pars.length != 4)
			throw new RuntimeException("wrong pars list");
		int idxCntr = 0;
		tradeId = (Long) pars[idxCntr++];
		customerId = (Long) pars[idxCntr++];
		acctId = (Long) pars[idxCntr++];
		brokerId = (Long) pars[idxCntr++];
	}
	
	public long getCustomerId() {
		return customerId;
	}

	public long getAcctId() {
		return acctId;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public long getTradeId() {
		return tradeId;
	}
}
