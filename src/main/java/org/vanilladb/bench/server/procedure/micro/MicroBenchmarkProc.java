package org.vanilladb.bench.server.procedure.micro;

import org.vanilladb.bench.server.param.micro.MicroBenchmarkProcParamHelper;
import org.vanilladb.bench.server.procedure.BasicStoredProcedure;
import org.vanilladb.core.query.algebra.Scan;

public class MicroBenchmarkProc extends BasicStoredProcedure<MicroBenchmarkProcParamHelper> {

	public MicroBenchmarkProc() {
		super(new MicroBenchmarkProcParamHelper());
	}

	@Override
	protected void executeSql() {

		for (int idx = 0; idx < paramHelper.getReadCount(); idx++) {
			int iid = paramHelper.getReadItemId(idx);
			Scan s = executeQuery("SELECT i_name, i_price FROM item WHERE i_id = " + iid);
			s.beforeFirst();
			if (s.next()) {
				String name = (String) s.getVal("i_name").asJavaVal();
				double price = (Double) s.getVal("i_price").asJavaVal();

				paramHelper.setItemName(name, idx);
				paramHelper.setItemPrice(price, idx);
			} else
				throw new RuntimeException("Cloud not find item record with i_id = " + iid);

			s.close();
		}

		for (int idx = 0; idx < paramHelper.getWriteCount(); idx++) {
			int iid = paramHelper.getWriteItemId(idx);
			double newPrice = paramHelper.getNewItemPrice(idx);
			executeUpdate("UPDATE item SET i_price = " + newPrice + " WHERE i_id =" + iid);
		}
	}
}
