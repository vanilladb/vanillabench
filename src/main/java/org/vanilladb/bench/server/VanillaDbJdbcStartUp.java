package org.vanilladb.bench.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.core.remote.jdbc.JdbcStartUp;
import org.vanilladb.core.server.VanillaDb;

public class VanillaDbJdbcStartUp implements SutStartUp {
	private static Logger logger = Logger.getLogger(VanillaDbJdbcStartUp.class
			.getName());

	public void startup(String[] args) {
		if (logger.isLoggable(Level.INFO))
			logger.info("initing...");

		VanillaDb.init(args[0]);
		if (logger.isLoggable(Level.INFO))
			logger.info("VanillaBench server ready");
		try {
			JdbcStartUp.startUp(1099);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
