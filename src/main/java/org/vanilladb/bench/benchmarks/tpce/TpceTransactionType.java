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
	SCHEMA_BUILDER(false), TESTBED_LOADER(false),
	
	// Database checking procedures
	CHECK_DATABASE(false),
	
	// TPC-E procedures
	BROKER_VOLUME(true), CUSTOMER_POSITION(true), MARKET_FEED(true), MARKET_WATCH(true),
	SECURITY_DETAIL(true), TRADE_LOOKUP(true), TRADE_ORDER(true), TRADE_RESULT(true),
	TRADE_STATUS(true), TRADE_UPDATE(true), DATA_MAINTENANCE(true), TRADE_CLEANUP(true);
	
	public static TpceTransactionType fromProcedureId(int pid) {
		return TpceTransactionType.values()[pid];
	}
	
	private boolean isBenchProc;
	
	TpceTransactionType(boolean isLoadProc) {
		this.isBenchProc = isLoadProc;
	}
	
	@Override
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isBenchmarkingProcedure() {
		return isBenchProc;
	}
}
