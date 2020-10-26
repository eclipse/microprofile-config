/*
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
 *   2011-12-28 - Mark Struberg & Gerhard Petracek
 *      Initially authored in Apache DeltaSpike as ConfigResolver fb0131106481f0b9a8fd
 *   2015-04-30 - Ron Smeral
 *      Typesafe Config authored in Apache DeltaSpike 25b2b8cc0c955a28743f
 *   2016-07-14 - Mark Struberg
 *      Extracted the Config part out of Apache DeltaSpike and proposed as Microprofile-Config
 *   2016-11-14 - Emily Jiang / IBM Corp
 *      Experiments with separate methods per type, JavaDoc, method renaming
 *   2018-04-04 - Mark Struberg, Manfred Huber, Alex Falb, Gerhard Petracek
 *      ConfigSnapshot added. Initially authored in Apache DeltaSpike fdd1e3dcd9a12ceed831dd
 *      Additional reviews and feedback by Tomas Langer.
 */
package org.eclipse.microprofile.config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Resolves the property value by searching through all configured {@link ConfigSource ConfigSources}. If the same
 * property is specified in multiple {@link ConfigSource ConfigSources}, the value in the {@link ConfigSource} with the
 * highest ordinal will be used.
 * <p>
 * If multiple {@link ConfigSource ConfigSources} are specified with the same ordinal, the
 * {@link ConfigSource#getName()} will be used for sorting.
 * <p>
 * The config objects produced via the injection model {@code @Inject Config} are guaranteed to be serializable, while
 * the programmatically created ones are not required to be serializable.
 * <p>
 * If one or more converters are registered for a class of a requested value then the registered
 * {@link org.eclipse.microprofile.config.spi.Converter} which has the highest {@code @javax.annotation.Priority} is
 * used to convert the string value retrieved from the config sources.
 *
 * <h2>Usage</h2>
 *
 * <p>
 * For accessing the config you can use the {@link ConfigProvider}:
 *
 * <pre>
 * public void doSomething() {
 *     Config cfg = ConfigProvider.getConfig();
 *     String archiveUrl = cfg.getValue("my.project.archive.endpoint", String.class);
 *     Integer archivePort = cfg.getValue("my.project.archive.port", Integer.class);
 * }
 * </pre>
 *
 * <p>
 * It is also possible to inject the Config if a DI container is available:
 *
 * <pre>
 * public class MyService {
 *     &#064;Inject
 *     private Config config;
 * }
 * </pre>
 *
 * <p>
 * See {@link #getValue(String, Class)} and {@link #getOptionalValue(String, Class)} for accessing a configured value.
 *
 * <p>
 * Configured values can also be accessed via injection. See
 * {@link org.eclipse.microprofile.config.inject.ConfigProperty} for more information.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:gunnar@hibernate.org">Gunnar Morling</a>
 */
@org.osgi.annotation.versioning.ProviderType
public interface Config {
    /**
     * The value of the property specifies a single active profile.
     */
    String PROFILE = "mp.config.profile";

    /**
     * The value of the property determines whether the property expression is enabled or disabled. The value
     * <code>false</code> means the property expression is disabled, while <code>true</code> means enabled.
     *
     * By default, the value is set to <code>true</code>.
     */
    String PROPERTY_EXPRESSIONS_ENABLED = "mp.config.property.expressions.enabled";

    /**
     * Return the resolved property value with the specified type for the specified property name from the underlying
     * {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive to compute;
     * therefore, if the returned value is intended to be frequently used, callers should consider storing rather than
     * recomputing it.
     * <p>
     * The result of this method is identical to the result of calling
     * {@code getOptionalValue(propertyName, propertyType).get()}. In particular, If the given property name or the
     * value element of this property does not exist, the {@link java.util.NoSuchElementException} is thrown. This
     * method never returns {@code null}.
     *
     * @param <T>
     *            The property type
     * @param propertyName
     *            The configuration property name
     * @param propertyType
     *            The type into which the resolved property value should get converted
     * @return the resolved property value as an instance of the requested type (not {@code null})
     * @throws IllegalArgumentException
     *             if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException
     *             if the property is not defined or is defined as an empty string or the converter returns {@code null}
     */
    <T> T getValue(String propertyName, Class<T> propertyType);

    /**
     * Return the {@link ConfigValue} for the specified property name from the underlying {@linkplain ConfigSource
     * configuration source}. The lookup of the configuration is performed immediately, meaning that calls to
     * {@link ConfigValue} will always yield the same results.
     * <p>
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive to compute;
     * therefore, if the returned value is intended to be frequently used, callers should consider storing rather than
     * recomputing it.
     * <p>
     * A {@link ConfigValue} is always returned even if a property name cannot be found. In this case, every method in
     * {@link ConfigValue} returns {@code null} except for {@link ConfigValue#getName()}, which includes the original
     * property name being looked up.
     *
     * @param propertyName
     *            The configuration property name
     * @return the resolved property value as a {@link ConfigValue}
     */
    ConfigValue getConfigValue(String propertyName);

    /**
     * Return the resolved property values with the specified type for the specified property name from the underlying
     * {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration values are not guaranteed to be cached by the implementation, and may be expensive to compute;
     * therefore, if the returned values are intended to be frequently used, callers should consider storing rather than
     * recomputing them.
     *
     * @param <T>
     *            The property type
     * @param propertyName
     *            The configuration property name
     * @param propertyType
     *            The type into which the resolved property values should get converted
     * @return the resolved property values as a list of instances of the requested type
     * @throws java.lang.IllegalArgumentException
     *             if the property values cannot be converted to the specified type
     * @throws java.util.NoSuchElementException
     *             if the property isn't present in the configuration or is defined as an empty string or the converter
     *             returns {@code null}
     */
    default <T> List<T> getValues(String propertyName, Class<T> propertyType) {
        @SuppressWarnings("unchecked")
        Class<T[]> arrayType = (Class<T[]>) Array.newInstance(propertyType, 0).getClass();
        return Arrays.asList(getValue(propertyName, arrayType));
    }

    /**
     * Return the resolved property value with the specified type for the specified property name from the underlying
     * {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive to compute;
     * therefore, if the returned value is intended to be frequently used, callers should consider storing rather than
     * recomputing it.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T>
     *            The property type
     * @param propertyName
     *            The configuration property name
     * @param propertyType
     *            The type into which the resolved property value should be converted
     * @return The resolved property value as an {@code Optional} wrapping the requested type
     *
     * @throws IllegalArgumentException
     *             if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType);

    /**
     * Return the resolved property values with the specified type for the specified property name from the underlying
     * {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration values are not guaranteed to be cached by the implementation, and may be expensive to compute;
     * therefore, if the returned values are intended to be frequently used, callers should consider storing rather than
     * recomputing them.
     *
     * @param <T>
     *            The property type
     * @param propertyName
     *            The configuration property name
     * @param propertyType
     *            The type into which the resolved property values should be converted
     * @return The resolved property values as an {@code Optional} wrapping a list of the requested type
     *
     * @throws java.lang.IllegalArgumentException
     *             if the property cannot be converted to the specified type
     */
    default <T> Optional<List<T>> getOptionalValues(String propertyName, Class<T> propertyType) {
        @SuppressWarnings("unchecked")
        Class<T[]> arrayType = (Class<T[]>) Array.newInstance(propertyType, 0).getClass();
        return getOptionalValue(propertyName, arrayType).map(Arrays::asList);
    }

    /**
     * Returns a sequence of configuration property names. The order of the returned property names is unspecified.
     * <p>
     * The returned property names are unique; that is, if a name is returned once by a given iteration, it will not be
     * returned again during that same iteration.
     * <p>
     * There is no guarantee about the completeness or currency of the names returned, nor is there any guarantee that a
     * name that is returned by the iterator will resolve to a non-empty value or be found in any configuration source
     * associated with the configuration; for example, it is allowed for this method to return an empty set always.
     * However, the implementation <em>should</em> return a set of names that is useful to a user that wishes to browse
     * the configuration.
     * <p>
     * It is implementation-defined whether the returned names reflect a point-in-time "snapshot" of names, or an
     * aggregation of multiple point-in-time "snapshots", or a more dynamic view of the available property names.
     * Implementations are not required to return the same sequence of names on each iteration; however, the produced
     * {@link java.util.Iterator Iterator} must adhere to the contract of that class, and must not return any more
     * elements once its {@link java.util.Iterator#hasNext() hasNext()} method returns {@code false}.
     * <p>
     * The returned instance is thread safe and may be iterated concurrently. The individual iterators are not
     * thread-safe.
     *
     * @return the names of all configured keys of the underlying configuration
     */
    Iterable<String> getPropertyNames();

    /**
     * Return all of the currently registered {@linkplain ConfigSource configuration sources} for this configuration.
     * <p>
     * The returned sources will be sorted by descending ordinal value and name, which can be iterated in a thread-safe
     * manner. The {@link java.lang.Iterable Iterable} contains a fixed number of {@linkplain ConfigSource configuration
     * sources}, determined at application start time, and the config sources themselves may be static or dynamic.
     * 
     * @return the configuration sources
     */
    Iterable<ConfigSource> getConfigSources();

    /**
     * Return the {@link Converter} used by this instance to produce instances of the specified type from string values.
     *
     * @param <T>
     *            the conversion type
     * @param forType
     *            the type to be produced by the converter
     * @return an {@link Optional} containing the converter, or empty if no converter is available for the specified
     *         type
     */
    <T> Optional<Converter<T>> getConverter(Class<T> forType);

    /**
     * Returns an instance of the specific class, to allow access to the provider specific API.
     * <p>
     * If the MP Config provider implementation does not support the specified class, a {@link IllegalArgumentException}
     * is thrown.
     * <p>
     * Unwrapping to the provider specific API may lead to non-portable behaviour.
     *
     * @param type
     *            Class representing the type to unwrap to
     * @param <T>
     *            The type to unwrap to
     * @return An instance of the given type
     * @throws IllegalArgumentException
     *             If the current provider does not support unwrapping to the given type
     */
    <T> T unwrap(Class<T> type);
}
