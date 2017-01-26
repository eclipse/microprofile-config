/*
 * Copyright (c) 2017 Mark Struberg and others
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
 */
package org.eclipse.microprofile.config.spi.internal;

import java.util.ServiceLoader;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;

/**
 * This is the central part of the purely CDI based Configuration approach.
 *
 * It's quite messy as it has concrete classes in the API.
 * Would need to think about finding a way to hide it.
 *
 * The idea has been born in a discussion between Romain and me.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 */
public class ConfigExtension implements Extension {

    private Config config;

    public Config getConfig() {
        return config;
    }

    /**
     * This method should be used if another Extension needs the Config
     * in a BeforeBeanDiscoveryEvent.
     * This is needed since the order in which the Extensions will get invoked is random.
     *
     * @return the Config
     */
    public Config getConfig(BeanManager beanManager) {
        if (config == null) {
            buildConfig(beanManager);
        }
        return config;
    }


    // trigger the creation of the Config subsystem early on
    public void initConfig(@Observes BeforeBeanDiscovery bbd, BeanManager beanManager) {
        if (config == null) {
            buildConfig(beanManager);
        }
    }

    private void buildConfig(BeanManager beanManager) {
        ConfigBuilder configBuilder = newConfigBuilder();

        // give other Extensions a way to tweak the config, add ConfigSources etc
        // Attention those other observers must also be in Extensions!
        // Usage:
        // public void addMyConfigSource(@Observes ConfigBuilder configBuilder) { configBuilder.withConfigSource(...); }
        beanManager.fireEvent(configBuilder);

        // after that we got our ConfigBuilder filled up with all the info we need
        // we now create the Config with it
        this.config = configBuilder.build();

        // and we now send another event to the other Extensions to allow using that Config even during boot
        beanManager.fireEvent(config);
    }

    private ConfigBuilder newConfigBuilder() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ConfigBuilder.class.getClassLoader();
        }

        ConfigBuilder instance = loadConfigBuilder(cl);

        if (instance == null) {
            throw new IllegalStateException("No ConfigBuilder SPI implementation found!");
        }

        return instance;
    }

    private static ConfigBuilder loadConfigBuilder(ClassLoader cl) {
        if (cl == null) {
            return null;
        }

        // start from the root CL and go back down to the TCCL
        ConfigBuilder instance = loadConfigBuilder(cl.getParent());

        if (instance == null) {
            ServiceLoader<ConfigBuilder> sl = ServiceLoader.load(ConfigBuilder.class, cl);
            for (ConfigBuilder spi : sl) {
                if (instance != null) {
                    Logger.getLogger(ConfigExtension.class.getName())
                            .warning("Multiple ConfigBuilder SPIs found. Ignoring " + spi.getClass().getName());
                }
                else {
                    instance = spi;
                }
            }
        }
        return instance;
    }


}
