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
package org.vanilladb.bench.server.param.tpce;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TradeOrderParamHelper extends StoredProcedureParamHelper {
	
	// Inputs
	private long acctId;
	private String coName, execFName, execLName, execTaxId, issue, stPendingId, 
			stSubmittedId, symbol, tradeTypeId;
	private int isLifo, tradeQty, typeIsMargin;
	private double requestedPrice;
	private boolean rollItBack;
	
	// Outputs
	private double buyValue, sellValue, taxAmount;
//	private long tradeId;
	
	// XXX: inputs that are not in the spec.
	protected long customerId;
	protected String customerName;
	protected long brokerId;
	protected double marketPrice;
	protected double tradePrice;
	protected long tradeId;

	@Override
	public void prepareParameters(Object... pars) {
		if (pars.length != 10)
			throw new RuntimeException("wrong pars list");
		acctId = (Long) pars[0];
		customerId = (Long) pars[1];
		brokerId = (Long) pars[2];
		coName = (String) pars[3];
		requestedPrice = (Double) pars[4];
		rollItBack = (Boolean) pars[5];
		symbol = (String) pars[6];
		tradeQty = (Integer) pars[7];
		tradeTypeId = (String) pars[8];
		tradeId = (Long) pars[9];
	}

	@Override
	public SpResultSet createResultSet() {
		Schema sch = new Schema();
		sch.addField("buy_value", Type.DOUBLE);
		sch.addField("sell_value", Type.DOUBLE);
		sch.addField("tax_amount", Type.DOUBLE);
		sch.addField("trade_id", Type.BIGINT);

		SpResultRecord rec = new SpResultRecord();
		rec.setVal("buy_value", new DoubleConstant(buyValue));
		rec.setVal("sell_value", new DoubleConstant(sellValue));
		rec.setVal("tax_amount", new DoubleConstant(taxAmount));
		rec.setVal("trade_id", new BigIntConstant(tradeId));

		return new SpResultSet(isCommitted, sch, rec);
	}

	public long getAcctId() {
		return acctId;
	}

	public int getTradeQty() {
		return tradeQty;
	}

	public String getTradeTypeId() {
		return tradeTypeId;
	}

	public boolean isRollback() {
		return rollItBack;
	}

	public double getRequestedPrice() {
		return requestedPrice;
	}

	public String getCoName() {
		return coName;
	}

	public String getSymbol() {
		return symbol;
	}

	public long getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public double getTradePrice() {
		return tradePrice;
	}
	
	public long getTradeId() {
		return tradeId;
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
		this.tradeId = tradeId;
	}
}
