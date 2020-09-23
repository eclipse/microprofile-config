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
 *   2016-07-14 - Mark Struberg
 *      Initial revision            cf41cf130bcaf5447ff8
 *   2016-07-20 - Romain Manni-Bucau
 *      Initial ConfigBuilder PR    0945b23cbf9dadb75fb9
 *   2016-11-14 - Emily Jiang / IBM Corp
 *      SPI reworked into own ConfigProviderResolver
 *   2016-12-02 - Viktor Klang
 *      removed ConfigFilter and security related functionality.
 */
package org.eclipse.microprofile.config;

import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

/**
 * This is the central class to access a {@link Config}.
 * <p>
 * A {@link Config} provides access to the application's configuration. It may have been automatically discovered, or
 * manually created and registered.
 * <p>
 * The default usage is to use {@link #getConfig()} to automatically pick up the <em>configuration</em> for the current
 * thread's {@linkplain Thread#getContextClassLoader() context class loader}.
 * <p>
 * A <em>configuration</em> consists of information collected from the registered
 * <em>{@linkplain org.eclipse.microprofile.config.spi.ConfigSource configuration sources}</em>, combined with the set
 * of registered {@linkplain org.eclipse.microprofile.config.spi.Converter converters}. The <em>configuration
 * sources</em> get sorted according to their
 * <em>{@linkplain org.eclipse.microprofile.config.spi.ConfigSource#getOrdinal() ordinal value}</em>. Thus it is
 * possible to override a lower-priority <em>configuration source</em> with a higher-priority one.
 * <p>
 * It is also possible to register custom <em>configuration sources</em> to flexibly extend the configuration mechanism.
 * For example, a configuration source could be provided which reads configuration values from a database table.
 *
 * <p>
 * Example:
 * 
 * <pre>
 * String restUrl = ConfigProvider.getConfig().getValue(&quot;myproject.some.remote.service.url&quot;, String.class);
 * Integer port = ConfigProvider.getConfig().getValue(&quot;myproject.some.remote.service.port&quot;, Integer.class);
 * </pre>
 *
 * <p>
 * For more advanced use cases (e.g. registering a manually created {@link Config} instance), please see
 * {@link ConfigProviderResolver#registerConfig(Config, ClassLoader)} and {@link ConfigProviderResolver#getBuilder()}.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:viktor.klang@gmail.com">Viktor Klang</a>
 */
public final class ConfigProvider {
    private ConfigProvider() {
    }

    /**
     * Get the {@linkplain Config configuration} corresponding to the current application, as defined by the calling
     * thread's {@linkplain Thread#getContextClassLoader() context class loader}.
     * <p>
     * The {@link Config} instance will be created and registered to the context class loader if no such configuration
     * is already created and registered.
     * <p>
     * Each class loader corresponds to exactly one configuration.
     *
     * @return the configuration instance for the thread context class loader
     */
    public static Config getConfig() {
        return ConfigProviderResolver.instance().getConfig();
    }

    /**
     * Get the {@linkplain Config configuration} for the application corresponding to the given class loader instance.
     * <p>
     * The {@link Config} instance will be created and registered to the given class loader if no such configuration is
     * already created and registered.
     * <p>
     * Each class loader corresponds to exactly one configuration.
     *
     * @param cl
     *            the Classloader used to register the configuration instance
     * @return the configuration instance for the given class loader
     */
    public static Config getConfig(ClassLoader cl) {
        return ConfigProviderResolver.instance().getConfig(cl);
    }
}
