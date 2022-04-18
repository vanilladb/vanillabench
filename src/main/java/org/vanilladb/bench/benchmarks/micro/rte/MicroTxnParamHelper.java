package org.vanilladb.bench.benchmarks.micro.rte;

public class MicroTxnParamHelper {
	
	private int readCount;
	private int writeCount;
	private int[] readItemId;
	private int[] writeItemId;
	private double[] newItemPrice;
	
	private boolean isReadOnly;
	
	public void unpackParameters(Object... pars) {
		int indexCnt = 0;

		readCount = (Integer) pars[indexCnt++];
		readItemId = new int[readCount];

		for (int i = 0; i < readCount; i++)
			readItemId[i] = (Integer) pars[indexCnt++];

		writeCount = (Integer) pars[indexCnt++];
		writeItemId = new int[writeCount];
		for (int i = 0; i < writeCount; i++)
			writeItemId[i] = (Integer) pars[indexCnt++];
		newItemPrice = new double[writeCount];
		for (int i = 0; i < writeCount; i++)
			newItemPrice[i] = (Double) pars[indexCnt++];

		if (writeCount == 0)
			isReadOnly = true;
	}

	public int getReadCount() {
		return readCount;
	}

	public int getWriteCount() {
		return writeCount;
	}

	public int getReadItemId(int index) {
		return readItemId[index];
	}

	public int getWriteItemId(int index) {
		return writeItemId[index];
	}

	public double getNewItemPrice(int index) {
		return newItemPrice[index];
	}
	
	public boolean isReadOnly() {
		return isReadOnly;
	}
}
