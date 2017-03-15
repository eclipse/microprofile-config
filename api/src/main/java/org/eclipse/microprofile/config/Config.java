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
 * Contributors:
 *   2011-12-28 - Mark Struberg & Gerhard Petracek
 *      Initially authored in Apache DeltaSpike as ConfigResolver fb0131106481f0b9a8fd
 *   2015-04-30 - Ron Smeral
 *      Typesafe Config authored in Apache DeltaSpike 25b2b8cc0c955a28743f
 *   2016-07-14 - Mark Struberg
 *      Extracted the Config part out of Apache DeltaSpike and proposed as Microprofile-Config
 *   2016-11-14 - Emily Jiang / IBM Corp
 *      Experiments with separate methods per type, JavaDoc, method renaming
 *
 *******************************************************************************/

package org.eclipse.microprofile.config;

import java.util.Optional;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * <p>
 * Resolves the property value by searching through all configured
 * {@link ConfigSource configsources}. If the same property is specified in multiple
 * {@link ConfigSource configsources}, the value in the {@link ConfigSource} with the highest
 * ordinal will be used. If multiple {@link ConfigSource configsources} are specified with
 * the same ordinal, no defined ordering will be applied if they contain the same key.
 * <p>
 *
 * <h3>Usage</h3>
 * For accessing the config you can use the {@link ConfigProvider}:
 *
 * <pre>
 * public void doSomething(
 *   Config cfg = ConfigProvider.getConfig();
 *   String archiveUrl = cfg.getString("my.project.archive.endpoint");
 *   Integer archivePort = cfg.getValue("my.project.archive.port", Integer.class);
 * </pre>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:rsmeral@apache.org">Ron Smeral</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public interface Config {
    /**
     * Return the resolved property value with the specified type for the
     * specified property name from the underlying {@link ConfigSource ConfigSources}.
     *
     * An empty string representation is interpreted as not-existing configuration.
     *
     * If this method gets used very often then consider to locally store the configured value.
     *
     * @param <T>
     *             the property type
     * @param propertyName
     *             The configuration propertyName.
     * @param propertyType
     *             The type into which the resolve property value should get converted
     * @return the resolved property value as an Optional of the requested type.
     * The {@link IllegalArgumentException} will be thrown if the property cannot be converted to the specified type.
     */
    <T> Optional<T> getValue(String propertyName, Class<T> propertyType);

    /**
     * Get an Optional raw string value associated with the given configuration
     * propertyName.
     *
     * An empty value string is interpreted as not-existing configuration.
     *
     * @param propertyName
     *             The configuration propertyName.
     * @return The resolved property value as a String-Optional, which will bypass converters.
     *
     * @throws IllegalArgumentException
     *             is thrown if the propertyName maps to an object that is not a
     *             String.
     */
    Optional<String> getString(String propertyName);

    /**
     * Create a {@link ConfigValue} to access the underlying configuration.
     *
     * @param key the property key
     */
    ConfigValue<String> access(String key);


    /**
     * Return a collection of property names.
     * @return the names of all configured keys of the underlying configuration.
     */
    Iterable<String> getPropertyNames();

    /**
     * @return all currently registered {@link ConfigSource configsources} sorted with descending ordinal
     */
    Iterable<ConfigSource> getConfigSources();

}
