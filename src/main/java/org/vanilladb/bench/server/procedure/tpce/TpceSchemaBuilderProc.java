package org.vanilladb.bench.server.procedure.tpce;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.server.param.tpce.TpceSchemaBuilderParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.server.VanillaDb;

public class TpceSchemaBuilderProc extends BasicStoredProcedure<TpceSchemaBuilderParamHelper> {
	private static Logger logger = Logger.getLogger(TpceSchemaBuilderProc.class.getName());

	public TpceSchemaBuilderProc() {
		super(new TpceSchemaBuilderParamHelper());
	}

	@Override
	protected void executeSql() {
		if (logger.isLoggable(Level.FINE))
			logger.info("Create schema for tpce testbed...");

		for (String cmd : paramHelper.getTableSchemas())
			VanillaDb.newPlanner().executeUpdate(cmd, tx);
		for (String cmd : paramHelper.getIndexSchemas())
			VanillaDb.newPlanner().executeUpdate(cmd, tx);
	}
}