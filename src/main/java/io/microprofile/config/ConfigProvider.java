/*
 * Copyright (c) 2009-2017 Author and Authors
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
 */


package io.microprofile.config;

import io.microprofile.config.spi.ConfigProviderResolver;

/**
 * <p>This is the central class to access a {@link Config}.</p>
 *
 * <p>A {@link Config} contains the configuration for a certain Application</p>
 **
 * <p>Example usage:
 *
 * <pre>
 *     String restUrl = ConfigProvider.getConfig().getValue("myproject.some.remote.service.url");
 *     Integer port = ConfigProvider.getConfig().getValue("myproject.some.remote.service.port", Integer.class);
 * </pre>
 *
 * </p>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public class ConfigProvider {

    private static final ConfigProviderResolver instance = ConfigProviderResolver.instance();

    /**
     * Provide a {@link Config} for the current Application.
     *
     * <p>There is exactly one single Config instance per Application</p>
     */
    public static Config getConfig() {
        return instance.getConfig();
    }

    /**
     * Provide a {@link Config} for the Application identified by
     * the specified ClassLoader.
     *
     * <p>There is exactly one single Config instance per identified Application</p>
     */
    public static Config getConfig(ClassLoader cl) {
        return instance.getConfig(cl);
    }


    /**
     * A {@link Config} normally gets released if the ClassLoader it represents gets destroyed.
     * Invoke this method if you like to destroy the Config prematurely.
     */
    public static void releaseConfig(Config config) {
        instance.releaseConfig(config);
    }
}
