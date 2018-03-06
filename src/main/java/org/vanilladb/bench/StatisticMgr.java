package org.vanilladb.bench;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.util.BenchProperties;

public class StatisticMgr {
	private static Logger logger = Logger.getLogger(StatisticMgr.class.getName());

	private static final String OUTPUT_DIR;

	private List<TxnResultSet> resultSets = new ArrayList<TxnResultSet>();

	private static long benchStartTime;

	private static int GRANULARITY;

	private static TreeMap<Long, ArrayList<Long>> latencyHistory;

	static {
		File defaultDir = new File(System.getProperty("user.home"), "benchmark_results");
		OUTPUT_DIR = BenchProperties.getLoader().getPropertyAsString(StatisticMgr.class.getName() + ".OUTPUT_DIR",
				defaultDir.getAbsolutePath());

		// Create the directory if that doesn't exist
		File dir = new File(OUTPUT_DIR);
		if (!dir.exists())
			dir.mkdir();
		benchStartTime = System.nanoTime();
		GRANULARITY = BenchProperties.getLoader()
				.getPropertyAsInteger(StatisticMgr.class.getName() + ".GRANULARITY", 3000);
		latencyHistory = new TreeMap<Long, ArrayList<Long>>();
	}
	
	private List<TransactionType> allTxTypes;
	
	public StatisticMgr(Collection<TransactionType> txTypes) {
		allTxTypes = new LinkedList<TransactionType>(txTypes);
	}

	public synchronized void processTxnResult(TxnResultSet trs) {
		resultSets.add(trs);
	}

	public synchronized void processBatchTxnsResult(TxnResultSet... trss) {
		for (TxnResultSet trs : trss)
			resultSets.add(trs);
	}

	public synchronized void outputReport() {
		HashMap<TransactionType, TxnStatistic> txnStatistics = new HashMap<TransactionType, TxnStatistic>();
		
		for (TransactionType type : allTxTypes)
			txnStatistics.put(type, new TxnStatistic(type));

		try {
			Random random = new Random();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss");
			String timeString = formatter.format(Calendar.getInstance().getTime());
			String fileName = timeString + ".txt";

			File dir = new File(OUTPUT_DIR);
			File outputFile = new File(dir, fileName);
			FileWriter wrFile = new FileWriter(outputFile);
			BufferedWriter bwrFile = new BufferedWriter(wrFile);

			// write total transaction count
			bwrFile.write("# of txns during benchmark period: " + resultSets.size());
			bwrFile.newLine();

			// read all txn resultset
			for (TxnResultSet resultSet : resultSets) {
				bwrFile.write(resultSet.getTxnType() + ": "
						+ TimeUnit.NANOSECONDS.toMillis(resultSet.getTxnResponseTime()) + " ms");
				bwrFile.newLine();
				TxnStatistic txnStatistic = txnStatistics.get(resultSet.getTxnType());
				if (txnStatistic != null)
					txnStatistic.addTxnResponseTime(resultSet.getTxnResponseTime());
				addTxnLatency(resultSet);

			}
			bwrFile.newLine();

			// output result
			for (Entry<TransactionType, TxnStatistic> entry : txnStatistics.entrySet()) {
				TxnStatistic value = entry.getValue();
				if (value.txnCount > 0) {
					long avgResTimeMs = TimeUnit.NANOSECONDS.toMillis(value.getTotalResponseTime() / value.txnCount);
					bwrFile.write(
							value.getmType() + " " + value.getTxnCount() + " avg latency: " + avgResTimeMs + " ms");
				} else {
					bwrFile.write(value.getmType() + " " + value.getTxnCount() + " avg latency: 0 ms");
				}
				bwrFile.newLine();
			}
			// TOTAL
			int l = resultSets.size();
			if (l > 0) {
				double avgResTimeMs = 0;

				for (TxnResultSet rs : resultSets) {
					avgResTimeMs += rs.getTxnResponseTime() / l;
				}
				bwrFile.write(String.format("TOTAL %d avg latency: %d ms", l, Math.round(avgResTimeMs / 1000000)));
			} else {
				bwrFile.write("TOTAL " + l + " avg latency: 0 ms");
			}

			bwrFile.close();

			fileName = timeString + "-" + random.nextInt() + ".csv";
			outputFile = new File(dir.getAbsoluteFile(), fileName);
			wrFile = new FileWriter(outputFile);
			bwrFile = new BufferedWriter(wrFile);
			bwrFile.write(
					"time(sec), throughput(txs), avg_latency(ms), min(ms), max(ms), 25th_lat(ms), median_lat(ms), 75th_lat(ms)");
			bwrFile.newLine();
			for (Map.Entry<Long, ArrayList<Long>> e : latencyHistory.entrySet()) {
				bwrFile.write(makeStatString(e));
				bwrFile.newLine();
			}
			bwrFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.INFO))
			logger.info("Finnish creating tpcc benchmark report");
	}

	public void addTxnLatency(TxnResultSet rs) {

		long t = TimeUnit.NANOSECONDS.toMillis(rs.getTxnEndTime() - benchStartTime);
		t = (t / GRANULARITY) * GRANULARITY / 1000;

		if (!latencyHistory.containsKey(t))
			latencyHistory.put(t, new ArrayList<Long>());

		latencyHistory.get(t).add(TimeUnit.NANOSECONDS.toMillis(rs.getTxnResponseTime()));

	}

	private String makeStatString(Map.Entry<Long, ArrayList<Long>> e) {

		Long[] datas = e.getValue().toArray(new Long[e.getValue().size()]);
		Arrays.sort(datas);

		int len = datas.length;
		int mid = len / 2;
		long lowerQ, upperQ, median;

		median = calcMedian(datas);
		double mean = calcMean(datas);

		if (len % 2 == 0) {
			lowerQ = calcMedian(Arrays.copyOfRange(datas, 0, mid));
			upperQ = calcMedian(Arrays.copyOfRange(datas, mid, len));
		} else {
			lowerQ = calcMedian(Arrays.copyOfRange(datas, 0, mid));
			upperQ = calcMedian(Arrays.copyOfRange(datas, mid + 1, len));
		}

		Long min = Collections.min(e.getValue());
		Long max = Collections.max(e.getValue());

		return e.getKey().toString() + ',' + Integer.toString(len) + ',' + mean + ',' + min + ',' + max + ',' + lowerQ
				+ ',' + median + ',' + upperQ;

	}

	private Long calcMedian(Long[] data) {
		int len = data.length;
		if (len % 2 != 0)
			return data[(len - 1) / 2];
		else
			return (data[len / 2] + data[len / 2 - 1]) / 2;

	}

	private double calcMean(Long[] datas) {
		double mean = 0;
		int len = datas.length;
		for (Long d : datas)
			mean += d;
		mean /= len;
		return mean;
	}

	private static class TxnStatistic {
		private TransactionType mType;
		private int txnCount = 0;
		private long totalResponseTimeNs = 0;

		public TxnStatistic(TransactionType txnType) {
			this.mType = txnType;
		}

		public TransactionType getmType() {
			return mType;
		}

		public void addTxnResponseTime(long responseTime) {
			txnCount++;
			totalResponseTimeNs += responseTime;
		}

		public int getTxnCount() {
			return txnCount;
		}

		public long getTotalResponseTime() {
			return totalResponseTimeNs;
		}
	}
}