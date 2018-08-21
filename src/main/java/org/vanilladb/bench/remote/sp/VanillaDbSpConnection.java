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
package org.vanilladb.bench.remote.sp;

import java.sql.Connection;
import java.sql.SQLException;

import org.vanilladb.bench.remote.SutConnection;
import org.vanilladb.bench.remote.SutResultSet;
import org.vanilladb.core.remote.storedprocedure.SpConnection;
import org.vanilladb.core.remote.storedprocedure.SpResultSet;

public class VanillaDbSpConnection implements SutConnection {
	private SpConnection conn;

	public VanillaDbSpConnection(SpConnection conn) {
		this.conn = conn;
	}

	@Override
	public SutResultSet callStoredProc(int pid, Object... pars) throws SQLException {
		SpResultSet r = conn.callStoredProc(pid, pars);
		return new VanillaDbSpResultSet(r);
	}
	
	@Override
	public Connection toJdbcConnection() {
		throw new RuntimeException("cannot convert a stored procedure connection to a JDBC connection");
	}
}
