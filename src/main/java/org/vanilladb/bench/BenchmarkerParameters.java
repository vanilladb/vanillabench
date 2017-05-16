package org.vanilladb.bench;

import org.vanilladb.bench.util.BenchProperties;

public class BenchmarkerParameters {
	
	public static final long WARM_UP_INTERVAL;
	public static final long BENCHMARK_INTERVAL;
	public static final int NUM_RTES;
	
	// JDBC = 1, SP = 2
	public static enum ConnectionMode { JDBC, SP };
	public static final ConnectionMode CONNECTION_MODE;
	
	// Micro = 1, TPC-C = 2, TPC-E = 3
	public static enum BenchType { MICRO, TPCC, TPCE };
	public static final BenchType BENCH_TYPE;
	
	public static final boolean PROFILING_ON_SERVER;

	static {
		WARM_UP_INTERVAL = BenchProperties.getLoader().getPropertyAsLong(
				BenchmarkerParameters.class.getName() + ".WARM_UP_INTERVAL", 60000);

		BENCHMARK_INTERVAL = BenchProperties.getLoader().getPropertyAsLong(
				BenchmarkerParameters.class.getName() + ".BENCHMARK_INTERVAL",
				60000);

		NUM_RTES = BenchProperties.getLoader().getPropertyAsInteger(
				BenchmarkerParameters.class.getName() + ".NUM_RTES", 1);
		
		int conMode = BenchProperties.getLoader().getPropertyAsInteger(
				BenchmarkerParameters.class.getName() + ".CONNECTION_MODE", 1);
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
				BenchmarkerParameters.class.getName() + ".BENCH_TYPE", 1);
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
		default:
			throw new IllegalArgumentException("The connection mode should be 1 (Micro), 2 (TPC-C), or 3 (TPC-E)");
		}
		
		PROFILING_ON_SERVER = BenchProperties.getLoader().getPropertyAsBoolean(
				BenchmarkerParameters.class.getName() + ".PROFILING_ON_SERVER", false);
	}
}