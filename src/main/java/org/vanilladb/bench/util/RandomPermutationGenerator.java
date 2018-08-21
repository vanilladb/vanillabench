/*******************************************************************************
 * Copyright 2016, 2017 vanilladb.org contributors
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
