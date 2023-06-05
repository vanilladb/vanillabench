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

import org.vanilladb.core.query.algebra.LimitPlan;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.query.algebra.TablePlan;
import org.vanilladb.core.query.algebra.materialize.SortPlan;
import org.vanilladb.core.query.parse.InsertData;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.distfn.DistanceFn;
import org.vanilladb.core.sql.distfn.EuclideanFn;
import org.vanilladb.core.storage.tx.Transaction;

public class StoredProcedureUtils {
	
	public static Scan executeQuery(String sql, Transaction tx) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		return p.open();
	}
	
	public static int executeUpdate(String sql, Transaction tx) {
		return VanillaDb.newPlanner().executeUpdate(sql, tx);
	}

	public static Scan executeCalculateRecall(VectorConstant query, String tableName, String field, int limit, Transaction tx) {
		Plan p = new TablePlan(tableName, tx);

		DistanceFn distFn = new EuclideanFn(field);
		distFn.setQueryVector(query);
		
		p = new SortPlan(p, distFn, tx);
		p = new LimitPlan(p, limit);

		return p.open();
	}

	public static int executeInsert(InsertData sql, Transaction tx) {
		return VanillaDb.newPlanner().executeInsert(sql, tx);
	}
}
