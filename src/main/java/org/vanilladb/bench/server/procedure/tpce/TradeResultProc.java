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
package org.vanilladb.bench.server.procedure.tpce;

import org.vanilladb.bench.server.param.tpce.TradeResultSpHelper;
import org.vanilladb.bench.server.procedure.StoredProcedureUtils;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.sql.storedprocedure.StoredProcedure;
import org.vanilladb.core.storage.tx.Transaction;

public class TradeResultProc extends StoredProcedure<TradeResultSpHelper> {
	
	public TradeResultProc() {
		super(new TradeResultSpHelper());
	}

	@Override
	protected void executeSql() {
		TradeResultSpHelper paramHelper = getParamHelper();
		Transaction tx = getTransaction();
		
		// SELECT ca_name, ca_b_id, ca_c_id FROM customer_account WHERE
		// ca_id = acctId
		String sql = "SELECT ca_name, ca_b_id, ca_c_id FROM customer_account WHERE "
				+ "ca_id = " + paramHelper.getAcctId();
		Scan s = StoredProcedureUtils.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		s.getVal("ca_name").asJavaVal();
		s.getVal("ca_b_id").asJavaVal();
		s.getVal("ca_c_id").asJavaVal();

		// SELECT c_name FROM customer WHERE c_id = customerKey
		sql = "SELECT c_f_name FROM customer WHERE c_id = " + paramHelper.getCustomerId();
		s = StoredProcedureUtils.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		s.getVal("c_f_name").asJavaVal();

		// SELECT b_name FROM broker WHERE b_id = brokerId
		sql = "SELECT b_name FROM broker WHERE b_id = " + paramHelper.getBrokerId();
		s = StoredProcedureUtils.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		s.getVal("b_name").asJavaVal();

		// SELECT t_trade_price FROM trade WHERE t_id = tradeId
		sql = "SELECT t_trade_price FROM trade WHERE t_id = " + paramHelper.getTradeId();
		s = StoredProcedureUtils.executeQuery(sql, tx);
		s.beforeFirst();
		if (!s.next())
			throw new RuntimeException("Executing '" + sql + "' fails");
		s.getVal("t_trade_price").asJavaVal();

		// INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES (...)
		long currentTime = System.currentTimeMillis();
		sql = String.format("INSERT INTO trade_history (th_t_id, th_dts, th_st_id) VALUES "
				+ "(%d, %d, '%s')",  paramHelper.getTradeId(), currentTime, "A");
		StoredProcedureUtils.executeUpdate(sql, tx);

		// UPDATE customer_account SET ca_bal = ca_bal + tradePrice WHERE
		// ca_id = acctId
		sql = String.format("UPDATE customer_account SET ca_bal = %f WHERE ca_id = %d", 
				1000.0, paramHelper.getAcctId());
		StoredProcedureUtils.executeUpdate(sql, tx);
	}
}
