/*******************************************************************************
 * Copyright 2016, 2018 vanilladb.org contributors
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
package org.vanilladb.bench;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.util.BenchProperties;

public class StatisticMgr {
	private static Logger logger = Logger.getLogger(StatisticMgr.class.getName());

	private static final File OUTPUT_DIR;
	private static int GRANULARITY;

	static {
		String outputDirPath = BenchProperties.getLoader().getPropertyAsString(StatisticMgr.class.getName()
				+ ".OUTPUT_DIR", null);
		
		if (outputDirPath == null) {
			OUTPUT_DIR = new File(System.getProperty("user.home"), "benchmark_results");
		} else {
			OUTPUT_DIR = new File(outputDirPath);
		}

		// Create the directory if that doesn't exist
		if (!OUTPUT_DIR.exists())
			OUTPUT_DIR.mkdir();
		
		GRANULARITY = BenchProperties.getLoader()
				.getPropertyAsInteger(StatisticMgr.class.getName() + ".GRANULARITY", 3000);
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

	private List<TxnResultSet> resultSets = new ArrayList<TxnResultSet>();
	private TreeMap<Long, ArrayList<Long>> latencyHistory = new TreeMap<Long, ArrayList<Long>>();
	private List<TransactionType> allTxTypes;
	private String fileNamePostfix = "";
	
	public StatisticMgr(Collection<TransactionType> txTypes) {
		allTxTypes = new LinkedList<TransactionType>(txTypes);
	}
	
	public StatisticMgr(Collection<TransactionType> txTypes, String namePostfix) {
		allTxTypes = new LinkedList<TransactionType>(txTypes);
		fileNamePostfix = namePostfix;
	}

	public synchronized void processTxnResult(TxnResultSet trs) {
		resultSets.add(trs);
	}

	public synchronized void processBatchTxnsResult(TxnResultSet... trss) {
		for (TxnResultSet trs : trss)
			resultSets.add(trs);
	}

	public synchronized void outputReport() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss"); // E.g. "20180524-200824"
			String fileName = formatter.format(Calendar.getInstance().getTime());
			if (fileNamePostfix != null && !fileNamePostfix.isEmpty())
				fileName += "-" + fileNamePostfix; // E.g. "20180524-200824-postfix"
			
			outputDetailReport(fileName);
			outputTimelineReport(fileName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.INFO))
			logger.info("Finnish creating tpcc benchmark report");
	}
	
	private void outputDetailReport(String fileName) throws IOException {
		HashMap<TransactionType, TxnStatistic> txnStatistics = new HashMap<TransactionType, TxnStatistic>();
		
		for (TransactionType type : allTxTypes)
			txnStatistics.put(type, new TxnStatistic(type));
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR, fileName + ".txt")))) {
			// First line: total transaction count
			writer.write("# of txns during benchmark period: " + resultSets.size());
			writer.newLine();
			
			// Detail latency report
			for (TxnResultSet resultSet : resultSets) {
				// Write a line: {[Tx Type]: [Latency]}
				writer.write(resultSet.getTxnType() + ": "
						+ TimeUnit.NANOSECONDS.toMillis(resultSet.getTxnResponseTime()) + " ms");
				writer.newLine();
				
				// Count transaction for each type
				TxnStatistic txnStatistic = txnStatistics.get(resultSet.getTxnType());
				txnStatistic.addTxnResponseTime(resultSet.getTxnResponseTime());
				
				// For another report
				addTxnLatency(resultSet);
			}
			writer.newLine();
			
			// Last few lines: show the statistics for each type of transactions
			for (Entry<TransactionType, TxnStatistic> entry : txnStatistics.entrySet()) {
				TxnStatistic value = entry.getValue();
				if (value.txnCount > 0) {
					long avgResTimeMs = TimeUnit.NANOSECONDS.toMillis(value.getTotalResponseTime() / value.txnCount);
					writer.write(value.getmType() + " " + value.getTxnCount()
						+ " avg latency: " + avgResTimeMs + " ms");
				} else { // Avoid "Divide By Zero"
					writer.write(value.getmType() + " 0 avg latency: 0 ms");
				}
				writer.newLine();
			}
			
			// Last line: Total statistics
			int count = resultSets.size();
			if (count > 0) { // Avoid "Divide By Zero"
				double avgResTimeMs = 0;
				for (TxnResultSet rs : resultSets)
					avgResTimeMs += rs.getTxnResponseTime() / count;
				writer.write(String.format("TOTAL %d avg latency: %d ms", count, Math.round(avgResTimeMs / 1000000)));
			} else {
				writer.write("TOTAL 0 avg latency: 0 ms");
			}
		}
	}
	
	private void outputTimelineReport(String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR, fileName + ".csv")))) {
			writer.write(
					"time(sec), throughput(txs), avg_latency(ms), min(ms), max(ms), 25th_lat(ms), median_lat(ms), 75th_lat(ms)");
			writer.newLine();
			for (Map.Entry<Long, ArrayList<Long>> e : latencyHistory.entrySet()) {
				writer.write(makeStatString(e.getKey(), e.getValue()));
				writer.newLine();
			}
		}
	}

	private void addTxnLatency(TxnResultSet rs) {
		long elapsedTime = TimeUnit.NANOSECONDS.toMillis(rs.getTxnEndTime() - Benchmarker.BENCH_START_TIME);
		long timeSlotBoundary = (elapsedTime / GRANULARITY) * GRANULARITY / 1000; // in seconds
		
		ArrayList<Long> timeSlot = latencyHistory.get(timeSlotBoundary);
		if (timeSlot == null) {
			timeSlot = new ArrayList<Long>();
			latencyHistory.put(timeSlotBoundary, timeSlot);
		}
		timeSlot.add(TimeUnit.NANOSECONDS.toMillis(rs.getTxnResponseTime()));
	}

	private String makeStatString(long timeSlotBoundary, List<Long> timeSlot) {
		Collections.sort(timeSlot);
		
		// Transfer it to unmodifiable in order to prevent modification
		// when we use a sublist to access it.
		timeSlot = Collections.unmodifiableList(timeSlot);
		
		int count = timeSlot.size();
		int middleOffset = timeSlot.size() / 2;
		long lowerQ, upperQ, median;
		double mean;

		median = calcMedian(timeSlot);
		mean = calcMean(timeSlot);
		
		if (count < 2) { // Boundary case: there is only one number in the list
			lowerQ = median;
			upperQ = median;
		} else if (count % 2 == 0) { // Even
			lowerQ = calcMedian(timeSlot.subList(0, middleOffset));
			upperQ = calcMedian(timeSlot.subList(middleOffset, count));
		} else { // Odd
			lowerQ = calcMedian(timeSlot.subList(0, middleOffset));
			upperQ = calcMedian(timeSlot.subList(middleOffset + 1, count));
		}

		Long min = Collections.min(timeSlot);
		Long max = Collections.max(timeSlot);
		
		return String.format("%d, %d, %f, %d, %d, %d, %d, %d",
				timeSlotBoundary, count, mean, min, max, lowerQ, median, upperQ);
	}

	private Long calcMedian(List<Long> timeSlot) {
		int count = timeSlot.size();
		if (count % 2 != 0) // Odd
			return timeSlot.get((count - 1) / 2);
		else {// Even
			long front = timeSlot.get(count / 2 - 1);
			long back = timeSlot.get(count / 2);
			return (front + back) / 2;
		}
	}

	private double calcMean(List<Long> timeSlot) {
		double mean = 0;
		double count = timeSlot.size();
		for (Long lat : timeSlot)
			mean += (lat / count);
		return mean;
	}
}