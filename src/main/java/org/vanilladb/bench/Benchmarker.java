package org.vanilladb.bench;

import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.rte.RemoteTerminalEmulator;

public abstract class Benchmarker {
	private static Logger logger = Logger.getLogger(Benchmarker.class
			.getName());
	
	private StatisticMgr statMgr;
	private SutDriver driver;
	
	public Benchmarker(SutDriver sutDriver) {
		statMgr = new StatisticMgr(getBenchmarkingTxTypes());
		driver = sutDriver;
	}
	
	public abstract Set<TransactionType> getBenchmarkingTxTypes();
	
	protected abstract void executeLoadingProcedure(SutConnection conn) throws SQLException;
	
	protected abstract void startProfilingProcedure(SutConnection conn) throws SQLException;
	
	protected abstract void stopProfilingProcedure(SutConnection conn) throws SQLException;
	
	protected abstract RemoteTerminalEmulator createRte(SutConnection conn, StatisticMgr statMgr);
		
	public void loadTestbed() {
		if (logger.isLoggable(Level.INFO))
			logger.info("loading the testbed of tpcc benchmark...");
		
		try {
			SutConnection con = getConnection();
			executeLoadingProcedure(con);
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
				logger.info("creating " + BenchmarkerParameters.NUM_RTES + " emulators...");
			
			RemoteTerminalEmulator[] emulators = new RemoteTerminalEmulator[BenchmarkerParameters.NUM_RTES];
			for (int i = 0; i < emulators.length; i++)
				emulators[i] = createRte(getConnection(), statMgr);
			
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
				
				startProfilingProcedure(getConnection());
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
				
				stopProfilingProcedure(getConnection());
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
	
	public void persist() {
		if (logger.isLoggable(Level.INFO))
			logger.info("persisting data structures in NVM...");
		
		try {
			SutConnection con = getConnection();
			executePersistingProcedure(con);
		} catch (SQLException e) {
			if (logger.isLoggable(Level.SEVERE))
				logger.severe("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		if (logger.isLoggable(Level.INFO))
			logger.info("persisting procedure finished.");
	}
	
	private SutConnection getConnection() throws SQLException {
		return driver.connectToSut();
	}
	
	protected void executePersistingProcedure(SutConnection conn) throws SQLException {
	
	}

}
