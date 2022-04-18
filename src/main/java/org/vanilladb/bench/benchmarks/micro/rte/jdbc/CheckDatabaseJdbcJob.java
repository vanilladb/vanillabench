/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.vanilladb.bench.benchmarks.micro.rte.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;
import org.vanilladb.bench.server.param.micro.MicroTestbedLoaderSpParamHelper;

public class CheckDatabaseJdbcJob implements JdbcJob {
	private static Logger logger = Logger.getLogger(CheckDatabaseJdbcJob.class.getName());
	
	MicroTestbedLoaderSpParamHelper paramHelper;

	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		// Parse parameters
		paramHelper = new MicroTestbedLoaderSpParamHelper();
		paramHelper.prepareParameters(pars);
		
		// Execute logic
		try {
			Statement stat = conn.createStatement();
			if (checkItemTable(stat, 1, paramHelper.getNumberOfItems()))
				conn.commit();
			else 
				conn.rollback();
			
			return new VanillaDbJdbcResultSet(true, "Success");
		} catch (Exception e) {
			if (logger.isLoggable(Level.SEVERE))
				logger.warning(e.toString());
			conn.rollback();
			return new VanillaDbJdbcResultSet(false, "");
		}
	}

	private boolean checkItemTable(Statement stat, int startIId, int endIId) throws SQLException {
		if (logger.isLoggable(Level.FINE))
			logger.info("Checking items from i_id=" + startIId + " to i_id=" + endIId);
		
		// Use a bit array to record existence
		int total = endIId - startIId + 1;
		boolean[] checked = new boolean[total];
		for (int i = 0; i < total; i++)
			checked[i] = false;
		
		// Scan the table
		String sql = "SELECT i_id FROM item";
		ResultSet resultSet = stat.executeQuery(sql);
		resultSet.beforeFirst();
		for (int i = startIId, count = 0; i <= endIId; i++) {
			if (!resultSet.next()) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("Only %d records are found (there should be %d records)",
							count, total));
				return false;
			}
			
			int id = resultSet.getInt("i_id");
			if (checked[id - 1]) {
				if (logger.isLoggable(Level.SEVERE))
					logger.severe(String.format("Found duplicated record (i_id = %d)", id));
				return false;
			}
			checked[id - 1] = true;
			count++;
		}
		resultSet.close();

		if (logger.isLoggable(Level.FINE))
			logger.info("Checking items completed.");
		
		return true;
	}
}
