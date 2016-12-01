/*******************************************************************************
 * Copyright 2016 Microprofile.io
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
 * 
 *******************************************************************************/

package io.microprofile.config.spi;
/**
 * <p>A very simple interface for conversion of configuration values from String to any Java type.</p>
 *
 * <p>A Converter can specify a {@link javax.annotation.Priority}.
 * If no priority is explicitly assigned, the value of 100 is assumed.</p>
 *
 * <p>If multiple Converter get found the one with the highest priority will be used.</p>
 *
 * <p>The Converter for the following types are automatically enabled:
 * <ul>
 *     <li>Boolean, values for {@code true}: (case insensitive) &quot;true&quot;, &quot;yes&quot;, &quot;on&quot;</li>
 *     <li>Integer</li>
 *     <li>Long</li>
 *     <li>Float, a dot '.' is used to separate the fractional digits</li>
 *     <li>Double, a dot '.' is used to separate the fractional digits</>
 * </ul>
 *
 * </p>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rsmeral@jboss.org">Ron Smeral</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
@FunctionalInterface
public interface Converter <T> {
	/**
	 * Configure the string value to a specified type
	 * @param value the string representation of a property value
	 * @return the converted value
	 */
	T convert(String value) throws ConvertException;
}
