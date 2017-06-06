package org.vanilladb.bench.remote;

import java.sql.SQLException;

public interface SutDriver {

	SutConnection connectToSut() throws SQLException;

}
