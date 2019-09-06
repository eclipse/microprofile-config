/*
 ******************************************************************************
 * Copyright (c) 2009-2019 Contributors to the Eclipse Foundation
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
 *   2011-12-28 - Mark Struberg & Gerhard Petracek
 *      Contributed to Apache DeltaSpike fb0131106481f0b9a8fd
 *   2016-07-07 - Mark Struberg
 *      Extracted the Config part out of DeltaSpike and proposed as Microprofile-Config 8ff76eb3bcfaf4fd
 *
 *******************************************************************************/
package org.eclipse.microprofile.config;


import java.util.NoSuchElementException;
import java.util.Optional;

import org.eclipse.microprofile.config.spi.Converter;

/**
 * An accessor for a configuration value.  Accessors are always immutable.  To customize
 * the behavior of a property access, methods of this class may be called (typically in a chain)
 * to derive successively further customized accessor instances.
 * <p>
 * For example, to retrieve an optional value of type {@code Integer} using a default value:
 * <pre>
 *  Optional&lt;Integer&gt; val = config.access("my.int.value")
 *    .convertedForType(Integer.class)
 *    .withDefault(123)
 *    .optional()
 *    .getValue();
 * </pre>
 * <p>
 * Intermediate accessors may be cached for reuse; however, retaining references to accessors can be expected to
 * cause an increase in memory footprint so this should only be done in cases where there is a net gain in code
 * simplicity or performance.  Some environments may have capability of inlining method calls in the chain and deleting
 * short-lived allocations; retaining intermediate accessor references might defeat this capability leading to a
 * degradation in overall performance for some specific workloads in such environments.
 * <p>
 * There are some constraints on the ways in which accessors may be derived.
 * Accessors cannot retrieve values, provide type-specific defaults, or become optional unless the accessor has an
 * associated type or converter.  Optional accessors cannot be used to create derived accessors with a different type
 * or with type-specific defaults.  Accessors with a type-specific default value cannot be used to create
 * derived accessors with a different type.
 * <p>
 * Accessor instances are serializable if (and only if) the associated configuration, the associated converter (if any),
 * and the associated type-safe default value (if any) are <em>all</em> serializable.
 * <p>
 * Accessor instances are not required to cache configuration values.  As such, if the value is needed at multiple
 * points in time, the result of {@link #getValue()} should generally be stored someplace for reuse.
 *
 * @param <T> the type of value being accessed
 *
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:tomas.langer@oracle.com">Tomas Langer</a>
 */
public interface ConfigAccessor<T> {

    /**
     * Get the associated configuration value.
     *
     * @return the configuration value
     *
     * @throws IllegalArgumentException if the conversion of the value or a string default value failed
     * @throws NoSuchElementException if the configuration value converts to an empty value, or the
     *      configuration value is not given and no default value is set, or the configuration value is not given
     *      and a string default value was set which converts to an empty value
     * @throws IllegalStateException if the accessor is not associated with a type or converter
     */
    T getValue() throws IllegalArgumentException, NoSuchElementException, IllegalStateException;

    /**
     * Get the property name associated with this accessor.
     *
     * @return the property name
     */
    String getPropertyName();

    /**
     * Get the configuration that this accessor is derived from.
     *
     * @return the configuration that this accessor is derived from
     */
    Config getConfig();

    /**
     * Get a derived accessor which is configured identically to this accessor, but which references a different
     * property name.
     *
     * @param propertyName the new property name (must not be {@code null})
     * @return the derived accessor
     */
    ConfigAccessor<T> forPropertyName(String propertyName);

    /**
     * Get a derived accessor which returns an optional value.  The resultant accessor cannot be used to derive
     * accessors with a different type or converter, or type-specific default value.
     *
     * @return the derived accessor
     * @throws IllegalStateException if the accessor is already optional, or if a type or converter is not
     *      associated with this accessor
     */
    ConfigAccessor<Optional<T>> optional() throws IllegalStateException;

    /**
     * Get a derived accessor which yields values converted by the given converter.  If this method is called on
     * an accessor that is already associated with a type or converter, the previous type or converter is discarded
     * and the new accessor will not reference it.
     *
     * @param converter the converter to use (must not be {@code null})
     * @param <U> the target type
     * @return the derived accessor
     * @throws IllegalStateException if the accessor is optional, or if the accessor is already associated with a
     *      type-specific default value
     */
    <U> ConfigAccessor<U> convertedWith(Converter<U> converter) throws IllegalStateException;

    /**
     * Get a derived accessor which yields values converted by the default converter for the given type.  If this method
     * is called on an accessor that is already associated with a type or converter, the previous type or converter is
     * discarded and the new accessor will not reference it.
     *
     * @param clazz the type of the value to return (must not be {@code null})
     * @param <U> the target type
     * @return the derived accessor
     * @throws IllegalStateException if the accessor is optional, or if the accessor is already associated with a
     *      type-specific default value, or if there is no converter available for the given type
     */
    <U> ConfigAccessor<U> convertedForType(Class<U> clazz) throws IllegalStateException;

    /**
     * Get a derived accessor which returns the given default value if the property is not found.  Any default value
     * set on this accessor will not be referenced or known by the derived accessor.
     * <p>
     * Type-safe default
     * values cannot be provided to optional accessors; to get an optional accessor with a type-safe default value,
     * the default value should be set first.
     *
     * @param defaultValue the type-specific default value
     * @return the derived accessor
     * @throws IllegalStateException if the accessor is not associated with a type or a converter, or if the accessor
     *      is optional
     */
    ConfigAccessor<T> withDefault(T defaultValue) throws IllegalStateException;

    /**
     * Get a derived accessor which returns the given default value if the property is not found.  Any default value
     * set on this accessor will not be referenced or known by the derived accessor.  The default value is converted
     * only if the property is not found.
     * <p>
     * Unlike {@link #withDefault(Object) withDefault(T)}, this method may be called on optional accessors as well as
     * accessors which are not yet associated with a type or converter.
     *
     * @param defaultValue the type-specific default value (must not be {@code null})
     * @return the derived accessor
     */
    ConfigAccessor<T> withStringDefault(String defaultValue);

    /**
     * Get a derived accessor which does not return a default value when a property is not found.  Any default value
     * set on this accessor will not be referenced or known by the derived accessor.
     *
     * @return the derived accessor
     */
    ConfigAccessor<T> withoutDefault();
}
