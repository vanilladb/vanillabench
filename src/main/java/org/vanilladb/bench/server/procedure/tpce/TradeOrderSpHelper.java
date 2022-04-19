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

import org.vanilladb.bench.benchmarks.tpce.rte.TradeOrderParamHelper;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureHelper;

public class TradeOrderSpHelper extends TradeOrderParamHelper
		implements StoredProcedureHelper {
	
	// Outputs
	private double buyValue, sellValue, taxAmount;
	private long newTradeId;

	@Override
	public void prepareParameters(Object... pars) {
		unpackParameters(pars);
	}

	@Override
	public Schema getResultSetSchema() {
		Schema sch = new Schema();
		sch.addField("buy_value", Type.DOUBLE);
		sch.addField("sell_value", Type.DOUBLE);
		sch.addField("tax_amount", Type.DOUBLE);
		sch.addField("trade_id", Type.BIGINT);
		return sch;
	}

	@Override
	public SpResultRecord newResultSetRecord() {
		SpResultRecord rec = new SpResultRecord();
		rec.setVal("buy_value", new DoubleConstant(buyValue));
		rec.setVal("sell_value", new DoubleConstant(sellValue));
		rec.setVal("tax_amount", new DoubleConstant(taxAmount));
		rec.setVal("trade_id", new BigIntConstant(newTradeId));
		return rec;
	}

	@Override
	public boolean isReadOnly() {
		return false;
	}
	
	// Set outputs
	
	public void setBuyValue(double buyValue) {
		this.buyValue = buyValue;
	}

	public void setSellValue(double sellValue) {
		this.sellValue = sellValue;
	}
	
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	public void setTradeId(long tradeId) {
		this.newTradeId = tradeId;
	}
}
