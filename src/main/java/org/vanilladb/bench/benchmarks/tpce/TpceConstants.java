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
package org.vanilladb.bench.benchmarks.tpce;

import org.vanilladb.bench.util.BenchProperties;

public class TpceConstants {

	public static final double TARDE_ORDER_PERCENTAGE;
	
	static {
		TARDE_ORDER_PERCENTAGE = BenchProperties.getLoader(). getPropertyAsDouble(
				TpceConstants.class.getName() + ".TARDE_ORDER_PERCENTAGE", 0.8);
	}
	
	// Factors
	public final static double ACCOUNT_CUSTOMER_FACTOR = 5;
	public final static double TRADE_CUSTOMER_FACTOR = 17280;
	public final static double BROKER_CUSTOMER_FACTOR = 0.01;
	public final static double COMPANY_CUSTOMER_FACTOR = 0.5;
	public final static double SECURITY_CUSTOMER_FACTOR = 0.685;
	
	// Scaling parameters
	public final static int CUSTOMER_COUNT = 1000;
	public final static int CUSTOMER_ACCOUNT_COUNT = (int) (CUSTOMER_COUNT * ACCOUNT_CUSTOMER_FACTOR);
	public final static int BROKER_COUNT = (int) (CUSTOMER_COUNT * BROKER_CUSTOMER_FACTOR);
	public final static int COMPANY_COUNT = (int) (CUSTOMER_COUNT * COMPANY_CUSTOMER_FACTOR);
	public final static int SECURITY_COUNT = (int) (CUSTOMER_COUNT * SECURITY_CUSTOMER_FACTOR);
}
