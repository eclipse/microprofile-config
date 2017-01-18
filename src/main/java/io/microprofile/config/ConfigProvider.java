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
 *
 *******************************************************************************/

package io.microprofile.config;

import io.microprofile.config.spi.ConfigProviderResolver;
import io.microprofile.config.spi.ConfigSource;
import io.microprofile.config.spi.ConfigSources;
import io.microprofile.config.spi.Converter;

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
 * A 'Configuration' consists of the information collected from the registered
 * {@link ConfigSource}s. These {@link ConfigSource}s get sorted according to
 * their <em>ordinal</em> defined via {@link ConfigSource#getOrdinal()}. That
 * way it is possible to overwrite configuration with lower importance from
 * outside.
 * </p>
 *
 * <p>
 * It is also possible to register custom {@link ConfigSource}s to flexibly
 * extend the configuration mechanism. An example would be to pick up
 * configuration values from a database table./p>
 *
 * <p>
 * Example usage:
 *
 * <pre>
 * String restUrl = ConfigProvider.getConfig().getValue(
 * 		&quot;myproject.some.remote.service.url&quot;);
 * Integer port = ConfigProvider.getConfig().getValue(
 * 		&quot;myproject.some.remote.service.port&quot;, Integer.class);
 * </pre>
 *
 * </p>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public class ConfigProvider
{

  private static final ConfigProviderResolver instance = ConfigProviderResolver.instance();

  /**
   * Provide a {@link Config} based on all {@link ConfigSource}s of the
   * current Thread Context ClassLoader (TCCL)
   *
   * <p>
   * There is exactly a single Config instance per ClassLoader
   * </p>
   */
  public static Config getConfig()
  {
    return instance.getConfig();
  }

  /**
   * Provide a {@link Config} based on all {@link ConfigSource}s of the
   * specified Thread Context ClassLoader (TCCL)
   *
   * <p>
   * There is exactly a single Config instance per ClassLoader
   * </p>
   */
  public static Config getConfig(ClassLoader cl)
  {
    return instance.getConfig(cl);
  }

  /**
   * Create a fresh {@link ConfigBuilder} instance. This ConfigBuilder will
   * initially contain no {@link ConfigSource} nor any {@link Converters}.
   * Those have to be added manually.
   *
   * The ConfigProvider will not manage the Config instance internally
   */
  public static ConfigBuilder getEmptyBuilder()
  {
    return instance.getEmptyBuilder();
  }

  /**
   * Create a {@link ConfigBuilder} instance. This builder contains the
   * default {@link ConfigSource} and the {@link Converters}.
   * 
   * @return the ConfigBuilder instance
   */
  public static ConfigBuilder getBuilder()
  {
    return instance.getBuilder();
  }

  /**
   * A {@link Config} normally gets released if the ClassLoader it represents
   * gets destroyed. Invoke this method if you like to destroy the Config
   * prematurely.
   */
  public static void releaseConfig(Config config)
  {
    instance.releaseConfig(config);
  }

  /**
   * Builder for manually creating an instance of a {@code Config}.
   *
   * @see ConfigProvider#newConfig()
   */
  public interface ConfigBuilder
  {
    /**
     * Add the default config sources appearing on the builder's classpath
     * including:
     * <ol>
     * <li>System properties</li>
     * <li>Environment properties</li>
     * <li>/META-INF/config.properties</li>
     * <li>/META-INF/config.xml</li>
     * <li>/META-INF/config.json</li>
     * </ol>
     * The format of config.xml should follow the schema
     * http://java.sun.com/dtd/properties.dtd.
     * <ul>
     * <li>&lt;?xml version="1.0" encoding="UTF-8" standalone="no"?></li>
     * <li>&lt;!DOCTYPE properties SYSTEM
     * "http://java.sun.com/dtd/properties.dtd"></li>
     * <li>&lt;properties></li>
     * <ul>
     * <li>&lt;comment>This is an exmaple of config.xml!&lt;/comment></li>
     * <li>&lt;entry key="name">me&lt;/entry></li>
     * <li>&lt;entry key="age">24&lt;/entry></li>
     * </ul>
     * <li>&lt;/properties></li> </ul>
     * 
     * @return the ConfigBuilder without the default config sources
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
     * Add the specified {@link ConfigSources}
     * 
     * @param sources
     * @return the ConfigBuilder with the configured ConfigSources
     */
    ConfigBuilder withSources(ConfigSources... sources);

    /**
     * Add the default built-in converters
     * 
     * @return the ConfigBuilder with the default converters
     */
    ConfigBuilder addDefaultConverters();

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
     * @return
     */
    Config build();
  }
}
