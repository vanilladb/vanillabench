package org.vanilladb.bench.tpce.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import org.vanilladb.bench.tpce.TpceConstants;
import org.vanilladb.bench.util.BenchProperties;
import org.vanilladb.bench.util.RandomValueGenerator;

/**
 * This must be thread-safe.
 * 
 * @author SLMT
 *
 */
public class TpceDataManager {

	private static final String DATA_DIR;

	static {
		DATA_DIR = BenchProperties.getLoader().getPropertyAsString(TpceDataManager.class.getName() + ".DATA_DIR", "");
	}

	private final long _TIdentShift = 4300000000l;
	private final RandomValueGenerator rg = new RandomValueGenerator();
	private final AtomicLong nextTradeId = new AtomicLong();

	// Pre-generated data
	private CopyOnWriteArrayList<String> companyNames;
	private CopyOnWriteArrayList<String> securitySymbols;
	// store the account information (for dd), <0 ~ account count,
	// CustomerAccount(accountId, customerId, brokerId)>
	private ConcurrentHashMap<Long, Customer> customerMap;
	private ConcurrentLinkedQueue<Trade> tradeQueue = new ConcurrentLinkedQueue<Trade>();

	public TpceDataManager() {
		loadData();
	}

	public void addNewTrade(long tradeId, long customerId, long customerAccountId, long brokerId) {
		tradeQueue.add(new Trade(tradeId, customerId, customerAccountId, brokerId));
	}

	/**
	 * Get the oldest trade. And the returned trade will be removed.
	 */
	public Trade getOldestTrade() {
		return tradeQueue.poll();
	}
	
	public long getNextTradeId() {
		return nextTradeId.getAndIncrement();
	}

	public Customer getNonUniformRandomCustomer() {
		long iCHigh, iCLow;
		double fCW = rg.randomDoubleIncrRange(0.0001, 2000, 0.000000001);

		// Generate a load unit across the entire range
		iCHigh = (rg.randomLongRange(1 + _TIdentShift, _TIdentShift + TpceConstants.CUSTOMER_COUNT) - 1) / 1000;

		if (fCW <= 200) {
			iCLow = (int) Math.ceil(Math.sqrt(22500 + 500 * fCW) - 151);
		} else if (fCW <= 1400) {
			iCLow = (int) Math.ceil(Math.sqrt(290000 + 1000 * fCW) - 501);
		} else {
			iCLow = (int) Math.ceil(Math.sqrt(500 * fCW - 277500));
		}

		long finalCustomerId = iCHigh * 1000 + permute(iCLow, iCHigh) + 1;
		return customerMap.get(finalCustomerId);
	}
	
	public String getRandomCompanyName() {
		int companyIdx = rg.number(0, TpceConstants.COMPANY_COUNT - 1);
		return companyNames.get(companyIdx);
	}

	// TODO
	public double getRandomRequestPrice() {
		return 0;
	}

	public boolean getRandomRollback() {
		// always return false
		return false;
	}

	public String getRandomSymbol() {
		int symbolIdx = rg.number(0, TpceConstants.SECURITY_COUNT - 1);
		return securitySymbols.get(symbolIdx);
	}

	// TODO
	public int getRandomTradeQuantity() {
		return 0;
	}

	// TODO
	public String getRandomTradeType() {
		return "TMB";
	}

	private void loadData() {
		try {
			// Load customer data
			customerMap = new ConcurrentHashMap<Long, Customer>();
			BufferedReader br = new BufferedReader(new FileReader(DATA_DIR + "Customer.txt"));

			String line = null;
			while ((line = br.readLine()) != null) {
				String[] fields = line.split("\\|");

				// TODO: Load other fields
				long cid = Long.parseLong(fields[0]);

				customerMap.put(cid, new Customer(cid));
			}

			br.close();

			// Load customer account data
			br = new BufferedReader(new FileReader(DATA_DIR + "CustomerAccount.txt"));

			line = null;
			while ((line = br.readLine()) != null) {
				String[] fields = line.split("\\|");

				// TODO: Load other fields
				long acid = Long.parseLong(fields[0]);
				long bid = Long.parseLong(fields[1]);
				long cid = Long.parseLong(fields[2]);

				customerMap.get(cid).addAccount(new CustomerAccount(acid, bid));
			}

			br.close();

			// Load company data
			ArrayList<String> comanyNameList = new ArrayList<String>();
			br = new BufferedReader(new FileReader(DATA_DIR + "Company.txt"));

			line = null;
			while ((line = br.readLine()) != null) {
				String[] fields = line.split("\\|");

				// TODO: Load other fields
				comanyNameList.add(fields[2]);
			}

			br.close();
			companyNames = new CopyOnWriteArrayList<String>(comanyNameList);

			// Load security data
			ArrayList<String> secNameList = new ArrayList<String>();
			br = new BufferedReader(new FileReader(DATA_DIR + "Security.txt"));

			line = null;
			while ((line = br.readLine()) != null) {
				String[] fields = line.split("\\|");

				// TODO: Load other fields
				secNameList.add(fields[0]);
			}

			br.close();
			securitySymbols = new CopyOnWriteArrayList<String>(secNameList);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private long permute(long iLow, long iHigh) {
		return ((677 * iLow + 33 * (iHigh + 1)) % 1000);
	}
}
