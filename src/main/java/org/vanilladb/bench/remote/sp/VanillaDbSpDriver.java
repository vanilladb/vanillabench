package org.vanilladb.bench.remote.sp;

import java.sql.SQLException;

import org.vanilladb.bench.BenchmarkerParameters;
import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.core.remote.storedprocedure.SpDriver;

public class VanillaDbSpDriver implements SutDriver {
	
	@Override
	public SutConnection connectToSut() throws SQLException {
		SpDriver driver = new SpDriver();
		return new VanillaDbSpConnection(driver.connect(BenchmarkerParameters.SERVER_IP, null));
	}
}
