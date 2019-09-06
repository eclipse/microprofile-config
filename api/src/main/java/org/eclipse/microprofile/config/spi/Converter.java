/*
 ********************************************************************************
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
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
 *   2019-07-25 - David M. Lloyd
 *      JavaDoc cleanups and clarification around empty value handling
 *
 *******************************************************************************/

package org.eclipse.microprofile.config.spi;

/**
 * <p>Interface for converting configured values from String to any Java type.
 *
 * <h3>Global Converters</h3>
 *
 * Converters may be global to a {@code Config} instance.  Global converters are automatically applied to types that
 * match the converter's type.
 *
 * <p>Global converters may be <em>built in</em>.  Such converters are provided by the implementation.  A compliant
 * implementation must provide built-in converters for at least the following types:
 * <ul>
 *     <li>{@code boolean} and {@code Boolean}, returning {@code true} for at least the following values (case insensitive):
 *     <ul>
 *         <li>{@code true}</li>
 *         <li>{@code yes}</li>
 *         <li>{@code y}</li>
 *         <li>{@code on}</li>
 *         <li>{@code 1}</li>
 *     </ul>
 *     <li>{@code int} and {@code Integer}, accepting (at minimum) all values accepted by the {@link Integer#parseInt(String)} method</li>
 *     <li>{@code long} and {@code Long}, accepting (at minimum) all values accepted by the {@link Long#parseLong(String)} method</li>
 *     <li>{@code float} and {@code Float}, accepting (at minimum) all values accepted by the {@link Float#parseFloat(String)} method</li>
 *     <li>{@code double} and {@code Double}, accepting (at minimum) all values accepted by the {@link Double#parseDouble(String)} method</li>
 *     <li>{@code java.lang.Class} based on the result of {@link java.lang.Class#forName}</li>
 *     <li>{@code java.lang.String}</li>
 * </ul>
 *
 * <p>Custom global converters will get picked up via the {@link java.util.ServiceLoader} mechanism and and can be registered by
 * providing a file named {@code META-INF/services/org.eclipse.microprofile.config.spi.Converter}
 * which contains one or more fully qualified {@code Converter} implementation class names as content.
 *
 * <p>A custom global {@code Converter} implementation class may specify a {@code javax.annotation.Priority}.
 * If no priority is explicitly assigned, the value of 100 is assumed.
 * If multiple Converters are registered for the same type, the one with the highest priority will be used. Highest number means highest priority.
 *
 * <p>Custom global converters may also be registered programmatically to a configuration builder via the
 * {@link ConfigBuilder#withConverters(Converter[])} or {@link ConfigBuilder#withConverter(Class, int, Converter)} methods.
 *
 * <p>All built in converters have a {@code javax.annotation.Priority} of 1.
 * A Converter should handle null values returning either null or a valid Object of the specified type.
 *
 * <p>Converters may return {@code null}, indicating that the result of the conversion is an empty value.
 *
 * <h3>Implicit Converters</h3>
 *
 * <p>If no global converter could be found for a certain type,
 * the {@code Config} implementation must provide an <em>Implicit Converter</em>, if:
 * <ul>
 *     <li>the target type {@code T} has a {@code public static T of(String)} method, or</li>
 *     <li>the target type {@code T} has a {@code public static T valueOf(String)} method, or</li>
 *     <li>the target type {@code T} has a public constructor with a {@code String} parameter, or</li>
 *     <li>the target type {@code T} has a {@code public static T parse(CharSequence)} method</li>
 *     <li>the target type {@code T} is an array of any type that has an installed explicit, built-in, or implicit converter</li>
 * </ul>
 *
 * The implicit array converter uses a comma ({@code U+002C ','}) as a delimiter.  To allow a comma to be embedded within
 * individual elements, it may be preceded by a backslash ({@code U+005C '\'}) character.  Any such escaped comma will be
 * included within a single element (the backslash will be discarded).
 * <p>
 * The implicit array converter <em>must not</em> include empty segments within the conversion result.  An empty segment
 * is defined as a segment for which the element converter has returned {@code null}.  If no elements are included, the
 * value must be considered empty and {@code null} returned.
 *
 * <h3>Empty Values</h3>
 *
 * A {@code null} conversion result indicates that the converted value is empty.  All implicit converters defined
 * here <em>must</em> return {@code null} when converting empty values.  All built-in global converters defined here
 * <em>must</em> return {@code null} when converting empty values.  Other built-in global converters <em>should</em>
 * return {@code null} when converting empty values.
 * <p>
 * Customized user converters and certain special built-in converters may return other values to represent empty.  However, this
 * may be unexpected so the cases for such behavior should be clearly documented for each converter.
 * <p>
 * Most converters will consider an empty string value ({@code ""}) to be empty.  Some converters may yield an empty value
 * for other inputs.
 *
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:john.d.ament@gmail.com">John D. Ament</a>
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public interface Converter<T> {
    /**
     * Configure the string value to a specified type.  If the given value string converts to an empty value,
     * {@code null} may be returned to indicate that this is the case.  In particular, an empty input string
     * should normally result in a {@code null} return value.
     *
     * @param value the string representation of a property value (must not be {@code null})
     * @return the converted value or {@code null} if the result is an empty value
     *
     * @throws IllegalArgumentException if the value cannot be converted to the specified type
     */
    T convert(String value);
}
