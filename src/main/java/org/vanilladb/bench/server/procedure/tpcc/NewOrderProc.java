package org.vanilladb.bench.server.procedure.tpcc;

import org.vanilladb.bench.server.param.tpcc.NewOrderProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.query.algebra.Plan;
import org.vanilladb.core.query.algebra.Scan;
import org.vanilladb.core.server.VanillaDb;

/**
 * Entering a new order is done in a single database transaction with the
 * following steps:<br />
 * 1. Create an order header, comprised of: <br />
 * - 2 row selections with data retrieval <br />
 * - 1 row selections with data retrieval and update<br />
 * - 2 row insertions <br />
 * 2. Order a variable number of items (average ol_cnt = 10), comprised of:
 * <br />
 * - (1 * ol_cnt) row selections with data retrieval <br />
 * - (1 * ol_cnt) row selections with data retrieval and update <br />
 * - (1 * ol_cnt) row insertions <br />
 * 
 * @author yslin
 *
 */
public class NewOrderProc extends BasicStoredProcedure<NewOrderProcParamHelper> {

	public NewOrderProc() {
		super(new NewOrderProcParamHelper());
	}

	@Override
	protected void executeSql() {
		int wid = paramHelper.getWid();
		int did = paramHelper.getDid();
		int cid = paramHelper.getCid();
		int nextOid = -1;
		
		// SELECT w_tax FROM warehouse WHERE w_id = wid
		String sql = "SELECT w_tax FROM warehouse WHERE w_id = " + wid;
		Scan s = executeQuery(sql);
		paramHelper.setWTax((Double) s.getVal("w_tax").asJavaVal());
		s.close();

		// SELECT d_tax, d_next_o_id FROM district WHERE d_w_id = wid AND d_id =
		// did
		sql = "SELECT d_tax, d_next_o_id FROM district WHERE d_w_id = " + wid +
				" AND d_id = " + did;
		s = executeQuery(sql);
		nextOid = (Integer) s.getVal("d_next_o_id").asJavaVal();
		paramHelper.setdTax((Double) s.getVal("d_tax").asJavaVal());
		s.close();
		
		if (nextOid < 0)
			throw new RuntimeException("Something wrong with the next_o_id");

		// UPDATE district SET d_next_o_id = (nextOId + 1) WHERE d_w_id = wid
		// AND d_id = did
		sql = "UPDATE district SET d_next_o_id = " + (nextOid + 1) +
				" WHERE d_w_id = " + wid + " AND d_id = " + did;
		executeUpdate(sql);

		// SELECT c_discount, c_last, c_credit FROM customer WHERE c_w_id = wid
		// AND c_d_id = did AND c_id = cid
		sql = "SELECT c_discount, c_last, c_credit FROM customer WHERE c_w_id = " + wid +
				" AND c_d_id = " + did + " AND c_id = " + cid;
		s = executeQuery(sql);
		paramHelper.setcDiscount((Double) s.getVal("c_discount").asJavaVal());
		paramHelper.setcLast((String) s.getVal("c_last").asJavaVal());
		paramHelper.setcCredit((String) s.getVal("c_credit").asJavaVal());
		s.close();

		// INSERT INTO orders (o_id, o_w_id, o_d_id, o_c_id, o_entry_d,
		// o_carrier_id, o_ol_cnt, o_all_local) VALUES (nextOId, wid,
		// did, cid, currentTime, 0, olCount, isAllLocal)
		int isAllLocal = paramHelper.isAllLocal() ? 1 : 0;
		long oEntryDate = System.currentTimeMillis();
		int olCount = paramHelper.getOlCount();
		paramHelper.setoEntryDate(oEntryDate);
		
		sql = String.format("INSERT INTO orders (o_id, o_w_id, o_d_id, o_c_id, o_entry_d,"
				+ "o_carrier_id, o_ol_cnt, o_all_local) VALUES (%d, %d," +
				"%d, %d, %d, 0, %d, %d)", nextOid, wid, did, cid, oEntryDate,
				olCount, isAllLocal);
		executeUpdate(sql);

		// INSERT INTO new_order (no_o_id, no_d_id, no_w_id) VALUES
		// (nextOId, did, wid)
		sql = String.format("INSERT INTO new_order (no_o_id, no_d_id, no_w_id) VALUES"
				+ " (%d, %d, %d)", nextOid, did, wid);
		executeUpdate(sql);

		// For each order line
		int totalAmount = 0;
		int[][] items = paramHelper.getItems();
		int orderLineCount = paramHelper.getOlCount();
		for (int i = 0; i < orderLineCount; i++) {
			int olIId = items[i][0];
			int olSupplyWId = items[i][1];
			int olQuantity = items[i][2];

			// SELECT i_price, i_name, i_data FROM item WHERE i_id = olIId
			sql = "SELECT i_price, i_name, i_data FROM item WHERE i_id = " + olIId;
			s = executeQuery(sql);
			// TODO: save i_price, i_name, i_data
			double iPrice = (Double) s.getVal("i_price").asJavaVal();
			s.getVal("i_name").asJavaVal();
			s.getVal("i_data").asJavaVal();
			s.close();

			// SELECT s_quantity, sDistXX, s_data, s_ytd, s_order_cnt FROM
			// stock WHERE s_i_id = olIId AND s_w_id = olSupplyWId
			String sDistXX;
			if (paramHelper.getDid() == 10)
				sDistXX = "s_dist_10";
			else
				sDistXX = "s_dist_0" + paramHelper.getDid();
			
			sql = "SELECT s_quantity, " + sDistXX +
					", s_data, s_ytd, s_order_cnt FROM stock WHERE s_i_id = " + olIId +
					" AND s_w_id = " + olSupplyWId;
			s = executeQuery(sql);
			// TODO: save sDistXX, s_data
			int sQuantity = (Integer) s.getVal("s_quantity").asJavaVal();
			String sDistInfo = (String) s.getVal(sDistXX).asJavaVal();
			s.getVal("s_data").asJavaVal();
			int sYtd = (Integer) s.getVal("s_ytd").asJavaVal();
			int sOrderCnt = (Integer) s.getVal("s_order_cnt").asJavaVal();
			s.close();

			// UPDATE stock SET s_quantity = ..., s_ytd = s_ytd + ol_quantitity,
			// s_order_cnt = s_order_cnt + 1 WHERE s_i_id = olIId AND
			// s_w_id = olSupplyWId
			sQuantity -= olQuantity;
			if (sQuantity < 10)
				sQuantity += 91;
			sYtd += olQuantity;
			sOrderCnt++;
			
			sql = String.format("UPDATE stock SET s_quantity = %d, s_ytd = %d, " +
					"s_order_cnt = %d WHERE s_i_id = %d AND s_w_id = %d",
					sQuantity, sYtd, sOrderCnt, olIId, olSupplyWId);
			executeUpdate(sql);
			
			// INSERT INTO order_line (ol_o_id, ol_d_id, ol_w_id,
			// ol_number,ol_i_id, ol_supply_w_id, ol_delivery_d,
			// ol_quantity, ol_amount, ol_dist_info) VALUES (
			// nextOId, did, wid, i, olIId, olSupplyWid, NULL, olQuantity,
			// DoublePlainPrinter.toPlainString(olAmount), sDistInfo)
			double olAmount = olQuantity * iPrice;
			
			sql = String.format("INSERT INTO order_line (ol_o_id, ol_d_id, ol_w_id, " +
					"ol_number,ol_i_id, ol_supply_w_id, ol_delivery_d, " +
					"ol_quantity, ol_amount, ol_dist_info) VALUES (" +
					"%d, %d, %d, %d, %d, %d, -1, %d, %f, '%s')",
					nextOid, did, wid, i, olIId, olSupplyWId, olQuantity,
					olAmount, sDistInfo);
			executeUpdate(sql);

			// record amounts
			totalAmount += olAmount;
		}
		paramHelper.setTotalAmount(
				totalAmount * (1 - paramHelper.getcDiscount()) * (1 + paramHelper.getwTax() + paramHelper.getdTax()));

	}
	
	private Scan executeQuery(String sql) {
		Plan p = VanillaDb.newPlanner().createQueryPlan(sql, tx);
		Scan s = p.open();
		s.beforeFirst();
		if (s.next()) {
			return s;
		} else
			throw new RuntimeException("Query: " + sql + " fails.");
	}
	
	private void executeUpdate(String sql) {
		int count = VanillaDb.newPlanner().executeUpdate(sql, tx);
		
		if (count > 1)
			throw new RuntimeException("Update: " + sql + " affect more than 1 record.");
		else if (count < 1)
			throw new RuntimeException("Update: " + sql + " fails.");
	}
}
