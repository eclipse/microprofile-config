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

package org.eclipse.microprofile.config;

import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.Optional;

/**
 * <p>
 * Resolves the property value by searching through all configured
 * {@link ConfigSource}s. If the same property is specified in multiple
 * {@link ConfigSource}s, the value in the {@link ConfigSource} with the highest
 * ordinal will be used. If multiple {@link ConfigSource}s are specified with
 * the same ordinal, non portable behaviour will occur.
 * <p>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public interface Config {
    /**
     * Return the resolved property value with the specified type for the
     * specified property name
     * 
     * @param <T>
     *             the property type
     * @param propertyName
     *             The configuration propertyName.
     * @param propertyType
     *             The type into which the resolve property value should get converted
     * @return the resolved property value as an Optional of the requested type.
     */
    <T> Optional<T> getValue(String propertyName, Class<T> propertyType);

    /**
     * Get an Optional string associated with the given configuration
     * propertyName.
     *
     * @param propertyName
     *             The configuration propertyName.
     * @return The resolved property value as a String-Optional.
     *
     * @throws IllegalArgumentException
     *             is thrown if the propertyName maps to an object that is not a
     *             String.
     */
    Optional<String> getValue(String propertyName);

    /**
     * Return a collection of property names.
     * This is
     * 
     * @return the names of all cofigured keys of the underlying configuration.
     */
    Iterable<String> getPropertyNames();

    /**
     * @return all currently registered {@link ConfigSource}s
     */
    Iterable<ConfigSource> getConfigSources();

}
