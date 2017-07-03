package org.vanilladb.bench.tpce.data;

public class Trade {
	
	private long tradeId;
	private long customerId;
	private long customerAccountId;
	private long brokerId;

	public Trade(long tradeId, long customerId, long customerAccountId,
			long brokerId) {
		this.tradeId = tradeId;
		this.customerId = customerId;
		this.customerAccountId = customerAccountId;
		this.brokerId = brokerId;
	}

	public long getTradeId() {
		return tradeId;
	}

	public long getCustomerId() {
		return customerId;
	}

	public long getCustomerAccountId() {
		return customerAccountId;
	}

	public long getBrokerId() {
		return brokerId;
	}
}
