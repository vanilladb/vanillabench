package org.vanilladb.bench.server;

import org.vanilladb.bench.BenchmarkerParameters;

public class StartUp {

	public static void main(String[] args) throws Exception {
		SutStartUp sut = null;
		
		switch (BenchmarkerParameters.CONNECTION_MODE) {
		case JDBC:
			sut = new VanillaDbJdbcStartUp();
			break;
		case SP:
			sut = new VanillaDbSpStartUp();
			break;
		}
		
		if (sut != null)
			sut.startup(args);
	}
}