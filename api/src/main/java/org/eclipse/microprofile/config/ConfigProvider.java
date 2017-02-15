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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <p>
 * This is the central class to access a {@link Config}.
 * </p>
 *
 * <p>It enables to access a {@link ConfigBuilder} to build a ready to use {@link Config} instance.</p>
 *
 * <p>
 * A {@link Config} contains the configuration for a certain situation. That
 * might be the configuration found in a certain ClassLoader or even a manually
 * created Configuration
 * </p>
 *
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
 * String restUrl = ConfigProviderResolver.lookup().getString(
 *         &quot;myproject.some.remote.service.url&quot;);
 * Integer port = ConfigProvider.getConfig().getValue(
 *         &quot;myproject.some.remote.service.port&quot;, Integer.class);
 * </pre>
 *
 * Or for a not managed or custom configuration instance:
 *
 * <pre>
 * String restUrl = ConfigProvider.newBuilde().build().getString(
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
    private ConfigProvider() {
    }

    /**
     * Create a fresh {@link ConfigBuilder} instance. This ConfigBuilder will
     * initially contain no {@link ConfigSource} but with default {@link Converter Converters} and auto discovered
     * {@link ConfigSource configsources} and {@link Converter converters}.
     * Other undiscovered {@link ConfigSource} and {@link Converter Converters} will have to be added manually.
     *
     * The ConfigProvider will not manage the Config instance internally
     */
    public static ConfigBuilder newBuilder() {
        ClassLoader cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run()  {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        if (cl == null) {
            cl = ConfigProviderResolver.class.getClassLoader();
        }

        final Iterator<ConfigBuilder> it = ServiceLoader.load(ConfigBuilder.class, cl).iterator();
        if (!it.hasNext()) {
            throw new IllegalStateException("No ConfigBuilder implementation, did you add a config-api implementation to your classpath?");
        }
        final ConfigBuilder builder = it.next();
        if (it.hasNext()) {
            throw new IllegalStateException("Found " + builder + " and " + it.next() + " as ConfigBuilder implementations");
        }
        return builder;
    }

    /**
     * Builder for manually creating an instance of a {@code Config}.
     *
     * @see ConfigProvider#newBuilder()
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
         * Build the {@link Config} object.
         *
         * @return the Config object
         */
        Config build();
    }
}
