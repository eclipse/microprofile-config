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

/**
 * <p>Implement this interfaces to provide multiple ConfigSources.
 * This is needed if there are multiple property files of a given name.</p>
 *
 * <p>If a single ConfigSource exists, then there is no need
 * to register it using a custom implementation of ConfigSourceProvider, it can be 
 * registered directly as a {@link ConfigSource}.</p>
 *
 * <p>A ConfigSourceProvider will get picked up via the
 * {@link java.util.ServiceLoader} mechanism and can be registered via
 * META-INF/services/javax.config.spi.ConfigSourceProvider</p>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public interface ConfigSourceProvider {

    /**
     * Return the collection of {@link ConfigSource}s.
     * For each e.g. property file, we return a single ConfigSource or an empty list if no ConfigSource exists.
     *
     * @param forClassLoader the classloader which should be used if any is needed
     * @return the {@link ConfigSource ConfigSources} to register within the {@link org.eclipse.microprofile.config.Config}.
     */
    Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader);
}
