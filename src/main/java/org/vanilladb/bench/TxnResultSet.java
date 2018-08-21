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
	private TransactionType txnType;
	private long keyingTime;
	private long thinkTime;
	private long txnResponseTimeNs;
	private long txnEndTime;
	private boolean txnIsCommited;
	private String outMsg;

	public TxnResultSet() {

	}

	public TransactionType getTxnType() {
		return txnType;
	}

	public void setTxnType(TransactionType txnType) {
		this.txnType = txnType;
	}

	public long getKeyingTime() {
		return keyingTime;
	}

	public void setKeyingTime(long keyingTime) {
		this.keyingTime = keyingTime;
	}

	public long getThinkTime() {
		return thinkTime;
	}

	public void setThinkTime(long thinkTime) {
		this.thinkTime = thinkTime;
	}

	public long getTxnResponseTime() {
		return txnResponseTimeNs;
	}

	public void setTxnResponseTimeNs(long txnResponseTime) {
		this.txnResponseTimeNs = txnResponseTime;
	}
	
	public long getTxnEndTime(){
		return txnEndTime;
	}
	
	public void setTxnEndTime(){
		this.txnEndTime = System.nanoTime();
	}


	public boolean isTxnIsCommited() {
		return txnIsCommited;
	}

	public void setTxnIsCommited(boolean txnIsCommited) {
		this.txnIsCommited = txnIsCommited;
	}

	public String getOutMsg() {
		return outMsg;
	}

	public void setOutMsg(String outMsg) {
		this.outMsg = outMsg;
	}

}
