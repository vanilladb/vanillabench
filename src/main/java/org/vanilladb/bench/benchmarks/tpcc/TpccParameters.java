package org.vanilladb.bench.benchmarks.tpcc;

import org.vanilladb.bench.util.BenchProperties;

public class TpccParameters {

	// Scaling parameters
	public static final int NUM_WAREHOUSES;
	
	public static final boolean ENABLE_THINK_AND_KEYING_TIME;

	// Transaction frequency follows the mixture requirement
	public static final int FREQUENCY_TOTAL;
	public static final int FREQUENCY_NEW_ORDER;
	public static final int FREQUENCY_PAYMENT;
	public static final int FREQUENCY_ORDER_STATUS;
	public static final int FREQUENCY_DELIVERY;
	public static final int FREQUENCY_STOCK_LEVEL;
	
	static {
		NUM_WAREHOUSES = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".NUM_WAREHOUSES", 1);
		
		ENABLE_THINK_AND_KEYING_TIME = BenchProperties.getLoader()
				.getPropertyAsBoolean(TpccParameters.class.getName() +
						".ENABLE_THINK_AND_KEYING_TIME", false);
		
		FREQUENCY_TOTAL = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".FREQUENCY_TOTAL", 100);
		FREQUENCY_NEW_ORDER = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".FREQUENCY_NEW_ORDER", 45);
		FREQUENCY_PAYMENT = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".FREQUENCY_PAYMENT", 43);
		FREQUENCY_ORDER_STATUS = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".FREQUENCY_ORDER_STATUS", 4);
		FREQUENCY_DELIVERY = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".FREQUENCY_DELIVERY", 4);
		FREQUENCY_STOCK_LEVEL = BenchProperties.getLoader().getPropertyAsInteger(
				TpccParameters.class.getName() + ".FREQUENCY_STOCK_LEVEL", 4);
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
}
