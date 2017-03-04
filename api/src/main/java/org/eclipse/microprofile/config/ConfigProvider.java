/*
 *******************************************************************************
 * Copyright (c) 2016 IBM Corp. and others
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
 *
 *******************************************************************************/

package org.eclipse.microprofile.config;

import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * <p>
 * This is the central class to access a {@link Config}.
 * </p>
 *
 * <p>
 * A {@link Config} contains the configuration for a certain situation. That
 * might be the configuration found in a certain ClassLoader or even a manually
 * created Configuration
 * </p>
 *
 * <p>
 * The default usage is to use {@link #getConfig()} to automatically pick up the
 * 'Configuration' for the Thread Context ClassLoader (See
 * {@link Thread#getContextClassLoader()}).
 * </p>
 *
 * <p>
 * A 'Configuration' consists of the information collected from the registered {@link ConfigSource ConfigSources}.
 * These {@link ConfigSource ConfigSources} get sorted according to
 * their <em>ordinal</em> defined via {@link ConfigSource#getOrdinal()}.
 * That way it is possible to overwrite configuration with lower importance from outside.
 * </p>
 *
 * <p>
 * It is also possible to register custom {@link ConfigSource ConfigSources} to flexibly
 * extend the configuration mechanism. An example would be to pick up
 * configuration values from a database table.</p>
 *
 * Example usage:
 *
 * <pre>
 * String restUrl = ConfigProvider.getConfig().getString(
 *         &quot;myproject.some.remote.service.url&quot;);
 * Integer port = ConfigProvider.getConfig().getValue(
 *         &quot;myproject.some.remote.service.port&quot;, Integer.class);
 * </pre>
 *
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public final class ConfigProvider {
    private static final ConfigProviderResolver INSTANCE = ConfigProviderResolver.instance();

    private ConfigProvider() {
    }

    /**
     * Provide a {@link Config} based on all {@link ConfigSource ConfigSources} of the
     * current Thread Context ClassLoader (TCCL)
     * The {@link Config} will be stored for future retrieval.
     * <p>
     * There is exactly a single Config instance per ClassLoader
     * </p>
     */
    public static Config getConfig() {
        return INSTANCE.getConfig();
    }

    /**
     * Provide a {@link Config} based on all {@link ConfigSource ConfigSources} of the
     * specified ClassLoader
     *
     * <p>
     * There is exactly a single Config instance per ClassLoader
     * </p>
     */
    public static Config getConfig(ClassLoader cl) {
        return INSTANCE.getConfig(cl);
    }

    /**
     * Create a fresh {@link ConfigBuilder} instance. This ConfigBuilder will
     * initially contain no {@link ConfigSource} but with default {@link Converter Converters} and auto discovered
     * {@link ConfigSource configsources} and {@link Converter converters}.
     * Other undiscovered {@link ConfigSource} and {@link Converter Converters} will have to be added manually.
     *
     * The ConfigProvider will not manage the Config instance internally
     */
    public static ConfigBuilder getBuilder() {
        return INSTANCE.getBuilder();
    }

    /**
     * Register a given {@link Config} within the Application (or Module) identified by the given ClassLoader.
     * If the ClassLoader is {@code null} then the current Application will be used.
     *
     * @param config
     *          which should get registered
     * @param classLoader
     *          which identifies the Application or Module the given Config should get associated with.
     *
     * @throws IllegalStateException
     *          if there is already a Config registered within the Application.
     *          A user could explicitly use {@link #releaseConfig(Config)} for this case.
     */
    public static void setConfig(Config config, ClassLoader classLoader) {
        INSTANCE.setConfig(config, classLoader);
    }

    /**
     * A {@link Config} normally gets released if the Application it is associated with gets destroyed.
     * Invoke this method if you like to destroy the Config prematurely.
     *
     * If the given Config is associated within an Application then it will be unregistered.
     */
    public static void releaseConfig(Config config) {
        INSTANCE.releaseConfig(config);
    }

    /**
     * Builder for manually creating an instance of a {@code Config}.
     *
     * @see ConfigProvider#getBuilder()
     */
    public interface ConfigBuilder {
        /**
         * Add the default config sources appearing on the builder's classpath
         * including:
         * <ol>
         * <li>System properties</li>
         * <li>Environment properties</li>
         * <li>/META-INF/microprofile-config.properties</li>
         * </ol>
         *
         * @return the ConfigBuilder with the default config sources
         */
        ConfigBuilder addDefaultSources();

        /**
         * Return the ConfigBuilder for a given classloader
         *
         * @param loader
         * @return the ConfigureBuilder for the given classloader
         */
        ConfigBuilder forClassLoader(ClassLoader loader);

        /**
         * Add the specified {@link ConfigSource}.
         *
         * @param sources
         * @return the ConfigBuilder with the configured sources
         */
        ConfigBuilder withSources(ConfigSource... sources);

        /**
         * Add the specified {@link Converter}
         *
         * @param converters
         * @return the ConfigBuilder with the added converters
         */
        ConfigBuilder withConverters(Converter<?>... converters);

        /**
         * Returns an instance of the specific class, allowing to access additional
         * APIs offered by specific providers.
         *
         * @param type Class representing the type to unwrap to
         * @param <T> The type to unwrap to
         * @return An instance of the given type
         * @throws IllegalArgumentException If the current provider does not
         * support unwrapping to the given type
         */
        <T> T unwrap(Class<T> type);

        /**
         * Build the {@link Config} object.
         *
         * @return the Config object
         */
        Config build();
    }
}
