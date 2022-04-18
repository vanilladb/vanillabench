package org.vanilladb.bench.benchmarks.micro.rte;

public class MicroTestbedParamHelper {

	private static final String TABLES_DDL[] = {
			"CREATE TABLE item ( i_id INT, i_im_id INT, i_name VARCHAR(24), "
					+ "i_price DOUBLE, i_data VARCHAR(50) )" };
	private static final String INDEXES_DDL[] = {
			"CREATE INDEX idx_item ON item (i_id)" };
	
	private int numOfItems = 0;

	public String[] getTableSchemas() {
		return TABLES_DDL;
	}

	public String[] getIndexSchemas() {
		return INDEXES_DDL;
	}
	
	public int getNumberOfItems() {
		return numOfItems;
	}
	
	public void unpackParameters(Object... pars) {
		numOfItems = (Integer) pars[0];
	}
}
