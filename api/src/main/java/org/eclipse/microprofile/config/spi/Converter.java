/*
 ********************************************************************************
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
 *
 *******************************************************************************/

package org.eclipse.microprofile.config.spi;

/**
 * <p>Interface for converting configured values from String to any Java type.
 **
 * <p>Converters for the following types are provided by default:
 * <ul>
 *     <li>{@code Boolean}, values for {@code true}: (case insensitive)
 *     &quot;true&quot;, &quot;yes&quot;, &quot;Y&quot;, &quot;on&quot;, &quot;1&quot;</li>
 *     <li>{@code Integer}</li>
 *     <li>{@code Long}</li>
 *     <li>{@code Float}, a dot '.' is used to separate the fractional digits</li>
 *     <li>{@code Double}, a dot '.' is used to separate the fractional digits</li>
 *     <li>{@code java.time.Duration} as defined in {@link java.time.Duration#parse(CharSequence)}</li>
 *     <li>{@code java.time.LocalDateTime} as defined in {@link java.time.LocalDateTime#parse(CharSequence)}</li>
 *     <li>{@code java.time.LocalDate} as defined in {@link java.time.LocalDate#parse(CharSequence)}</li>
 *     <li>{@code java.time.LocalTime} as defined in {@link java.time.LocalTime#parse(CharSequence)}</li>
 *     <li>{@code OffsetDateTime} as defined in {@link java.time.OffsetDateTime#parse(CharSequence)}</li>
 *     <li>{@code OffsetTime} as defined in {@link java.time.OffsetTime#parse(CharSequence)}</li>
 *     <li>{@code Instant}</li>
 *
 * </ul>
 *
 * <p>Custom Converters will get picked up via the {@link java.util.ServiceLoader} mechanism and and can be registered by
 * providing a file<br>
 * <code>META-INF/services/org.eclipse.microprofile.config.spi.Converter</code><br>
 * which contains the fully qualified {@code Converter} implementation class name as content.
 *
 * <p>A Converter can specify a {@code javax.annotation.Priority}.
 * If no priority is explicitly assigned, the value of 100 is assumed.
 * If multiple Converters are registered for the same type, the one with the highest priority will be used.
 * All Built In Converters have a {@code javax.annotation.Priority} of 1
 *
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public interface Converter<T> {
    /**
     * Configure the string value to a specified type
     * @param value the string representation of a property value
     * @return the converted value or null if value is null
     *
     * @throws IllegalArgumentException if the value cannot be converted to the specified type.
     */
    T convert(String value);
}
