/*******************************************************************************
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

package io.microprofile.config.spi;

import io.microprofile.config.Config;
import io.microprofile.config.ConfigProvider.ConfigBuilder;

import java.util.ServiceLoader;

/**
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

    public abstract Config getConfig();

    public abstract Config getConfig(ClassLoader loader);

    public abstract ConfigBuilder getEmptyBuilder();

    public abstract ConfigBuilder getBuilder();

    public abstract void releaseConfig(Config config);

    /**
     * Creates a ConfigProviderResolver object
     *
     * @return
     */
    public static ConfigProviderResolver instance() {
        if (instance == null) {
            synchronized (ConfigProviderResolver.class) {
            if (instance != null) {
                return instance;
            }
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = ConfigProviderResolver.class.getClassLoader();
            }

            ConfigProviderResolver newInstance = loadSpi(cl);

            if (newInstance == null) {
                throw new IllegalStateException("No ConfigProviderResolver implementation found!");
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
            ServiceLoader<ConfigProviderResolver> sl = ServiceLoader.load(ConfigProviderResolver.class, cl);
            for (ConfigProviderResolver spi : sl) {
                if (instance != null) {
                    throw new IllegalStateException("Multiple ConfigResolverProvider implementations found: " + spi.getClass().getName()
                            + " and " + instance.getClass().getName());
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