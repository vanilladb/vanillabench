package org.vanilladb.bench.server.param.tpce;

import org.vanilladb.core.remote.storedprocedure.SpResultSet;
import org.vanilladb.core.sql.BigIntConstant;
import org.vanilladb.core.sql.DoubleConstant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;
import org.vanilladb.core.sql.storedprocedure.SpResultRecord;
import org.vanilladb.core.sql.storedprocedure.StoredProcedureParamHelper;

public class TradeOrderParamHelper extends StoredProcedureParamHelper {
	
	protected long acctId;
	protected int tradeQty;
	protected String tradeTypeId;
	protected boolean rollback;
	protected double requestedPrice;
	protected String coName, sSymb;

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
		rollback = (Boolean) pars[5];
		sSymb = (String) pars[6];
		tradeQty = (Integer) pars[7];
		tradeTypeId = (String) pars[8];
		tradeId = (Long) pars[9];
	}

	@Override
	public SpResultSet createResultSet() {
		Schema sch = new Schema();
		Type statusType = Type.VARCHAR(10);
		Type nameType = Type.VARCHAR(46);
		Type companyNameType = Type.VARCHAR(60);
		Type securitySymbolType = Type.VARCHAR(15);
		Type longType = Type.BIGINT;
		Type doubleType = Type.DOUBLE;

		sch.addField("c_id", longType);
		sch.addField("c_name", nameType);
		sch.addField("ac_id", longType);
		sch.addField("b_id", longType);
		sch.addField("co_name", companyNameType);
		sch.addField("s_symbol", securitySymbolType);
		sch.addField("mrkt_price", doubleType);
		sch.addField("t_id", longType);
		sch.addField("trade_price", doubleType);
		sch.addField("status", Type.VARCHAR(10));

		SpResultRecord rec = new SpResultRecord();
		String status = isCommitted ? "committed" : "abort";
		rec.setVal("c_id", new BigIntConstant(customerId));
		rec.setVal("c_name", new VarcharConstant(customerName));
		rec.setVal("ac_id", new BigIntConstant(acctId));
		rec.setVal("b_id", new BigIntConstant(brokerId));
		rec.setVal("co_name", new VarcharConstant(coName));
		rec.setVal("s_symbol", new VarcharConstant(sSymb));
		rec.setVal("mrkt_price", new DoubleConstant(marketPrice));
		rec.setVal("trade_price", new DoubleConstant(tradePrice));
		rec.setVal("t_id", new BigIntConstant(tradeId));
		rec.setVal("status", new VarcharConstant(status, statusType));

		return new SpResultSet(sch, rec);
	}

	public long getAcctId() {
		return acctId;
	}

	public void setAcctId(long acctId) {
		this.acctId = acctId;
	}

	public int getTradeQty() {
		return tradeQty;
	}

	public void setTradeQty(int tradeQty) {
		this.tradeQty = tradeQty;
	}

	public String getTradeTypeId() {
		return tradeTypeId;
	}

	public void setTradeTypeId(String tradeTypeId) {
		this.tradeTypeId = tradeTypeId;
	}

	public boolean isRollback() {
		return rollback;
	}

	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}

	public double getRequestedPrice() {
		return requestedPrice;
	}

	public void setRequestedPrice(double requestedPrice) {
		this.requestedPrice = requestedPrice;
	}

	public String getCoName() {
		return coName;
	}

	public void setCoName(String coName) {
		this.coName = coName;
	}

	public String getsSymb() {
		return sSymb;
	}

	public void setsSymb(String sSymb) {
		this.sSymb = sSymb;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(long brokerId) {
		this.brokerId = brokerId;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(double tradePrice) {
		this.tradePrice = tradePrice;
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
	}
}
