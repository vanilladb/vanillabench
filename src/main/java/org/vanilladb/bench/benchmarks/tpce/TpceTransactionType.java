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
package org.vanilladb.bench.benchmarks.tpce;

import org.vanilladb.bench.BenchTransactionType;

public enum TpceTransactionType implements BenchTransactionType {
	// Loading procedures
	SCHEMA_BUILDER(true), TESTBED_LOADER(true),
	
	// TPC-E procedures
	BROKER_VOLUME(false), CUSTOMER_POSITION(false), MARKET_FEED(false), MARKET_WATCH(false),
	SECURITY_DETAIL(false), TRADE_LOOKUP(false), TRADE_ORDER(false), TRADE_RESULT(false),
	TRADE_STATUS(false), TRADE_UPDATE(false), DATA_MAINTENANCE(false), TRADE_CLEANUP(false);
	
	public static TpceTransactionType fromProcedureId(int pid) {
		return TpceTransactionType.values()[pid];
	}
	
	private boolean isLoadProc;
	
	TpceTransactionType(boolean isLoadProc) {
		this.isLoadProc = isLoadProc;
	}
	
	@Override
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isLoadingProcedure() {
		return isLoadProc;
	}
}
