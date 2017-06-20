package org.vanilladb.bench;

import org.vanilladb.bench.micro.MicroBenchmarker;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.remote.sp.VanillaDbSpDriver;
import org.vanilladb.bench.tpcc.TpccBenchmarker;

public class App {
	
	private static int action;
	
	public static void main(String[] args) {
		Benchmarker benchmarker = null;
		
		try {
			parseArguments(args);
		} catch (IllegalArgumentException e) {
			System.err.println("Error: " + e.getMessage());
			System.err.println("Usage: ./app [Action]");
		}
		
		// Create a driver for connection
		SutDriver driver = null;
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			// TODO: There is no JDBC driver for now
			throw new UnsupportedOperationException("No JDBC implementation");
		case SP:
			driver = new VanillaDbSpDriver();
			break;
		}
		
		// Create a benchmarker
		switch (BenchmarkerParameters.BENCH_TYPE) {
		case MICRO:
			benchmarker = new MicroBenchmarker(driver);
			break;
		case TPCC:
			benchmarker = new TpccBenchmarker(driver);
			break;
		case TPCE:
			throw new UnsupportedOperationException("No TPC-E for now");
		}
		
		switch (action) {
		case 1: // Load testbed
			benchmarker.loadTestbed();
			break;
		case 2: // Benchmarking
			benchmarker.benchmark();
			break;
		case 3: // Persist
			benchmarker.persist();
			break;
		}
	}
	
	private static void parseArguments(String[] args) throws IllegalArgumentException {
		if (args.length < 1) {
			throw new IllegalArgumentException("The number of arguments is less than 1");
		}
		
		try {
			action = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(String.format("'%s' is not a number", args[0]));
		}
	}
}
