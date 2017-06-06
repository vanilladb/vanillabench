package org.vanilladb.bench.remote.sp;

import java.sql.SQLException;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutDriver;
import org.vanilladb.bench.util.BenchProperties;
import org.vanilladb.core.remote.storedprocedure.SpDriver;

public class VanillaDbSpDriver implements SutDriver {

	private static final String SERVER_IP;

	static {
		SERVER_IP = BenchProperties.getLoader().getPropertyAsString(
				VanillaDbSpDriver.class.getName() + ".SERVER_IP", "127.0.0.1");
	}
	
	@Override
	public SutConnection connectToSut() throws SQLException {
		SpDriver driver = new SpDriver();
		return new VanillaDbSpConnection(driver.connect(SERVER_IP, null));
	}
}
