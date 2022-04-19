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
package org.vanilladb.bench.server.procedure.micro;

import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class MicroTxnProc extends StoredProcedure<MicroTxnSpHelper> {

	public MicroTxnProc() {
		super(new MicroTxnSpHelper());
	}

	@Override
	protected void executeSql() {
		MicroTxnSpHelper helper = getHelper();
		Transaction tx = getTransaction();
		
		// SELECT
		for (int idx = 0; idx < helper.getReadCount(); idx++) {
			int iid = helper.getReadItemId(idx);
			Scan s = StoredProcedureUtils.executeQuery(
				"SELECT i_name, i_price FROM item WHERE i_id = " + iid,
				tx
			);
			s.beforeFirst();
			if (s.next()) {
				String name = (String) s.getVal("i_name").asJavaVal();
				double price = (Double) s.getVal("i_price").asJavaVal();

				helper.setItemName(name, idx);
				helper.setItemPrice(price, idx);
			} else
				throw new RuntimeException("Cloud not find item record with i_id = " + iid);

			s.close();
		}
		
		// UPDATE
		for (int idx = 0; idx < helper.getWriteCount(); idx++) {
			int iid = helper.getWriteItemId(idx);
			double newPrice = helper.getNewItemPrice(idx);
			StoredProcedureUtils.executeUpdate(
				"UPDATE item SET i_price = " + newPrice + " WHERE i_id =" + iid,
				tx
			);
		}
	}
}
