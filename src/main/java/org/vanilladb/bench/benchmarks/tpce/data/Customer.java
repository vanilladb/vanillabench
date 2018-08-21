/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
