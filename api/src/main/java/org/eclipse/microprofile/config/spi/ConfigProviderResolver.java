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
import org.eclipse.microprofile.config.ConfigProvider.ConfigBuilder;

/**
 * This class is not intended to be used by end-users but for
 * portable container integration purpose only.
 *
 * Service provider for ConfigProviderResolver. The implementation registers
 * itself via the {@link java.util.ServiceLoader} mechanism.
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
     * @see org.eclipse.microprofile.config.ConfigProvider#getConfig()
     */
    public abstract Config getConfig();

    /**
     * @see org.eclipse.microprofile.config.ConfigProvider#getConfig(ClassLoader)
     */
    public abstract Config getConfig(ClassLoader loader);

    /**
     * @see org.eclipse.microprofile.config.ConfigProvider#getBuilder()
     */
    public abstract ConfigBuilder getBuilder();

    /**
     * @see org.eclipse.microprofile.config.ConfigProvider#setConfig(Config, ClassLoader)
     */
    public abstract void setConfig(Config config, ClassLoader classLoader);

    /**
     * @see org.eclipse.microprofile.config.ConfigProvider#releaseConfig(Config)
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

                ClassLoader cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                    @Override
                    public ClassLoader run() {
                        return Thread.currentThread().getContextClassLoader();
                    }
                });
                if (cl == null) {
                    cl = ConfigProviderResolver.class.getClassLoader();
                }

                ConfigProviderResolver newInstance = loadSpi(cl);

                if (newInstance == null) {
                    throw new IllegalStateException(
                                    "No ConfigProviderResolver implementation found!");
                }

                instance = newInstance;
            }
        }

        return instance;
    }

    private static ConfigProviderResolver loadSpi(ClassLoader cl) {
        if (cl == null) {
            return null;
        }

        // start from the root CL and go back down to the TCCL
        ConfigProviderResolver instance = loadSpi(cl.getParent());

        if (instance == null) {
            ServiceLoader<ConfigProviderResolver> sl = ServiceLoader.load(
                            ConfigProviderResolver.class, cl);
            for (ConfigProviderResolver spi : sl) {
                if (instance != null) {
                    throw new IllegalStateException(
                                    "Multiple ConfigResolverProvider implementations found: "
                                                    + spi.getClass().getName() + " and "
                                                    + instance.getClass().getName());
                } else {
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
        instance = resolver;
    }
}