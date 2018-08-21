package org.vanilladb.bench.benchmarks.tpcc;

import java.util.Random;

/** A TPC-C random generator. */
public class TpccValueGenerator {
	public static final int NU_CLAST_LOAD = 0, NU_CLAST_RUN = 1, NU_CID = 2, NU_OLIID = 3;
	private static final String CHARSET = new String("QAa0bcLdUK2eHfJgTP8XhiFj61DOklNm9nBoI5pGqYVrs3CtSuMZvwWx4yE7zR");
	private static final String TOKENS[] = { "BAR", "OUGHT", "ABLE", "PRI", "PRES", "ESE", "ANTI", "CALLY", "ATION",
			"EING", };

	private Random rng = new Random();
	private NURandGenerator nug = new NURandGenerator();

	public static void main(String[] args) {
		TpccValueGenerator ranValGen = new TpccValueGenerator(100);

		System.out.println(ranValGen.nextDouble());
		System.out.println(ranValGen.nstring(10));
		System.out.println(ranValGen.randomAString(10));
		System.out.println(ranValGen.fixedDecimalNumber(10, 0.1, 1.0));

		ranValGen = new TpccValueGenerator(100);

		System.out.println(ranValGen.nextDouble());
		System.out.println(ranValGen.nstring(10));
		System.out.println(ranValGen.randomAString(10));
		System.out.println(ranValGen.fixedDecimalNumber(10, 0.1, 1.0));
	}

	public TpccValueGenerator() {
	}

	public TpccValueGenerator(long seed) {
		rng = new Random(seed);
		nug = new NURandGenerator();
	}

	public Random rng() {
		return (rng);
	}

	/**
	 * Return an integer in the (inclusive) range [min, max].
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public int number(int min, int max) {
		if (min > max)
			throw new IllegalArgumentException();
		int value = rng.nextInt(max - min + 1);
		value += min;
		return value;
	}

	/**
	 * Return the result of a random choose from a given distribution.
	 * 
	 * @param probs
	 * @return
	 */
	public int randomChooseFromDistribution(double... probs) {
		int result = -1;
		int[] range = new int[probs.length];
		double accuracy = 1000;
		int total = 0;

		for (int i = 0; i < probs.length; i++) {
			range[i] = (int) (probs[i] * accuracy);
			total += range[i];
		}

		int randNum = ((int) (rng.nextDouble() * total)) % total;
		for (int i = 0; i < range.length; i++) {
			randNum -= range[i];
			if (randNum < 0) {
				result = i;
				break;
			}
		}
		return result;
	}

	public double nextDouble() {
		return rng.nextDouble();
	}

	/**
	 * Return an integer in the (inclusive) range [minimum, maximum] excluding
	 * exclusivedVal.
	 */
	public int numberExcluding(int min, int max, int exclusivedVal) {
		if (min > max)
			throw new IllegalArgumentException();
		if (min > exclusivedVal || exclusivedVal > max)
			throw new IllegalArgumentException();
		int value = number(min, max - 1);
		if (value >= exclusivedVal)
			value++;
		return value;
	}

	/**
	 * Return an fixed decimal double value in the (inclusive) range [minimum,
	 * maximum]. For example, [0.01 .. 100.00] with decimal 2 has 10,000 unique
	 * values.
	 * 
	 */
	public double fixedDecimalNumber(int decimal, double min, double max) {
		if (min > max)
			throw new IllegalArgumentException();
		if (decimal < 0)
			throw new IllegalArgumentException();
		int multiplier = 1;
		for (int i = 0; i < decimal; ++i) {
			multiplier *= 10;
		}
		int top = (int) (min * multiplier);
		int bottom = (int) (max * multiplier);
		return (double) number(top, bottom) / (double) multiplier;
	}

	/** Return a last name as defined by TPC-C 4.3.2.3. */
	public String makeLastName(int number) {
		if (number < 0 && number > TpccConstants.NUM_DISTINCT_CLAST - 1)
			throw new IllegalArgumentException();
		int indicies[] = { number / 100, (number / 10) % 10, number % 10 };
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indicies.length; ++i) {
			sb.append(TOKENS[indicies[i]]);
		}
		return sb.toString();
	}

	public String makeRandomLastName(boolean isForLoad) {
		int i;
		if (isForLoad)
			i = NURand(NU_CLAST_LOAD, 0, TpccConstants.NUM_DISTINCT_CLAST - 1);
		else
			i = NURand(NU_CLAST_RUN, 0, TpccConstants.NUM_DISTINCT_CLAST - 1);
		return makeLastName(i);
	}

	/**
	 * Return a string of random alphanumeric characters of a random length
	 * between [minLength, maxLength].
	 */
	public String randomAString(int minLength, int maxLength) {
		int length = number(minLength, maxLength);
		return randomAString(length);
	}

	public String randomAString(int length) {
		StringBuffer sb = new StringBuffer();
		int te = 0;
		for (int i = 1; i <= length; i++) {
			te = rng().nextInt(62);
			sb.append(CHARSET.charAt(te));
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param minimum_length
	 * @param maximum_length
	 * @return a random numeric string with length in range [minimum_length,
	 *         maximum_length].
	 */
	public String nstring(int minimum_length, int maximum_length) {
		return randomString(minimum_length, maximum_length, '0', 10);
	}

	public String nstring(int length) {
		return randomString(length, '0', 10);
	}

	public String randomZipCode() {
		StringBuffer sb = new StringBuffer();
		sb.append(nstring(4));
		sb.append("11111");
		return sb.toString();
	}

	private String randomString(int minimum_length, int maximum_length, char base, int numCharacters) {
		int length = number(minimum_length, maximum_length);
		return randomString(length, base, numCharacters);
	}

	private String randomString(int length, char base, int numCharacters) {
		byte baseByte = (byte) base;
		byte[] bytes = new byte[length];
		for (int i = 0; i < length; ++i) {
			bytes[i] = (byte) (baseByte + number(0, numCharacters - 1));
		}
		return new String(bytes);
	}

	public int NURand(int type, int min, int max) {
		return nug.NURand(type, min, max);
	}

	public class NURandGenerator {
		private final int aForCLast;
		private final int aForCId;
		private final int aForOrderLineIId;
		private int cLoadForCLast;
		private int cRunForCLast;
		private int cForCId;
		private int cForOrderLineIId;

		public NURandGenerator() {
			aForCLast = (int) (TpccConstants.NUM_DISTINCT_CLAST * ((double) 255 / 1000));
			aForCId = (int) (TpccConstants.CUSTOMERS_PER_DISTRICT * ((double) 1023 / 3000));
			aForOrderLineIId = (int) (TpccConstants.NUM_ITEMS * ((double) 8191 / 100000));
			generateC();
		}

		public void setCLoadForCLast(int cLoad) {
			cLoadForCLast = cLoad;
		}

		public void setCRunForCLast(int cRun) {
			cRunForCLast = cRun;
		}

		public void setCForCId(int c) {
			cForCId = c;
		}

		public void setCForOrderLineIId(int c) {
			cForOrderLineIId = c;
		}

		public int NURand(int type, int min, int max) {
			if (min > max)
				throw new IllegalArgumentException();
			int c, a;
			switch (type) {
			case NU_CLAST_LOAD:
				a = aForCLast;
				c = cLoadForCLast;
				break;
			case NU_CLAST_RUN:
				a = aForCLast;
				c = cRunForCLast;
				break;
			case NU_CID:
				a = aForCId;
				c = cForCId;
				break;
			case NU_OLIID:
				a = aForOrderLineIId;
				c = cForOrderLineIId;
				break;
			default:
				throw new IllegalArgumentException();
			}

			return (((number(0, a) | number(min, max)) + c) % (max - min + 1)) + min;
		}

		public String toString() {
			return "NURand with aForCLast = " + aForCLast + ", aForCId = " + aForCId + ", aForOrderLineIId = "
					+ aForOrderLineIId + ", cLoadForCLast = " + cLoadForCLast + ", cRunForCLast = " + cRunForCLast
					+ ", cForCId = " + cForCId + ", cForOrderLineIId=" + cForOrderLineIId;
		}

		private void generateC() {
			cForCId = number(0, aForCId);
			cForOrderLineIId = number(0, aForOrderLineIId);
			if (TpccConstants.CUSTOMERS_PER_DISTRICT < 1000) {
				cLoadForCLast = 91;
				cRunForCLast = 21;
			} else {
				cLoadForCLast = number(0, aForCLast);
				cRunForCLast = number(0, aForCLast);
				while (!isValidCRun())
					cRunForCLast = number(0, aForCLast);
			}
		}

		private boolean isValidCRun() {
			int cDelta = Math.abs(cRunForCLast - cLoadForCLast);
			return 65 <= cDelta && cDelta <= 119 && cDelta != 96 && cDelta != 112;
		}

	}

}
