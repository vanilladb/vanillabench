package org.vanilladb.bench.tpce.data;

public class CustomerAccount {
	private long accountId;
	private long brokerId;

	public CustomerAccount(long aId, long bId) {
		accountId = aId;
		brokerId = bId;
	}

	public long getAccountId() {
		return accountId;
	}

	public long getBrokerId() {
		return brokerId;
	}
}
