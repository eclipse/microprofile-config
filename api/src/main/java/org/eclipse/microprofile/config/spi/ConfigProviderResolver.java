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
 *******************************************************************************/

package org.eclipse.microprofile.config.spi;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ServiceLoader;

import org.eclipse.microprofile.config.Config;

/**
 * Way to access automatically created configurations, this can be seems as a configuration instances manager.
 *
 * Note: it is ClassLoader based to lookup the right Config instance so setConfig() and lookup() need to match.
 *
 * Service provider for ConfigProviderResolver. The implementation registers
 * itself via {@link java.util.ServiceLoader} mechanism.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public abstract class ConfigProviderResolver {
    protected ConfigProviderResolver() {
    }

    private static volatile ConfigProviderResolver instance = null;

    /**
     * Provide a {@link Config} based on all {@link ConfigSource ConfigSources} of the
     * current Thread Context ClassLoader (TCCL)
     * The {@link Config} will be stored for future retrieval.
     * <p>
     * There is exactly a single Config instance per ClassLoader
     * </p>
     *
     * If no instance is provided it will call
     * <code>
     *     ConfigProvider.newBuilder().addDefaultSources().build();
     * </code>
     * to get a new one.
     */
    public static Config lookup() {
        return instance().getConfig();
    }


    /**
     * Provide a {@link Config} based on all {@link ConfigSource ConfigSources} of the
     * specified ClassLoader
     *
     * <p>
     * There is exactly a single Config instance per ClassLoader
     * </p>
     *
     * If no instance is provided it will call
     * <code>
     *     ConfigProvider.newBuilder().addDefaultSources().build();
     * </code>
     * to get a new one.
     */
    public static Config lookup(ClassLoader loader) {
        return instance().getConfig(loader);
    }

    /**
     * @see ConfigProviderResolver#lookup()
     */
    public abstract Config getConfig();

    /**
     * @see ConfigProviderResolver#lookup(ClassLoader)
     */
    public abstract Config getConfig(ClassLoader loader);

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
    public abstract void setConfig(Config config, ClassLoader classLoader);

    /**
     * A {@link Config} normally gets released if the Application it is associated with gets destroyed.
     * Invoke this method if you like to destroy the Config prematurely.
     *
     * If the given Config is associated within an Application then it will be unregistered.
     */
    public abstract void releaseConfig(Config config);

    /**
     * Creates a ConfigProviderResolver object
     * Only used internally from within {@link org.eclipse.microprofile.config.ConfigProvider}
     */
    public static ConfigProviderResolver instance() {
        if (instance == null) {
            synchronized (ConfigProviderResolver.class) {
                if (instance != null) {
                    return instance;
                }

                ClassLoader cl = getClassLoader();

                ConfigProviderResolver newInstance = loadSpi(cl);

                if (newInstance == null) {
                    throw new IllegalStateException("No ConfigProviderResolver implementation found!");
                }

                instance = newInstance;
            }
        }

        return instance;
    }

    private static ClassLoader getClassLoader() {
        ClassLoader cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
            @Override
            public ClassLoader run()  {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        if (cl == null) {
            cl = ConfigProviderResolver.class.getClassLoader();
        }
        return cl;
    }

    private static ConfigProviderResolver loadSpi(ClassLoader cl) {
        if (cl == null) {
            return null;
        }

        // todo: application provided impl should override parent instead no, otherwise no point to have this logic and
        //       just use CPR.class.getClassLoader()?

        // start from the root CL and go back down to the TCCL
        ConfigProviderResolver instance = loadSpi(cl.getParent());

        if (instance == null) {
            ServiceLoader<ConfigProviderResolver> sl = ServiceLoader.load(ConfigProviderResolver.class, cl);
            for (ConfigProviderResolver spi : sl) {
                if (instance != null) {
                    throw new IllegalStateException("Multiple ConfigResolverProvider implementations found: " + spi.getClass().getName()
                            + " and " + instance.getClass().getName());
                }
                else {
                    instance = spi;
                }
            }
        }
        return instance;
    }


    /**
     * Set the instance. It is used by OSGi environment while service loader
     * pattern is not supported.
     *
     * @param resolver
     *            set the instance.
     */
    public static void setInstance(ConfigProviderResolver resolver) {
        if (instance != null) {
            throw new IllegalStateException("Instance already set");
        }
        instance = resolver;
    }
}