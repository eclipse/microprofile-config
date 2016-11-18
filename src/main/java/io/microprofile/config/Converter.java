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

package io.microprofile.config;
/**
 * <p> A convert to convert from String to any Java type.</p>
 * @author Emily
 *
 * @param <T> the class type to be coverted to
 */
public interface Converter <T> {
	/**
	 * Configure the string value to a specified type
	 * @param value the string representation of a property value
	 * @return the converted value
	 */
	T convert(String value);
}
