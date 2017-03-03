/*
 *******************************************************************************
 * Copyright (c) 2016-2017 Mark Struberg and others
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

import org.eclipse.microprofile.config.Config;

/**
 * Builder for manually creating an instance of a {@code Config}.
 *
 * @see ConfigProviderResolver#getBuilder()
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>

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
