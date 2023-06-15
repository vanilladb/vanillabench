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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import static org.vanilladb.core.sql.RecordComparator.DIR_DESC;

import org.vanilladb.core.query.algebra.LimitPlan;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.query.algebra.TablePlan;
import org.vanilladb.core.query.algebra.materialize.SortPlan;
import org.vanilladb.core.query.parse.InsertData;
import org.vanilladb.core.query.parse.VectorEmbeddingData;
import org.vanilladb.core.server.VanillaDb;
import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.Record;
import org.vanilladb.core.sql.RecordComparator;
import org.vanilladb.core.sql.VectorConstant;
import org.vanilladb.core.sql.distfn.EuclideanFn;
import org.vanilladb.core.storage.index.IndexType;
import org.vanilladb.core.storage.index.ivf.IVFIndex;
import org.vanilladb.core.storage.metadata.index.IndexInfo;
import org.vanilladb.core.storage.tx.Transaction;

public class StoredProcedureUtils {
	
	public static Scan executeQuery(String sql, Transaction tx) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		return p.open();
	}
	
	public static int executeUpdate(String sql, Transaction tx) {
		return VanillaDb.newPlanner().executeUpdate(sql, tx);
	}

	static class MapRecord implements Record {

		Map<String, Constant> fldVals = new HashMap<>();

		@Override
		public Constant getVal(String fldName) {
			return fldVals.get(fldName);
		}

		public void put(String fldName, Constant val) {
			fldVals.put(fldName, val);
		}

		public boolean containsKey(String fldName) {
			return fldVals.containsKey(fldName);
		}
	}

	static class PriorityQueueScan implements Scan {
		private PriorityQueue<MapRecord> pq;
		private boolean isBeforeFirsted = false;

        public PriorityQueueScan(PriorityQueue<MapRecord> pq) {
            this.pq = pq;
        }

        @Override
        public Constant getVal(String fldName) {
            return pq.peek().getVal(fldName);
        }

        @Override
        public void beforeFirst() {
            this.isBeforeFirsted = true;
        }

        @Override
        public boolean next() {
            if (isBeforeFirsted) {
                isBeforeFirsted = false;
                return true;
            }
            pq.poll();
            return pq.size() > 0;
        }

        @Override
        public void close() {
            return;
        }

        @Override
        public boolean hasField(String fldName) {
            return pq.peek().containsKey(fldName);
        }
	}


	public static Scan executeCalculateRecall(VectorConstant query, String tableName, String field, int limit, Transaction tx) {
		Plan p = new TablePlan(tableName, tx);
		
		VectorEmbeddingData vec = new VectorEmbeddingData(query, field, new EuclideanFn());

		List<String> sortFlds = new ArrayList<String>();
		sortFlds.add(field);
		
		List<Integer> sortDirs = new ArrayList<Integer>();
		sortDirs.add(DIR_DESC); // for priority queue

		RecordComparator comp = new RecordComparator(sortFlds, sortDirs, vec);
		PriorityQueue<MapRecord> pq = new PriorityQueue<>(limit, (MapRecord r1, MapRecord r2) -> comp.compare(r1, r2));
		Scan s = p.open();
		s.beforeFirst();
		while (s.next()) {
			MapRecord fldVals = new MapRecord();
			for (String fldName : p.schema().fields()) {
				fldVals.put(fldName, s.getVal(fldName));
			}
			pq.add(fldVals);
			if (pq.size() > limit)
				pq.poll();
		}
		s.close();
		return new PriorityQueueScan(pq);
	}

	public static int executeInsert(InsertData sql, Transaction tx) {
		return VanillaDb.newPlanner().executeInsert(sql, tx);
	}

	public static void executeTrainIndex(String tblName, List<String> fldNames, String idxName, Transaction tx) {
		TablePlan tp = new TablePlan(tblName, tx);
		IndexInfo ii = new IndexInfo(idxName, tblName, fldNames, IndexType.IVF);
		IVFIndex.train(ii, tp, fldNames.get(0), new EuclideanFn(), tx);
	}
}
