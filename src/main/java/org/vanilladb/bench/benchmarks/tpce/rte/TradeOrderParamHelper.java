package org.vanilladb.bench.benchmarks.tpce.rte;

public class TradeOrderParamHelper {
	
	// Inputs
	private long acctId;
	private String coName, execFName, execLName, execTaxId, issue, stPendingId, 
			stSubmittedId, symbol, tradeTypeId;
	private int isLifo, tradeQty, typeIsMargin;
	private double requestedPrice;
	private boolean rollItBack;
	
	// XXX: inputs that are not in the spec.
	private long customerId;
	private String customerName;
	private long brokerId;
	private double marketPrice;
	private double tradePrice;
	private long tradeId;
	
	public void unpackParameters(Object... pars) {
		if (pars.length != 10)
			throw new RuntimeException("wrong pars list");
		acctId = (Long) pars[0];
		customerId = (Long) pars[1];
		brokerId = (Long) pars[2];
		coName = (String) pars[3];
		requestedPrice = (Double) pars[4];
		rollItBack = (Boolean) pars[5];
		symbol = (String) pars[6];
		tradeQty = (Integer) pars[7];
		tradeTypeId = (String) pars[8];
		tradeId = (Long) pars[9];
	}

	public long getAcctId() {
		return acctId;
	}

	public int getTradeQty() {
		return tradeQty;
	}

	public String getTradeTypeId() {
		return tradeTypeId;
	}

	public boolean isRollback() {
		return rollItBack;
	}

	public double getRequestedPrice() {
		return requestedPrice;
	}

	public String getCoName() {
		return coName;
	}

	public String getSymbol() {
		return symbol;
	}

	public long getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public double getTradePrice() {
		return tradePrice;
	}
	
	public long getTradeId() {
		return tradeId;
	}
}
