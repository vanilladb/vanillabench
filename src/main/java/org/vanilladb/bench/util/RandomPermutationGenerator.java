package org.vanilladb.bench.util;

public class RandomPermutationGenerator {
	private int[] a;

	public RandomPermutationGenerator(int size) {
		a = new int[size];
		for (int i = 0; i < size; i++) {
			a[i] = i + 1;
		}
	}

	public int get(int index) {
		return a[index];
	}

	// It produces the next random permutation
	public void next() {
		for (int k = a.length - 1; k > 0; k--) {
			int w = (int) Math.floor(Math.random() * (k + 1));
			int temp = a[w];
			a[w] = a[k];
			a[k] = temp;
		}

	}
}
