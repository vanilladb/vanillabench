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
package org.vanilladb.bench;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.util.BenchProperties;

public class VanillaBenchParameters {
	private static Logger logger = Logger.getLogger(VanillaBenchParameters.class
			.getName());
	
	public static final long WARM_UP_INTERVAL;
	public static final long BENCHMARK_INTERVAL;
	public static final int NUM_RTES;
	public static final long RTE_SLEEP_TIME;
	
	public static final String SERVER_IP;
	
	// JDBC = 1, SP = 2
	public static enum ConnectionMode { JDBC, SP };
	public static final ConnectionMode CONNECTION_MODE;
	
	// Micro = 1, TPC-C = 2, TPC-E = 3, YCSB = 4, ANN = 5
	public static enum BenchType { MICRO, TPCC, TPCE, YCSB, ANN };
	public static final BenchType BENCH_TYPE;
	
	public static final boolean PROFILING_ON_SERVER;
	
	public static final File REPORT_OUTPUT_DIRECTORY;
	public static final int REPORT_TIMELINE_GRANULARITY;
	
	public static final boolean SHOW_TXN_RESPONSE_ON_CONSOLE;

	static {
		WARM_UP_INTERVAL = BenchProperties.getLoader().getPropertyAsLong(
				VanillaBenchParameters.class.getName() + ".WARM_UP_INTERVAL", 60000);

		BENCHMARK_INTERVAL = BenchProperties.getLoader().getPropertyAsLong(
				VanillaBenchParameters.class.getName() + ".BENCHMARK_INTERVAL",
				60000);

		NUM_RTES = BenchProperties.getLoader().getPropertyAsInteger(
				VanillaBenchParameters.class.getName() + ".NUM_RTES", 1);
		
		RTE_SLEEP_TIME = BenchProperties.getLoader().getPropertyAsLong(
				VanillaBenchParameters.class.getName() + ".RTE_SLEEP_TIME", 0);
		
		SERVER_IP = BenchProperties.getLoader().getPropertyAsString(
				VanillaBenchParameters.class.getName() + ".SERVER_IP", "127.0.0.1");
		
		int conMode = BenchProperties.getLoader().getPropertyAsInteger(
				VanillaBenchParameters.class.getName() + ".CONNECTION_MODE", 1);
		switch (conMode) {
		case 1:
			CONNECTION_MODE = ConnectionMode.JDBC;
			break;
		case 2:
			CONNECTION_MODE = ConnectionMode.SP;
			break;
		default:
			throw new IllegalArgumentException("The connection mode should be 1 (JDBC) or 2 (SP)");
		}

		int benchType = BenchProperties.getLoader().getPropertyAsInteger(
				VanillaBenchParameters.class.getName() + ".BENCH_TYPE", 1);
		switch (benchType) {
		case 1:
			BENCH_TYPE = BenchType.MICRO;
			break;
		case 2:
			BENCH_TYPE = BenchType.TPCC;
			break;
		case 3:
			BENCH_TYPE = BenchType.TPCE;
			break;
		case 4:
			BENCH_TYPE = BenchType.YCSB;
			break;
		case 5:
			BENCH_TYPE = BenchType.ANN;
			break;
		default:
			throw new IllegalArgumentException("The connection mode should be 1 (Micro), 2 (TPC-C), or 3 (TPC-E)");
		}
		
		if (logger.isLoggable(Level.INFO))
			logger.info("Using " + BENCH_TYPE + " benchmarks");
		
		PROFILING_ON_SERVER = BenchProperties.getLoader().getPropertyAsBoolean(
				VanillaBenchParameters.class.getName() + ".PROFILING_ON_SERVER", false);
		
		// Report Output Directory
		String outputDirPath = BenchProperties.getLoader()
				.getPropertyAsString(
						VanillaBenchParameters.class.getName() + ".REPORT_OUTPUT_DIRECTORY",null);

		if (outputDirPath == null) {
			REPORT_OUTPUT_DIRECTORY = new File(System.getProperty("user.home"), "benchmark_results");
		} else {
			REPORT_OUTPUT_DIRECTORY = new File(outputDirPath);
		}

		// Create the directory if that doesn't exist
		if (!REPORT_OUTPUT_DIRECTORY.exists())
			REPORT_OUTPUT_DIRECTORY.mkdir();

		REPORT_TIMELINE_GRANULARITY = BenchProperties.getLoader().getPropertyAsInteger(
				VanillaBenchParameters.class.getName() + ".REPORT_TIMELINE_GRANULARITY", 3000);
		
		SHOW_TXN_RESPONSE_ON_CONSOLE = BenchProperties.getLoader().getPropertyAsBoolean(
				VanillaBenchParameters.class.getName() + ".SHOW_TXN_RESPONSE_ON_CONSOLE", false);
	}
}