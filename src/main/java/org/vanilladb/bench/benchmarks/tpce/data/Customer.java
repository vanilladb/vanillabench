package org.vanilladb.bench.benchmarks.tpce.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Customer {
	
	private static final Random random = new Random();
	
	private long customerId;
	private List<CustomerAccount> accountList = new ArrayList<CustomerAccount>();

	public Customer(long cId) {
		this.customerId = cId;
	}

	public synchronized void addAccount(CustomerAccount account) {
		accountList.add(account);
	}

	public synchronized CustomerAccount getRandomAccount() {
		return accountList.get(random.nextInt(accountList.size()));
	}

	public long getCustomerId() {
		return customerId;
	}
}
