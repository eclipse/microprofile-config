/*
 * Copyright (c) 2016-2020 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.microprofile.config.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.eclipse.microprofile.config.Config;

import aQute.bnd.annotation.spi.ServiceConsumer;

/**
 * The service provider for implementations of the MicroProfile Configuration specification.
 * <p>
 * This class is not intended to be used by end-users.
 * <p>
 * The implementation of this class should register itself via the {@link java.util.ServiceLoader} mechanism.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */

/*
 * The @ServiceConsumer annotation adds support for Service Loader Mediator in order to support wiring of Service Loader
 * providers to consumers in OSGi. However, the requirements generated are specified as effective:=active to prevent
 * this from being a strict requirement. As such the API is usable in runtimes without a Service Loader Mediator
 * implementation while allowing for such to be enabled when using the resolver during assembly.
 */
@ServiceConsumer(value = ConfigProviderResolver.class, effective = "active")
public abstract class ConfigProviderResolver {
    /**
     * Construct a new instance.
     */
    protected ConfigProviderResolver() {
    }

    private static volatile ConfigProviderResolver instance = null;

    /**
     * Get the configuration instance for the current application in the manner described by
     * {@link org.eclipse.microprofile.config.ConfigProvider#getConfig()}.
     *
     * @return the configuration instance
     */
    public abstract Config getConfig();

    /**
     * Get the configuration instance for the current application in the manner described by
     * {@link org.eclipse.microprofile.config.ConfigProvider#getConfig(ClassLoader)}.
     *
     * @param loader
     *            the class loader identifying the application
     * @return the configuration instance
     */
    public abstract Config getConfig(ClassLoader loader);

    /**
     * Create a {@link ConfigBuilder} instance for the {@linkplain Thread#getContextClassLoader() current application}.
     * <p>
     * The returned configuration builder must initially contain no registered configuration sources.
     * <p>
     * The returned configuration builder must initially contain only <a href="Converter.html#built_in_converters">built
     * in converters</a>.
     *
     * @return a new configuration builder instance
     */
    public abstract ConfigBuilder getBuilder();

    /**
     * Register the given {@link Config} instance to the application identified by the given class loader. If the class
     * loader is {@code null}, then the current application (as identified by the
     * {@linkplain Thread#getContextClassLoader() thread context class loader}) will be used.
     *
     * @param config
     *            the configuration to register
     * @param classLoader
     *            the class loader identifying the application
     * @throws IllegalStateException
     *             if there is already a configuration registered for the application
     */
    public abstract void registerConfig(Config config, ClassLoader classLoader);

    /**
     * A {@link Config} normally gets released if the Application it is associated with gets destroyed. Invoke this
     * method if you like to destroy the Config prematurely.
     *
     * If the given Config is associated within an Application then it will be unregistered.
     * 
     * @param config
     *            the config to be released
     */
    public abstract void releaseConfig(Config config);

    /**
     * Find and return the provider resolver instance. If the provider resolver instance was already found, or was
     * manually specified, that instance is returned. Otherwise, {@link ServiceLoader} is used to locate the first
     * implementation that is visible from the class loader that defined this class.
     *
     * @return the provider resolver instance
     */
    public static ConfigProviderResolver instance() {
        if (instance == null) {
            synchronized (ConfigProviderResolver.class) {
                if (instance != null) {
                    return instance;
                }
                instance = loadSpi(ConfigProviderResolver.class.getClassLoader());
            }
        }

        return instance;
    }

    private static ConfigProviderResolver loadSpi(ClassLoader cl) {
        ServiceLoader<ConfigProviderResolver> sl = ServiceLoader.load(
                ConfigProviderResolver.class, cl);
        final Iterator<ConfigProviderResolver> iterator = sl.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        throw new IllegalStateException(
                "No ConfigProviderResolver implementation found!");
    }

    /**
     * Set the instance. It is used by OSGi environments that do not support
     * <a href="https://osgi.org/specification/osgi.cmpn/7.0.0/service.loader.html">the service loader</a> pattern.
     * <p>
     * Note that calling this method after a different provider instance was {@linkplain #instance() already retrieved}
     * can lead to inconsistent results. Mixing usage of this method with the service loader pattern is for this reason
     * strongly discouraged.
     *
     * @param resolver
     *            the instance to set, or {@code null} to unset the instance
     */
    public static void setInstance(ConfigProviderResolver resolver) {
        instance = resolver;
    }
}
