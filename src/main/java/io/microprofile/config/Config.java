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
 */

package io.microprofile.config;


import java.util.Optional;

/**
 * <p>Resolves the property value by searching through all configured ConfigSources.
 *
 * In Microprofile-config-1.0 the following search order is performed:
 *
 * <ol>
 *     <li>1. System Properties</li>
 *     <li>2. Environment Properties</li>
 *     <li>3. values in all META-INF/microprofile-config.properties files along the classpath (in undefined order!)</li>
 * </ol>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public interface Config {

    /**
     * Return the resolved property value with the specified type for the specified property name
     * @param <T> the property type
     * @param propertyName
     * @param propertyType
     * @return the resolved property value as an optional
     */
    <T> Optional<T> getValue(String propertyName, Class<T> propertyType);


    /**
     * Get an Optional string associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated Optional string.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a String.
     */
    Optional<String> getValue(String propertyName);

    /**
     * Return a collection of property names
     * @return the available property names
     */
    Iterable<String> getPropertyNames();
}
