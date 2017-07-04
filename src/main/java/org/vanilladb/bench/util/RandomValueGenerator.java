package org.vanilladb.bench.util;

import java.util.Random;

/**
 * A thread-safe random value generator.
 * 
 * @author SLMT
 *
 */
public class RandomValueGenerator {
	
	private static final String CHARSET = new String(
			"QAa0bcLdUK2eHfJgTP8XhiFj61DOklNm9nBoI5pGqYVrs3CtSuMZvwWx4yE7zR");

	private Random rng;

	public RandomValueGenerator() {
		rng = new Random();
	}

	public RandomValueGenerator(long seed) {
		rng = new Random(seed);
	}

	public Random rng() {
		return (rng);
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

		int randNum = (int) (rng.nextDouble() * total);
		for (int i = 0; i < range.length; i++) {
			randNum -= range[i];
			if (randNum <= 0) {
				result = i;
				break;
			}
		}
		return result;
	}

	/**
	 * Return an integer in the (inclusive) range [min, max].
	 */
	public int number(int min, int max) {
		if (min > max)
			throw new IllegalArgumentException();
		int value = rng.nextInt(max - min + 1);
		value += min;
		return value;
	}

	/**
	 * Return a long in the (inclusive) range [min, max].
	 */
	public long randomLongRange(long min, long max) {

		if (max <= min)
			return min;

		long width = max - min;
		return (long) (rng.nextDouble() * width) + min;
	}

	/**
	 * Return a double in the (inclusive) range [min, max], with the increase
	 * precision.
	 */
	public double randomDoubleIncrRange(double min, double max, double incr) {

		long width = (long) ((max - min) / incr);
		return randomLongRange(0, width) * incr + min;
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
	 * @return a random numeric string with length in range [minimum_length,
	 *          maximum_length].
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

	private String randomString(int minimum_length, int maximum_length,
			char base, int numCharacters) {
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
}
