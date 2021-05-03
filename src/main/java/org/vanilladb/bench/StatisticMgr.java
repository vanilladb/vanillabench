/*******************************************************************************
 * Copyright 2016 ~ 2021 vanilladb.org contributors
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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vanilladb.bench.util.BenchProperties;

public class StatisticMgr {
	private static Logger logger = Logger.getLogger(StatisticMgr.class.getName());

	private static final File OUTPUT_DIR;
	private static int GRANULARITY;

	static {
		String outputDirPath = BenchProperties.getLoader()
				.getPropertyAsString(StatisticMgr.class.getName() + ".OUTPUT_DIR", null);

		if (outputDirPath == null) {
			OUTPUT_DIR = new File(System.getProperty("user.home"), "benchmark_results");
		} else {
			OUTPUT_DIR = new File(outputDirPath);
		}

		// Create the directory if that doesn't exist
		if (!OUTPUT_DIR.exists())
			OUTPUT_DIR.mkdir();

		GRANULARITY = BenchProperties.getLoader().getPropertyAsInteger(
				StatisticMgr.class.getName() + ".GRANULARITY", 3000);
	}

	private static class TxnStatistic {
		private BenchTransactionType mType;
		
		private int commitedTxnCount = 0;
		private int abortedTxnCount = 0;
		
		private long totalResponseTimeNs = 0;

		public TxnStatistic(BenchTransactionType txnType) {
			this.mType = txnType;
		}

		public BenchTransactionType getmType() {
			return mType;
		}

		public void addTxnResponseTimeAndCommitedTxnCount(long responseTime) {
			commitedTxnCount++;
			totalResponseTimeNs += responseTime;
		}
		
		public void addAbortedTxnCount() {
			abortedTxnCount++;
		}

		public int commitedTxnCount() {
			return commitedTxnCount;
		}
		
		public int abortedTxnCount() {
			return abortedTxnCount;
		}

		public long getTotalResponseTime() {
			return totalResponseTimeNs;
		}
	}
	
	private BlockingQueue<TxnResultSet> resultSets = new ArrayBlockingQueue<TxnResultSet>(100_000);
	private int totalTxnCount;
	private double totalResTimeMs;
	private TreeMap<Long, ArrayList<Long>> latencyHistory = new TreeMap<Long, ArrayList<Long>>();
	private List<BenchTransactionType> allTxTypes;
	private String fileNamePostfix = "";
	private long recordStartTime = -1;
	private ZipThread zipThread;
	private Map<BenchTransactionType, TxnStatistic> txnStatistics = new HashMap<BenchTransactionType, TxnStatistic>();
	private Map<BenchTransactionType, Integer> abortedCounts = new HashMap<BenchTransactionType, Integer>();

	public StatisticMgr(Collection<BenchTransactionType> txTypes) {
		allTxTypes = new LinkedList<BenchTransactionType>(txTypes);
		initZipThread();
	}

	public StatisticMgr(Collection<BenchTransactionType> txTypes, String namePostfix) {
		allTxTypes = new LinkedList<BenchTransactionType>(txTypes);
		fileNamePostfix = namePostfix;
		initZipThread();
	}
	
	private void initZipThread() {
		zipThread = new ZipThread();
		zipThread.start();
		for (BenchTransactionType type : allTxTypes) {
			txnStatistics.put(type, new TxnStatistic(type));
			abortedCounts.put(type, 0);
		}
	}

	public class ZipThread extends Thread {
		boolean stop = false;

		@Override
		public void run() {
			while (!stop||!resultSets.isEmpty()) {
				try {
					TxnResultSet resultSet = resultSets.poll(1, TimeUnit.SECONDS);
					if (resultSet!=null) {
						analyzeReadWriteSet(resultSet);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void stopRunning() {
			stop = true;
		}
	}

	/**
	 * We use the time that this method is called at as the start time for
	 * recording.
	 */
	public synchronized void setRecordStartTime() {
		if (recordStartTime == -1)
			recordStartTime = System.nanoTime();
	}

	public synchronized void processTxnResult(TxnResultSet trs) {
		if (recordStartTime == -1)
			recordStartTime = trs.getTxnEndTime();

		resultSets.add(trs);
	}

	public synchronized void outputReport() {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd-HHmmss"); // E.g. "20180524-200824"
			String fileName = formatter.format(Calendar.getInstance().getTime());
			if (fileNamePostfix != null && !fileNamePostfix.isEmpty())
				fileName += "-" + fileNamePostfix; // E.g. "20180524-200824-postfix"
			
			// Stop the zipping thread
			zipThread.stopRunning();
			zipThread.join();
			
			// Output the result
			outputDetailReport(fileName);
			outputTimelineReport(fileName);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (logger.isLoggable(Level.INFO))
			logger.info("Finnish creating tpcc benchmark report");
	}

	private void outputDetailReport(String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(
				new FileWriter(new File(OUTPUT_DIR, fileName + ".txt")))) {
			// First line: total transaction count
			writer.write("# of txns (including aborted) during benchmark period: " + totalTxnCount);
			writer.newLine();

			// Last few lines: show the statistics for each type of transactions
			int abortedTotal = 0;
			for (Entry<BenchTransactionType, TxnStatistic> entry : txnStatistics.entrySet()) {
				TxnStatistic value = entry.getValue();
				//int abortedCount = abortedCounts.get(entry.getKey());
				abortedTotal += value.abortedTxnCount();
				long avgResTimeMs = 0;

				if (value.commitedTxnCount > 0) {
					avgResTimeMs = TimeUnit.NANOSECONDS.toMillis(value.getTotalResponseTime() / value.commitedTxnCount);
				}

				writer.write(value.getmType() + " - committed: " + value.commitedTxnCount() + ", aborted: " + value.abortedTxnCount()
						+ ", avg latency: " + avgResTimeMs + " ms");
				writer.newLine();
			}

			// Last line: Total statistics
			int finishedCount = totalTxnCount - abortedTotal;
			double avgResTimeMs = 0;
			if (finishedCount > 0) { // Avoid "Divide By Zero"
				avgResTimeMs = totalResTimeMs / finishedCount;
			}
			writer.write(String.format("TOTAL - committed: %d, aborted: %d, avg latency: %d ms",
					finishedCount, abortedTotal, Math.round(avgResTimeMs / 1000000)));
		}
	}

	private void outputTimelineReport(String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(OUTPUT_DIR, fileName + ".csv")))) {
			writer.write(
					"time(sec), throughput(txs), avg_latency(ms), min(ms), max(ms), 25th_lat(ms), median_lat(ms), 75th_lat(ms)");
			writer.newLine();

			int timeAdvance = GRANULARITY / 1000;
			for (long timeBound = 0, outCount = 0; outCount < latencyHistory.size(); timeBound += timeAdvance) {
				List<Long> slot = latencyHistory.get(timeBound);
				if (slot != null) {
					writer.write(makeStatString(timeBound, slot));
					outCount++;
				} else
					writer.write(String.format("%d, 0, NaN, NaN, NaN, NaN, NaN, NaN", timeBound));
				writer.newLine();
			}
		}
	}

	private void analyzeReadWriteSet(TxnResultSet rs) {
		long elapsedTime = TimeUnit.NANOSECONDS.toMillis(rs.getTxnEndTime() - recordStartTime);
		long timeSlotBoundary = (elapsedTime / GRANULARITY) * GRANULARITY / 1000; // in seconds

		ArrayList<Long> timeSlot = latencyHistory.get(timeSlotBoundary);
		if (timeSlot == null) {
			timeSlot = new ArrayList<Long>();
			latencyHistory.put(timeSlotBoundary, timeSlot);
		}
		timeSlot.add(TimeUnit.NANOSECONDS.toMillis(rs.getTxnResponseTime()));
		
		// there are multiple txn types
		TxnStatistic txnStatistic = txnStatistics.get(rs.getTxnType());
		
		if (rs.isTxnIsCommited()) {
			// Count transactions for each type
			txnStatistic.addTxnResponseTimeAndCommitedTxnCount(rs.getTxnResponseTime());
		} else {
			// Count aborted transactions
			txnStatistic.addAbortedTxnCount();
		}
		totalResTimeMs += rs.getTxnResponseTime();
		totalTxnCount++;
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