/*
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
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
 *   2016-07-20 - Romain Manni-Bucau
 *      Initial ConfigBuilder PR    0945b23cbf9dadb75fb9
 *   2016-07-17 - Mark Struberg
 *      Merged and JavaDoc          c8525998a43fe798f367
 *   2016-11-14 - Emily Jiang / IBM
 *      API improvements + JavaDoc  f53258b8eca1253fee52
 */
package org.eclipse.microprofile.config.spi;

import org.eclipse.microprofile.config.Config;

/**
 * A builder for manually creating a configuration instance.
 *
 * @see ConfigProviderResolver#getBuilder()
 *
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
@org.osgi.annotation.versioning.ProviderType
public interface ConfigBuilder {
    /**
     * Add the <a href="ConfigSource.html#default_config_sources"><em>default configuration sources</em></a> to the
     * configuration being built.
     *
     * @return this configuration builder instance
     */
    ConfigBuilder addDefaultSources();

    /**
     * Add all configuration sources which can be <a href="ConfigSource.html#discovery">discovered</a> from this
     * configuration builder's {@linkplain #forClassLoader(ClassLoader) class loader}.
     *
     * @return this configuration builder instance
     */
    ConfigBuilder addDiscoveredSources();

    /**
     * Add all configuration converters which can be <a href="Converter.html#discovery">discovered</a> from this
     * configuration builder's {@linkplain #forClassLoader(ClassLoader) class loader}.
     *
     * @return this configuration builder instance
     */
    ConfigBuilder addDiscoveredConverters();

    /**
     * Specify the class loader for which this configuration is being built.
     *
     * @param loader
     *            the class loader
     * @return this configuration builder instance
     */
    ConfigBuilder forClassLoader(ClassLoader loader);

    /**
     * Add the specified {@link ConfigSource} instances to the configuration being built.
     *
     * @param sources
     *            the configuration sources
     * @return this configuration builder instance
     */
    ConfigBuilder withSources(ConfigSource... sources);

    /**
     * Add the specified {@link Converter} instances to the configuration being built.
     * <p>
     * The implementation may use reflection to determine the target type of the converter. If the type cannot be
     * determined reflectively, this method may fail with a runtime exception.
     * <p>
     * When using lambda expressions for custom converters you should use the
     * {@link #withConverter(Class, int, Converter)} method and pass the target type explicitly, since lambda
     * expressions generally do not offer enough type information to the reflection API in order to determine the target
     * converter type.
     * <p>
     * The added converters will be given a priority of {@code 100}.
     *
     * @param converters
     *            the converters to add
     * @return this configuration builder instance
     */
    ConfigBuilder withConverters(Converter<?>... converters);

    /**
     * Add the specified {@link Converter} instance for the given type to the configuration being built.
     * <p>
     * This method does not rely on reflection to determine the target type of the converter; therefore, lambda
     * expressions may be used for the converter instance.
     * <p>
     * The priority value of custom converters defaults to {@code 100} if not specified.
     *
     * @param type
     *            the class of the type to convert
     * @param priority
     *            the priority of the converter
     * @param converter
     *            the converter (can not be {@code null})
     * @param <T>
     *            the type to convert
     * @return this configuration builder instance
     */
    <T> ConfigBuilder withConverter(Class<T> type, int priority, Converter<T> converter);

    /**
     * Build a new {@link Config} instance based on this builder instance.
     *
     * @return the new configuration object
     */
    Config build();
}
