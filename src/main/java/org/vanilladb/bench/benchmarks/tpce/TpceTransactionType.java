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

import org.vanilladb.bench.TransactionType;

public enum TpceTransactionType implements TransactionType {
	// Loading procedures
	SCHEMA_BUILDER, TESTBED_LOADER,
	
	// Profiling
	START_PROFILING, STOP_PROFILING,
	
	// TPC-E procedures
	BROKER_VOLUME, CUSTOMER_POSITION, MARKET_FEED, MARKET_WATCH,
	SECURITY_DETAIL, TRADE_LOOKUP, TRADE_ORDER, TRADE_RESULT,
	TRADE_STATUS, TRADE_UPDATE, DATA_MAINTENANCE, TRADE_CLEANUP;
	
	public static TpceTransactionType fromProcedureId(int pid) {
		return TpceTransactionType.values()[pid];
	}
	
	public int getProcedureId() {
		return this.ordinal();
	}
	
	public boolean isBenchmarkingTx() {
		switch (this) {
		case SCHEMA_BUILDER:
		case TESTBED_LOADER:
		case START_PROFILING:
		case STOP_PROFILING:
			return false;
		default:
			return true;
		}
	}
}
