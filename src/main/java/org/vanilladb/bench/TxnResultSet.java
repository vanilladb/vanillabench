/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
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
package org.vanilladb.bench;

public class TxnResultSet {
	private BenchTransactionType txnType;
	private long respTime; // in ns
	private long endTime; // in ns
	private boolean isCommitted;
	private String outputMessage;

	public TxnResultSet(BenchTransactionType txnType, long respTime, long endTime,
			boolean isCommitted, String outputMessage) {
		this.txnType = txnType;
		this.respTime = respTime;
		this.endTime = endTime;
		this.isCommitted = isCommitted;
		this.outputMessage = outputMessage;
	}

	public BenchTransactionType getTxnType() {
		return txnType;
	}

	public long getTxnResponseTime() {
		return respTime;
	}
	
	public long getTxnEndTime(){
		return endTime;
	}

	public boolean isTxnIsCommited() {
		return isCommitted;
	}

	public String getOutMsg() {
		return outputMessage;
	}
}
