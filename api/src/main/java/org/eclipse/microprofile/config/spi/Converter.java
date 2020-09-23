/*
 * Copyright (c) 2011-2017 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
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
 * Contributors:
 *   2015-04-30 - Ron Smeral
 *      Initially authored in Apache DeltaSpike 25b2b8cc0c955a28743f
 *   2016-07-14 - Mark Struberg
 *      JavaDoc + priority
 *   2016-12-01 - Emily Jiang / IBM Corp
 *      Marking as FunctionalInterface + JavaDoc + additional types
 */
package org.eclipse.microprofile.config.spi;

import java.io.Serializable;

/**
 * A mechanism for converting configured values from {@link String} to any Java type.
 *
 * <h2 id="global_converters">Global converters</h2>
 *
 * <p>
 * Converters may be global to a {@code Config} instance. Global converters are registered either by being
 * <a href="#discovery">discovered</a> or explicitly {@linkplain ConfigBuilder#withConverter(Class, int, Converter)
 * added to the configuration}.
 * <p>
 * Global converters are automatically applied to types that match the converter's type.
 *
 * <h3 id="built_in_converters">Built-in converters</h3>
 *
 * <p>
 * Global converters may be <em>built in</em>. Such converters are provided by the implementation. A compliant
 * implementation must provide build-in converters for <em>at least</em> the following types:
 * <ul>
 * <li>{@code boolean} and {@code Boolean}, returning {@code true} for at least the following values (case insensitive):
 * <ul>
 * <li>{@code true}</li>
 * <li>{@code yes}</li>
 * <li>{@code y}</li>
 * <li>{@code on}</li>
 * <li>{@code 1}</li>
 * </ul>
 * <li>{@code byte} and {@code Byte}, accepting (at minimum) all values accepted by the {@link Byte#parseByte(String)}
 * method</li>
 * <li>{@code short} and {@code Short}, accepting (at minimum) all values accepted by the {@link Byte#parseByte(String)}
 * method</li>
 * <li>{@code int}, {@code Integer}, and {@code OptionalInt} accepting (at minimum) all values accepted by the
 * {@link Integer#parseInt(String)} method</li>
 * <li>{@code long}, {@code Long}, and {@code OptionalLong} accepting (at minimum) all values accepted by the
 * {@link Long#parseLong(String)} method</li>
 * <li>{@code float} and {@code Float}, accepting (at minimum) all values accepted by the
 * {@link Float#parseFloat(String)} method</li>
 * <li>{@code double}, {@code Double}, and {@code OptionalDouble} accepting (at minimum) all values accepted by the
 * {@link Double#parseDouble(String)} method</li>
 * <li>{@code java.lang.Class} based on the result of {@link java.lang.Class#forName}</li>
 * <li>{@code java.lang.String}</li>
 * </ul>
 *
 * <h3 id="discovery">Global converter discovery</h3>
 *
 * <p>
 * Custom global converters may be added to a configuration via the {@link java.util.ServiceLoader} mechanism, and as
 * such can be registered by providing a {@linkplain ClassLoader#getResource(String) resource} named
 * "{@code META-INF/services/org.eclipse.microprofile.config.spi.Converter}" which contains the fully qualified
 * {@code Converter} implementation class name(s) (one per line) as content.
 * <p>
 * It is also possible to explicitly register a global converter to a {@linkplain ConfigBuilder configuration builder}
 * using the {@link ConfigBuilder#withConverters(Converter[])} and
 * {@link ConfigBuilder#withConverter(Class, int, Converter)} methods.
 *
 * <h3 id="implicit">Implicit converters</h3>
 *
 * <p>
 * If no global converter can be found for a given type, the configuration implementation must attempt to derive an
 * <em>implicit converter</em> if any of the following are true (in order):
 *
 * <ul>
 * <li>the target type has a {@code public static T of(String)} method</li>
 * <li>the target type has a {@code public static T valueOf(String)} method</li>
 * <li>the target type has a {@code public static T parse(CharSequence)} method</li>
 * <li>the target type has a public constructor with a single parameter of type {@code String}</li>
 * <li>the target type is an array of any type corresponding to either a registered
 * <a href="#global_converters"><em>global</em></a> converter or a <a href="#built_in_converters"><em>built in</em></a>
 * or <em>implicit</em> converter</li>
 * </ul>
 *
 * <h3 id="priority">Converter priority</h3>
 *
 * <p>
 * A converter implementation class can specify a priority by way of the standard {@code javax.annotation.Priority}
 * annotation or by explicitly specifying the priority value to the appropriate
 * {@linkplain ConfigBuilder#withConverter(Class, int, Converter) builder method}.
 * <p>
 * If no priority is explicitly assigned, the default priority value of {@code 100} is assumed.
 * <p>
 * If multiple converters are registered for the same type, the one with the highest numerical priority value will be
 * used.
 * <p>
 * All <em>built in</em> Converters have a priority value of {@code 1}. <em>Implicit</em> converters are only created
 * when no other converter was found; therefore, they do not have a priority.
 *
 * <h3 id="empty">Empty values</h3>
 *
 * For all converters, the empty string {@code ""} <em>must</em> be considered an empty value. Some converters
 * <em>may</em> consider other values to be empty as well.
 * <p>
 * Implementations <em>may</em> (but are not required to) implement {@code Config.getOptionalValue()} using a
 * {@code Converter}. If so, this converter <em>must</em> return {@code Optional.empty()} for an empty input.
 *
 * <h3>Array conversion</h3>
 *
 * <p>
 * A conforming implementation must support the automatic creation of an <em>implicit</em> converter for array types.
 * This converter uses a comma ({@code U+002C ','}) as a delimiter. To allow a comma to be embedded within individual
 * array element values, it may be escaped using a backslash ({@code U+005C '\'}) character. Any escaped comma character
 * will be included as a plain comma within the single element (the backslash is discarded by the converter).
 * <p>
 * Empty elements <em>must</em> not be included in the final array. An array which would consist of only empty values
 * <em>must</em> be considered empty; the array converter <em>must</em> return {@code null} in this case.
 *
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:john.d.ament@gmail.com">John D. Ament</a>
 */
public interface Converter<T> extends Serializable {
    /**
     * Convert the given string value to a specified type. Callers <em>must not</em> pass in {@code null} for
     * {@code value}; doing so may result in a {@code NullPointerException} being thrown.
     *
     * @param value
     *            the string representation of a property value (must not be {@code null})
     * @return the converted value, or {@code null} if the value is empty
     * @throws IllegalArgumentException
     *             if the value cannot be converted to the specified type
     * @throws NullPointerException
     *             if the given value was {@code null}
     */
    T convert(String value) throws IllegalArgumentException, NullPointerException;
}
