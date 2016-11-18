/*******************************************************************************
 * Copyright 2016 Microprofile.io
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


package io.microprofile.config;

import io.microprofile.config.spi.ConfigProviderResolver;
import io.microprofile.config.spi.ConfigSource;
import io.microprofile.config.spi.ConfigSources;
import io.microprofile.config.spi.Converter;

/**
 * <p>This is the central class to access a {@link Config}.</p>
 *
 * <p>A {@link Config} contains the configuration for a certain
 * situation. That might be the configuration found in a certain ClassLoader
 * or even a manually created Configuration</p>
 *
 * <p>The default usage is to use {@link #getConfig()} to automatically
 * pick up the 'Configuration' for the Thread Context ClassLoader
 * (See {@link  Thread#getContextClassLoader()}). </p>
 *
 * <p>A 'Configuration' consists of the information collected from the registered
 * {@link ConfigSource}s. These {@link ConfigSource}s
 * get sorted according to their <em>ordinal</em> defined via {@link ConfigSource#getOrdinal()}.
 * That way it is possible to overwrite configuration with lower importance from outside.</p>
 *
 * <p>It is also possible to register custom {@link ConfigSource}s to
 * flexibly extend the configuration mechanism. An example would be to pick up configuration values
 * from a database table./p>
 *
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
     * Provide a {@link Config} based on all {@link ConfigSource}s
     * of the current Thread Context ClassLoader (TCCL)
     *
     * <p>There is exactly a single Config instance per ClassLoader</p>
     */
    public static Config getDefaultConfig() {
        return instance.getDefaultConfig();
    }

    

    /**
     * Create a fresh {@link ConfigBuilder} instance.
     * This ConfigBuilder will initially contain no
     * {@link ConfigSource} nor any {@link Converters}.
     * Those have to be added manually.
     *
     * The ConfigProvider will not manage the Config instance internally
     */
    public static ConfigBuilder emptyBuilder() {
        return instance.emptyBuilder();
    }

    /**
     * A {@link Config} normally gets released if the ClassLoader it represents gets destroyed.
     * Invoke this method if you like to destroy the Config prematurely.
     */
    public static void releaseConfig(Config config) {
        instance.releaseConfig(config);
    }


    /**
     * Builder for manually creating an instance of a {@code Config}.
     *
     * @see ConfigProvider#newConfig()
     */
    public interface ConfigBuilder {
    	/**
		 * Add the default config sources including:
		 * <ol> 
		 * <li>System properties</li>
		 * <li>Environment properties</li>
		 * <li>/META-INF/config.properties</li>
		 * <li>/META-INF/config.xml</li>
		 * <li>/META-INF/config.json</li>
		 * </ol>
		 * 
		 * @return the ConfigBuilder without the default config sources
		 */    	
    	ConfigBuilder addDefaultSources();
    	/**
    	 * Return the ConfigBuilder for a given classloader
    	 * @param loader
    	 * @return
    	 */
        ConfigBuilder forClassLoader(ClassLoader loader);
        /**
         * Add the specified {@link ConfigSource}.
         * @param sources
         * @return
         */
        ConfigBuilder withSources(ConfigSource... sources);
        /**
         * Add the specified {@link ConfigSources}
         * @param sources
         * @return
         */
        ConfigBuilder withSources(ConfigSources... sources);
        /**
         * Add the default built-in converters 
         * @return
         */
        ConfigBuilder addDefaultConverters();
        /**
         * Add the specified {@link Converter}
         * @param filters
         * @return
         */
        ConfigBuilder withConverters(Converter<?>... filters);
        /**
         * Build the {@link Config} object.
         * @return
         */
        Config build();
    }
}
