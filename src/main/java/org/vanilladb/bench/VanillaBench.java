package org.vanilladb.bench;

import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.benchmarks.micro.MicroBenchmark;
import org.vanilladb.bench.benchmarks.tpcc.TpccBenchmark;
import org.vanilladb.bench.benchmarks.tpce.TpceBenchmark;
import org.vanilladb.bench.benchmarks.ycsb.YcsbBenchmark;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcDriver;
import org.vanilladb.bench.remote.sp.VanillaDbSpDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public class VanillaBench {
	private static Logger logger = Logger.getLogger(VanillaBench.class.getName());
	
	private SutDriver driver;
	private Benchmark benchmarker;
	private StatisticMgr statMgr;
	
	public VanillaBench() {
		driver = newDriver();
		benchmarker = newBenchmarker();
		statMgr = newStatisticMgr(benchmarker);
	}

	public void loadTestbed() {
		if (logger.isLoggable(Level.INFO))
			logger.info("loading the testbed of the benchmark...");

		try {
			SutConnection con = getConnection();
			benchmarker.executeLoadingProcedure(con);
		} catch (SQLException e) {
			if (logger.isLoggable(Level.SEVERE))
				logger.severe("Error: " + e.getMessage());
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.INFO))
			logger.info("loading procedure finished.");
	}

	public void benchmark() {
		try {
			if (logger.isLoggable(Level.INFO))
				logger.info("checking the database on the server...");

			SutConnection conn = getConnection();
			boolean result = benchmarker.executeDatabaseCheckProcedure(conn);

			if (!result) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe("the database is not ready, please load the database again.");
				return;
			}

			if (logger.isLoggable(Level.INFO))
				logger.info("database check passed.");

			if (logger.isLoggable(Level.INFO))
				logger.info("creating " + BenchmarkerParameters.NUM_RTES + " emulators...");
			
			int rteCount = benchmarker.getNumOfRTEs();
			RemoteTerminalEmulator<?>[] emulators = new RemoteTerminalEmulator[rteCount];
			emulators[0] = benchmarker.createRte(conn, statMgr); // Reuse the connection
			for (int i = 1; i < emulators.length; i++)
				emulators[i] = benchmarker.createRte(getConnection(), statMgr);

			if (logger.isLoggable(Level.INFO))
				logger.info("waiting for connections...");

			// TODO: Do we still need this ?
			// Wait for connections
			Thread.sleep(1500);

			if (logger.isLoggable(Level.INFO))
				logger.info("start benchmarking.");

			// Start the execution of the RTEs
			for (int i = 0; i < emulators.length; i++)
				emulators[i].start();

			// Waits for the warming up finishes
			Thread.sleep(BenchmarkerParameters.WARM_UP_INTERVAL);

			if (logger.isLoggable(Level.INFO))
				logger.info("warm up period finished.");

			if (BenchmarkerParameters.PROFILING_ON_SERVER) {
				if (logger.isLoggable(Level.INFO))
					logger.info("starting the profiler on the server-side");

				benchmarker.startProfilingProcedure(getConnection());
			}

			if (logger.isLoggable(Level.INFO))
				logger.info("start recording results...");

			// notify RTEs for recording statistics
			for (int i = 0; i < emulators.length; i++)
				emulators[i].startRecordStatistic();

			// waiting
			Thread.sleep(BenchmarkerParameters.BENCHMARK_INTERVAL);

			if (logger.isLoggable(Level.INFO))
				logger.info("benchmark preiod finished. Stoping RTEs...");

			// benchmark finished
			for (int i = 0; i < emulators.length; i++)
				emulators[i].stopBenchmark();

			if (BenchmarkerParameters.PROFILING_ON_SERVER) {
				if (logger.isLoggable(Level.INFO))
					logger.info("stoping the profiler on the server-side");

				benchmarker.stopProfilingProcedure(getConnection());
			}

			// TODO: Do we need to 'join' ?
			// for (int i = 0; i < emulators.length; i++)
			// emulators[i].join();

			// Create a report
			statMgr.outputReport();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			if (logger.isLoggable(Level.SEVERE))
				logger.severe("Error: " + e.getMessage());
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.INFO))
			logger.info("benchmark process finished.");
	}
	
	private SutDriver newDriver() {
		// Create a driver for connection
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			return new VanillaDbJdbcDriver();
		case SP:
			return new VanillaDbSpDriver();
		}
		return null;
	}
	
	private Benchmark newBenchmarker() {
		switch (BenchmarkerParameters.BENCH_TYPE) {
		case MICRO:
			return new MicroBenchmark();
		case TPCC:
			return new TpccBenchmark();
		case TPCE:
			return new TpceBenchmark();
		case YCSB:
			return new YcsbBenchmark();
		}
		return null;
	}
	
	private StatisticMgr newStatisticMgr(Benchmark benchmarker) {
		Set<BenchTransactionType> txnTypes = benchmarker.getBenchmarkingTxTypes();
		String reportPostfix = benchmarker.getBenchmarkName();
		return new StatisticMgr(txnTypes, reportPostfix);
	}
	
	private SutConnection getConnection() throws SQLException {
		return driver.connectToSut();
	}
}
