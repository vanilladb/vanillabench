package org.vanilladb.bench.benchmarks.tpcc;

import org.vanilladb.bench.util.BenchProperties;

/** Holds TPC-C constants. */

public class TpccConstants {

	// Scaling parameters
	public static final int NUM_WAREHOUSES;
	public static final int NUM_ITEMS = 100000;
	public static final int DISTRICTS_PER_WAREHOUSE = 10;
	public static final int CUSTOMERS_PER_DISTRICT = 3000;
	/*
	 * WARNNING: if the customer number less than 1000, system can not generate
	 * valid constant for NURand().
	 */
	public static final int NEW_ORDERS_PER_DISTRICT = 3000;
	public static final int NEW_ORDER_START_ID = (int) (NEW_ORDERS_PER_DISTRICT * ((double) 2101 / 3000));
	public static final int NUM_DISTINCT_CLAST = (int) NEW_ORDERS_PER_DISTRICT / 3;

	static {
		NUM_WAREHOUSES = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".NUM_WAREHOUSES", 1);
	}

	// 9 tables's names
	public static final String TABLENAME_WAREHOUSE = "warehouse";
	public static final String TABLENAME_DISTRICT = "district";
	public static final String TABLENAME_ITEM = "item";
	public static final String TABLENAME_CUSTOMER = "customer";
	public static final String TABLENAME_HISTORY = "history";
	public static final String TABLENAME_STOCK = "stock";
	public static final String TABLENAME_ORDERS = "orders";
	public static final String TABLENAME_NEW_ORDER = "new_order";
	public static final String TABLENAME_ORDER_LINE = "order_line";
	public static final String TABLENAMES[] = { TABLENAME_WAREHOUSE,
			TABLENAME_DISTRICT, TABLENAME_ITEM, TABLENAME_CUSTOMER,
			TABLENAME_HISTORY, TABLENAME_STOCK, TABLENAME_ORDERS,
			TABLENAME_NEW_ORDER, TABLENAME_ORDER_LINE, };

	// Transaction frequency follows the mixture requirement
	public static final int FREQUENCY_TOTAL;
	public static final int FREQUENCY_NEW_ORDER;
	public static final int FREQUENCY_PAYMENT;
	public static final int FREQUENCY_ORDER_STATUS;
	public static final int FREQUENCY_DELIVERY;
	public static final int FREQUENCY_STOCK_LEVEL;
	static {
		FREQUENCY_TOTAL = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".FREQUENCY_TOTAL", 100);
		FREQUENCY_NEW_ORDER = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".FREQUENCY_NEW_ORDER", 45);
		FREQUENCY_PAYMENT = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".FREQUENCY_PAYMENT", 43);
		FREQUENCY_ORDER_STATUS = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".FREQUENCY_ORDER_STATUS", 4);
		FREQUENCY_DELIVERY = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".FREQUENCY_DELIVERY", 4);
		FREQUENCY_STOCK_LEVEL = BenchProperties.getLoader().getPropertyAsInteger(
				TpccConstants.class.getName() + ".FREQUENCY_STOCK_LEVEL", 4);
	}

	// Range for uniformly selecting transaction type
	public static final int RANGE_NEW_ORDER = FREQUENCY_NEW_ORDER;
	public static final int RANGE_PAYMENT = RANGE_NEW_ORDER + FREQUENCY_PAYMENT;
	public static final int RANGE_ORDER_STATUS = RANGE_PAYMENT
			+ FREQUENCY_ORDER_STATUS;
	public static final int RANGE_DELIVERY = RANGE_ORDER_STATUS
			+ FREQUENCY_DELIVERY;
	public static final int RANGE_STOCK_LEVEL = RANGE_DELIVERY
			+ FREQUENCY_STOCK_LEVEL;

	// Minimal keying time in second
	public static final int KEYING_NEW_ORDER = 18;
	public static final int KEYING_PAYMENT = 3;
	public static final int KEYING_ORDER_STATUS = 2;
	public static final int KEYING_DELIVERY = 2;
	public static final int KEYING_STOCK_LEVEL = 2;

	// Minimal mean think time in second
	public static final int THINKTIME_NEW_ORDER = 12;
	public static final int THINKTIME_PAYMENT = 12;
	public static final int THINKTIME_ORDER_STATUS = 10;
	public static final int THINKTIME_DELIVERY = 5;
	public static final int THINKTIME_STOCK_LEVEL = 5;

	// Item constants
	// public static final int NUM_ITEMS = 100000; allow dynamic changing
	public static final int MIN_IM = 1;
	public static final int MAX_IM = 10000;
	public static final double MIN_PRICE = 1.00;
	public static final double MAX_PRICE = 100.00;
	public static final int MIN_I_NAME = 14;
	public static final int MAX_I_NAME = 24;
	public static final int MIN_I_DATA = 26;
	public static final int MAX_I_DATA = 50;

	// Warehouse constants
	public static final double MIN_TAX = 0;
	public static final double MAX_TAX = 0.2000;
	public static final int TAX_DECIMALS = 4;
	public static final double INITIAL_W_YTD = 300000.00;
	public static final int MIN_NAME = 6;
	public static final int MAX_NAME = 10;
	public static final int MIN_STREET = 10;
	public static final int MAX_STREET = 20;
	public static final int MIN_CITY = 10;
	public static final int MAX_CITY = 20;
	public static final int STATE = 2;
	public static final int ZIP_LENGTH = 9;
	public static final String ZIP_SUFFIX = "11111";

	// Stock constants
	public static final int MIN_QUANTITY = 10;
	public static final int MAX_QUANTITY = 100;
	public static final int DIST = 24;
	public static final int STOCK_PER_WAREHOUSE = 100000;

	// District constants
	// public static final int DISTRICTS_PER_WAREHOUSE = 10;allow dynamic
	// changing
	public static final double INITIAL_D_YTD = 30000.00; // different from
															// Warehouse
	public static final int INITIAL_NEXT_O_ID = 3001;

	// Customer constants
	// public static final int CUSTOMERS_PER_DISTRICT = 3000; allow dynamic
	// changing
	public static final double INITIAL_CREDIT_LIM = 50000.00;
	public static final double MIN_DISCOUNT = 0.0000;
	public static final double MAX_DISCOUNT = 0.5000;
	public static final int DISCOUNT_DECIMALS = 4;
	public static final double INITIAL_BALANCE = -10.00;
	public static final double INITIAL_YTD_PAYMENT = 10.00;
	public static final int INITIAL_PAYMENT_CNT = 1;
	public static final int INITIAL_DELIVERY_CNT = 0;
	public static final int MIN_FIRST = 6;
	public static final int MAX_FIRST = 10;
	public static final String MIDDLE = "OE";
	public static final int PHONE = 16;
	public static final int MIN_C_DATA = 300;
	public static final int MAX_C_DATA = 500;
	public static final String GOOD_CREDIT = "GC";
	public static final String BAD_CREDIT = "BC";
	public static final byte[] BAD_CREDIT_BYTES = BAD_CREDIT.getBytes();

	// Order constants
	public static final int MIN_CARRIER_ID = 1;
	public static final int MAX_CARRIER_ID = 10;
	// HACK: This is not strictly correct, but it works
	public static final int NULL_CARRIER_ID = Integer.MAX_VALUE;
	public static final long NULL_DELIVERY_DATE = Long.MIN_VALUE;
	public static final int MONEY_DECIMALS = 2;
	// o_id < than this value, carrier != null, >= -> carrier == null
	public static final int NULL_CARRIER_LOWER_BOUND = 2101;
	public static final int MIN_OL_CNT = 5;
	public static final int MAX_OL_CNT = 15;
	public static final int INITIAL_ALL_LOCAL = 1;
	public static final int INITIAL_ORDERS_PER_DISTRICT = 3000;
	// Used to generate new order transactions
	public static final int MAX_OL_QUANTITY = 10;

	// Order line constants
	public static final int INITIAL_QUANTITY = 5;
	public static final double MIN_AMOUNT = 0.01;

	// History constants
	public static final int MIN_DATA = 12;
	public static final int MAX_DATA = 24;
	public static final double INITIAL_AMOUNT = 10.00f;

	// New order constants
	public static final int INITIAL_NEW_ORDERS_PER_DISTRICT = 900;

	// TPC-C 2.4.3.4 (page 31) says this must be displayed when new order rolls
	// back.
	public static final String INVALID_ITEM_MESSAGE = "Item number is not valid";

	// Status message used by delivery txn
	public static final String QUEUED_MESSAGE = "Delivery has been queued";

	// Used to generate stock level transactions
	public static final int MIN_STOCK_LEVEL_THRESHOLD = 10;
	public static final int MAX_STOCK_LEVEL_THRESHOLD = 20;

	// Payment txn constant
	public static final double MIN_PAYMENT = 1.0;
	public static final double MAX_PAYMENT = 5000.0;

	// Indicates "brand" items and stock in i_data and s_data.
	public static final String ORIGINAL_STRING = "ORIGINAL";
	public static final byte[] ORIGINAL_BYTES = ORIGINAL_STRING.getBytes();

}
