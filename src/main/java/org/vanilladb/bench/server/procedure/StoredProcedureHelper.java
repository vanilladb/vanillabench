/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
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
package org.vanilladb.bench.server.procedure;

import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.storage.tx.Transaction;

public class StoredProcedureHelper {
	
	public static Scan executeQuery(String sql, Transaction tx) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		Scan s = p.open();
		
		s.beforeFirst();
		if (s.next()) {
			return s;
		} else
			throw new RuntimeException("Query: '" + sql + "' fails.");
	}
	
	public static int executeUpdate(String sql, Transaction tx) {
		return VanillaDb.newPlanner().executeUpdate(sql, tx);
	}
}
