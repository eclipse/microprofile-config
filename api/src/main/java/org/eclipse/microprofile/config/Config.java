/*
 *******************************************************************************
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
 *
 *******************************************************************************/

package org.eclipse.microprofile.config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * <p>
 * Resolves the property value by searching through all configured
 * {@link ConfigSource ConfigSources}. If the same property is specified in multiple
 * {@link ConfigSource ConfigSources}, the value in the {@link ConfigSource} with the highest
 * ordinal will be used.
 * <p>If multiple {@link ConfigSource ConfigSources} are specified with
 * the same ordinal, the {@link ConfigSource#getName()} will be used for sorting.
 * <p>
 * The config objects produced via the injection model {@code @Inject Config} are guaranteed to be serializable, while
 * the programmatically created ones are not required to be serializable.
 * <p>
 * If one or more converters are registered for a class of a requested value then the registered {@link org.eclipse.microprofile.config.spi.Converter}
 * which has the highest {@code @javax.annotation.Priority} is used to convert the string value retrieved from the config sources.
 *
 * <h2>Usage</h2>
 *
 * For accessing the config you can use the {@link ConfigProvider}:
 *
 * <pre>
 * public void doSomething(
 *   Config cfg = ConfigProvider.getConfig();
 *   String archiveUrl = cfg.getValue("my.project.archive.endpoint", String.class);
 *   Integer archivePort = cfg.getValue("my.project.archive.port", Integer.class);
 * </pre>
 *
 * <p>It is also possible to inject the Config if a DI container is available:
 *
 * <pre>
 * public class MyService {
 *     &#064;Inject
 *     private Config config;
 * }
 * </pre>
 *
 * <p>See {@link #getValue(String, Class)} and {@link #getOptionalValue(String, Class)} for accessing a configured value.
 *
 * <p>Configured values can also be accessed via injection.
 * See {@link org.eclipse.microprofile.config.inject.ConfigProperty} for more information.
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
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive
     * to compute; therefore, if the returned value is intended to be frequently used, callers should consider storing
     * rather than recomputing it.
     *
     * @param <T>
     *             The property type
     * @param propertyName
     *             The configuration property name
     * @param propertyType
     *             The type into which the resolved property value should get converted
     * @return the resolved property value as an instance of the requested type
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property isn't present in the configuration
     */
    <T> T getValue(String propertyName, Class<T> propertyType);

    /**
     * Return the {@link ConfigValue} for the specified property name from the underlying
     * {@linkplain ConfigSource configuration source}. The lookup of the configuration is performed immediatily,
     * meaning that calls to {@link ConfigValue} will always yeld the same results.
     * <p>
     *
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive
     * to compute; therefore, if the returned value is intended to be frequently used, callers should consider storing
     * rather than recomputing it.
     * <p>
     *
     * A {@link ConfigValue} is always returned even if a property name cannot be found. In this case, every method in
     * {@link ConfigValue} returns {@code null} except for {@link ConfigValue#getName()}, which includes the original
     * property name being looked up.
     *
     * @param propertyName
     *              The configuration property name
     * @return the resolved property value as a {@link ConfigValue}
     */
    ConfigValue getConfigValue(String propertyName);

    /**
     * Return the resolved property values with the specified type for the
     * specified property name from the underlying {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration values are not guaranteed to be cached by the implementation, and may be expensive
     * to compute; therefore, if the returned values are intended to be frequently used, callers should consider storing
     * rather than recomputing them.
     *
     * @param <T>
     *             The property type
     * @param propertyName
     *             The configuration property name
     * @param propertyType
     *             The type into which the resolved property values should get converted
     * @return the resolved property values as a list of instances of the requested type
     * @throws java.lang.IllegalArgumentException if the property values cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property isn't present in the configuration
     */
    default <T> List<T> getValues(String propertyName, Class<T> propertyType) {
        @SuppressWarnings("unchecked")
        Class<T[]> arrayType = (Class<T[]>) Array.newInstance(propertyType, 0).getClass();
        return Arrays.asList(getValue(propertyName, arrayType));
    }

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration value is not guaranteed to be cached by the implementation, and may be expensive
     * to compute; therefore, if the returned value is intended to be frequently used, callers should consider storing
     * rather than recomputing it.
     *
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T>
     *             The property type
     * @param propertyName
     *             The configuration property name
     * @param propertyType
     *             The type into which the resolved property value should be converted
     * @return The resolved property value as an {@code Optional} wrapping the requested type
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType);

    /**
     * Return the resolved property values with the specified type for the
     * specified property name from the underlying {@linkplain ConfigSource configuration sources}.
     * <p>
     * The configuration values are not guaranteed to be cached by the implementation, and may be expensive
     * to compute; therefore, if the returned values are intended to be frequently used, callers should consider storing
     * rather than recomputing them.
     *
     * @param <T>
     *             The property type
     * @param propertyName
     *             The configuration property name
     * @param propertyType
     *             The type into which the resolved property values should be converted
     * @return The resolved property values as an {@code Optional} wrapping a list of the requested type
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    default <T> Optional<List<T>> getOptionalValues(String propertyName, Class<T> propertyType) {
        @SuppressWarnings("unchecked")
        Class<T[]> arrayType = (Class<T[]>) Array.newInstance(propertyType, 0).getClass();
        return getOptionalValue(propertyName, arrayType).map(Arrays::asList);
    }

    /**
     * Return the resolved configuration properties instance with the specified prefix. 
     * The type declaration can be annotated with 
     * {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}. 
     * If the type is annotated with {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}, 
     * the prefix supplied in this method overrides the prefix associated with the annotation 
     * {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}.
     * 
     * @param <T> 
     *              The Class Type
     * @param configProperties
     *              The class that contains a number of fields that maps to corresponding configuration properties. 
     *              The type declaration can be annotated with 
     *              {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}.
     *              The prefix as the method parameter overrides the prefix set on the type.                
     *              This class should contain a zero-arg constructor. Otherwise, the behaviour is unspecified.
     * @param prefix
     *              The prefix for the configuration properties declared on the class configProperties.
     *              If the prefix is "", which means no prefix involved when performing property lookup.
     *              If the prefix is null, this method is equivalent to {@linkplain #getConfigProperties(Class)}.
     * @return      An instance for the specified type and prefix 
     */
    <T> T getConfigProperties(Class<T> configProperties, String prefix);

    /**
     * Return the resolved configuration properties instance. The type can be annotated with 
     * {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}. 
     * If the type is annotated with {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}, 
     * the prefix associated with the annotation {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties} will be used.
     * @param <T> 
     *              The Class Type
     * @param configProperties
     *              The class that contains a number of fields that maps to corresponding configuration properties. 
     *              The type declaration can be annotated with 
     *              {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}. 
     *              If the type is annotated with {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties}, 
     *              the prefix on the annotation {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties} 
     *              will be honoured. 
     *              The absence of the annotation {@linkplain org.eclipse.microprofile.config.inject.ConfigProperties @ConfigProperties} 
     *              or the absence of the prefix on the annotation means no prefix specified. 
     *              This class should contain a zero-arg constructor. Otherwise, the behaviour is unspecified.
     * @return      An instance for the specified type
     */
    <T> T getConfigProperties(Class<T> configProperties);

    /**
     * Returns a sequence of configuration property names. The order of the returned property names is unspecified.
     * <p>
     * The returned property names are unique; that is, if a name is returned once by a given iteration, it will not be
     * returned again during that same iteration.
     * <p>
     * There is no guarantee about the completeness or currency of the names returned, nor is there any guarantee that a
     * name that is returned by the iterator will resolve to a non-empty value or be found in any configuration source
     * associated with the configuration; for example, it is allowed for this method to return an empty set always.
     * However, the implementation <em>should</em> return a set of names that is useful to a
     * user that wishes to browse the configuration.
     * <p>
     * It is implementation-defined whether the returned names reflect a point-in-time "snapshot" of names, or an
     * aggregation of multiple point-in-time "snapshots", or a more dynamic view of the available property names.
     * Implementations are not required to return the same sequence of names on each iteration; however, the produced
     * {@link java.util.Iterator Iterator} must adhere to the contract of that class, and must not return any more
     * elements once its {@link java.util.Iterator#hasNext() hasNext()} method returns {@code false}.
     * <p>
     * The returned instance is thread safe and may be iterated concurrently.  The individual iterators are not
     * thread-safe.
     *
     * @return the names of all configured keys of the underlying configuration.
     */
    Iterable<String> getPropertyNames();

    /**
     * Return all of the currently registered {@linkplain ConfigSource configuration sources} for this configuration.
     * The returned sources will be sorted by descending ordinal value and name, which can be iterated in a thread-safe manner.
     *
     * @return the configuration sources
     */
    Iterable<ConfigSource> getConfigSources();

    /**
     * Return the {@link Converter} used by this instance to produce instances of the specified type from string values.
     *
     * @param <T>
     *             the conversion type
     * @param forType
     *             the type to be produced by the converter
     * @return an {@link Optional} containing the converter, or empty if no converter is available for the specified type
     */
    <T> Optional<Converter<T>> getConverter(Class<T> forType);
}
