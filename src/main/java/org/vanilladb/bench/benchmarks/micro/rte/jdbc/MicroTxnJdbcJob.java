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

import org.vanilladb.bench.benchmarks.micro.rte.MicroTxnParamHelper;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.bench.remote.jdbc.VanillaDbJdbcResultSet;
import org.vanilladb.bench.rte.jdbc.JdbcJob;

public class MicroTxnJdbcJob implements JdbcJob {
	private static Logger logger = Logger.getLogger(MicroTxnJdbcJob.class
			.getName());
	
	@Override
	public SutResultSet execute(Connection conn, Object[] pars) throws SQLException {
		MicroTxnParamHelper paramHelper = new MicroTxnParamHelper();
		paramHelper.unpackParameters(pars);
		
		// Output message
		StringBuilder outputMsg = new StringBuilder("[");
		
		if (paramHelper.getWriteCount() == 0)
			conn.setReadOnly(true);
		else
			conn.setReadOnly(false);
		
		// Execute logic
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = null;
			
			// SELECT
			for (int i = 0; i < paramHelper.getReadCount(); i++) {
				int iid = paramHelper.getReadItemId(i);
				String sql = "SELECT i_name FROM item WHERE i_id = " + iid;
				rs = statement.executeQuery(sql);
				rs.beforeFirst();
				if (rs.next()) {
					outputMsg.append(String.format("'%s', ", rs.getString("i_name")));
				} else
					throw new RuntimeException("cannot find the record with i_id = " + iid);
				rs.close();
			}
			
			// UPDATE
			for (int i = 0; i < paramHelper.getWriteCount(); i++) {
				int iid = paramHelper.getWriteItemId(i);
				double newPrice = paramHelper.getNewItemPrice(i);
				String sql = "UPDATE item SET i_price = " + newPrice + " WHERE i_id =" + iid;
				int count = statement.executeUpdate(sql);
				
				if (count < 1)
					throw new RuntimeException("cannot update the record with i_id = " + iid);
			}
			
			conn.commit();
			
			outputMsg.deleteCharAt(outputMsg.length() - 2);
			outputMsg.append("]");
			
			return new VanillaDbJdbcResultSet(true, outputMsg.toString());
		} catch (Exception e) {
			if (logger.isLoggable(Level.WARNING))
				logger.warning(e.toString());
			return new VanillaDbJdbcResultSet(false, "");
		}
	}
}
