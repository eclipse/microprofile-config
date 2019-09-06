/*
 *******************************************************************************
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
 *   2011-12-28 - Mark Struberg & Gerhard Petracek
 *      Initially authored in Apache DeltaSpike as ConfigResolver fb0131106481f0b9a8fd
 *   2015-04-30 - Ron Smeral
 *      Typesafe Config authored in Apache DeltaSpike 25b2b8cc0c955a28743f
 *   2016-07-14 - Mark Struberg
 *      Extracted the Config part out of Apache DeltaSpike and proposed as Microprofile-Config
 *   2016-11-14 - Emily Jiang / IBM Corp
 *      Experiments with separate methods per type, JavaDoc, method renaming
 *
 *******************************************************************************/

package org.eclipse.microprofile.config;

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
 * If one or more converters are registered for a class of a requested value, then the registered converter
 * which has the highest priority is used to convert the string value retrieved from config sources.
 * The highest priority means the highest priority number.
 * For more information about converters, see {@link org.eclipse.microprofile.config.spi.Converter}.
 * <p>
 * Alternatively, an explicit converter can be given.  The converter might be independent or it might wrap a converter
 * returned from the {@link #getConverter(Class)} method.
 *
 * <h3>Usage</h3>
 *
 * For accessing the config you can use the {@link ConfigProvider}:
 *
 * <pre>
 * public void doSomething() {
 *   Config cfg = ConfigProvider.getConfig();
 *   String archiveUrl = cfg.getString("my.project.archive.endpoint", String.class);
 *   Integer archivePort = cfg.getValue("my.project.archive.port", Integer.class);
 *   // ...
 * }
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
 * <p>
 * The {@code getValue(*)} and {@code getOptionalValue(*)} methods can be used for simple access of configuration values.
 * <p>
 * To access a configuration value providing a default, the {@code getValueWithDefault(*)} and {@code getOptionalValueWithDefault(*)}
 * methods allow a typed default to be specified, whereas the {@code getValueWithDefaultString(*)} and {@code getOptionalValueWithDefaultString(*)}
 * methods allow an unconverted default value string to be given.  If the value is not found in the configuration, then
 * instead of returning an empty value or throwing {@code NoSuchElementException}, these methods will return the
 * default value given on the method call.
 *
 * <p>Configured values can also be accessed via injection.
 * See {@link org.eclipse.microprofile.config.inject.ConfigProperty} for more information.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:gunnar@hibernate.org">Gunnar Morling</a>
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
@org.osgi.annotation.versioning.ProviderType
public interface Config {

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}.
     * <p>
     * If this method gets used very often, consider locally storing the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param propertyType the type into which the resolved property value should be converted (must not be {@code null})
     * @return the resolved property value as an object of the requested type
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property isn't present in the configuration, or is present
     *  but has an empty value
     */
    <T> T getValue(String propertyName, Class<T> propertyType);

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.
     * <p>
     * If this method gets used very often, consider locally storing the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param propertyType the type into which the resolved property value should be converted (must not be {@code null})
     * @param defaultValue the default value to return if no property with the given name is found
     * @return the resolved property value as an object of the requested type
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property has an empty value
     */
    <T> T getValueWithDefault(String propertyName, Class<T> propertyType, T defaultValue);

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.  The default value will be converted into the target type
     * only if the property is not found.
     * <p>
     * If this method gets used very often, consider locally storing the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param propertyType the type into which the resolved property value should be converted (must not be {@code null})
     * @param defaultValue the default value to convert and return if no property with the given name is found (must not be {@code null})
     * @return the resolved property value as an object of the requested type
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property has an empty value, or the property is not present
     *      and the default value string is empty
     */
    <T> T getValueWithStringDefault(String propertyName, Class<T> propertyType, String defaultValue);

    /**
     * Return the resolved property value with the specified converter for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}.
     * <p>
     * If this method gets used very often, consider locally storing the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param converter the converter to use to convert the value (must not be {@code null})
     * @return the resolved property value as converted by the given converter
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property isn't present in the configuration, or is present
     *  but has an empty value
     */
    <T> T getValue(String propertyName, Converter<T> converter);

    /**
     * Return the resolved property value with the specified converter for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.
     * <p>
     * If this method gets used very often, consider locally storing the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param converter the converter to use to convert the value (must not be {@code null})
     * @param defaultValue the default value to return if no property with the given name is found
     * @return the resolved property value as converted by the given converter
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property has an empty value
     */
    <T> T getValueWithDefault(String propertyName, Converter<T> converter, T defaultValue);

    /**
     * Return the resolved property value with the specified converter for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.  The default value will be converted into the target type
     * only if the property is not found.
     * <p>
     * If this method gets used very often, consider locally storing the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param converter the converter to use to convert the value (must not be {@code null})
     * @param defaultValue the default value to convert and return if no property with the given name is found (must not be {@code null})
     * @return the resolved property value as converted by the given converter
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     * @throws java.util.NoSuchElementException if the property has an empty value, or the property is not present
     *      and the default value string is empty
     */
    <T> T getValueWithStringDefault(String propertyName, Converter<T> converter, String defaultValue);

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}.  The
     * return value will be {@linkplain Optional#empty() empty} if the value is empty.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name
     * @param propertyType the type into which the resolved property value should be converted
     * @return the resolved property value as an {@code Optional} of the requested type
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType);

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.  The return value will be {@linkplain Optional#empty() empty}
     * if the value is empty or if the property is not found and the default value given is {@code null}.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param propertyType the type into which the resolved property value should be converted (must not be {@code null})
     * @param defaultValue the default value to return if no property with the given name is found
     * @return the resolved property value as an {@code Optional} of the requested type (not {@code null})
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValueWithDefault(String propertyName, Class<T> propertyType, T defaultValue);

    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.  The default value will be converted into the target type
     * only if the property is not found..  The return value will be {@linkplain Optional#empty() empty} if the value
     * is empty or if the property is not found and the default value given is empty.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name (must not be {@code null})
     * @param propertyType the type into which the resolved property value should be converted (must not be {@code null})
     * @param defaultValue the default value to convert and return if no property with the given name is found (must not be {@code null})
     * @return the resolved property value as an {@code Optional} of the requested type (not {@code null})
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValueWithStringDefault(String propertyName, Class<T> propertyType, String defaultValue);

    /**
     * Return the resolved property value with the specified converter for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}.  The
     * return value will be {@linkplain Optional#empty() empty} if the value is empty.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name
     * @param converter the converter to use to convert the value
     * @return the resolved property value as an {@code Optional} of the requested type
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValue(String propertyName, Converter<T> converter);

    /**
     * Return the resolved property value with the specified converter for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.  The return value will be {@linkplain Optional#empty() empty}
     * if the value is empty or if the property is not found and the default value given is {@code null}.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name
     * @param converter the converter to use to convert the value
     * @param defaultValue the default value to return if no property with the given name is found
     * @return the resolved property value as an {@code Optional} of the requested type
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValueWithDefault(String propertyName, Converter<T> converter, T defaultValue);

    /**
     * Return the resolved property value with the specified converter for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}, using the given
     * default value if the property is not found.  The default value will be converted into the target type
     * only if the property is not found..  The return value will be {@linkplain Optional#empty() empty} if the value
     * is empty or if the property is not found and the default value given is empty.
     * <p>
     * If this method is used very often then consider to locally store the configured value.
     *
     * @param <T> the result type
     * @param propertyName the configuration property name
     * @param converter the converter to use to convert the value
     * @param defaultValue the default value to convert and return if no property with the given name is found (must not be {@code null})
     * @return the resolved property value as an {@code Optional} of the requested type
     *
     * @throws java.lang.IllegalArgumentException if the property cannot be converted to the specified type
     */
    <T> Optional<T> getOptionalValueWithStringDefault(String propertyName, Converter<T> converter, String defaultValue);

    /**
     * Get the converter that would be used for converting instances of the given class.  The resultant converter
     * may be an implicit converter, a built-in converter, a global converter, or any other matching
     * converter registered in any other implementation-specific manner.  If no converter is known or available for the
     * given class, {@code null} is returned.
     *
     * @param <T> the value type
     * @param clazz the type class (must not be {@code null})
     * @return the converter that would be used, or {@code null} if no converter is known for the given class
     */
    <T> Converter<T> getConverter(Class<T> clazz);

    /**
     * Return all known property names.
     *
     * @return the names of all known configured keys of the underlying configuration sources
     */
    Iterable<String> getPropertyNames();

    /**
     * Return all registered {@linkplain ConfigSource configuration sources}.  The result will be sorted
     * by decreasing ordinal value.  If two sources have the same ordinal value, they will be ordered by name.
     *
     * @return all currently registered configuration sources
     */
    Iterable<ConfigSource> getConfigSources();
}
