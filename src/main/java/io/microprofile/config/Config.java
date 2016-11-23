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
 *******************************************************************************/

package io.microprofile.config;

import io.microprofile.config.spi.ConfigSource;

import java.util.Collection;

/**
 * <p> Resolves the property value by searching through all configured {@link ConfigSource}s. 
 * If the same property is specified in multiple {@link ConfigSource}s, the value in the {@link ConfigSource} with the 
 * highest ordinal will be used. If multiple {@link ConfigSource}s are specified with the same ordinal, non portable behaviour will occur.<p>
 * 
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public interface Config {	
	
	/**
	 * Return the resolved property value with the specified type for the specified property name
	 * @param propertyName 
	 * @param propertyType
	 * @return the resolved property value or {@code null} if the property does not exist.
	 */
	<T> T getPropertyValue(String propertyName, Class<T> propertyType);
	


	/**
	 * Return the resolved property value with the specified type for the specified property name or
	 * defaultValue if the property does not exist.
	 * @param propertyName the property name
	 * @param propertyType the property type
	 * @param defaultValue the default value
	 * @return the property value or the defult value if the property does not exist.
	 */
	<T> T getPropertyValue(String propertyName, Class<T> propertyType, T defaultValue);
	
    /**
     * Get a Boolean associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated boolean or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Boolean.
     */
    boolean getBoolean(String propertyName);

    /**
     * Get a boolean associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated boolean.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Boolean.
     */
    boolean getBoolean(String propertyName, boolean defaultValue);

    /**
     * Get an integer associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated integer or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Integer.
     */
    int getInt(String propertyName);

    /**
     * Get a int associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated int.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Integer.
     */
    int getInt(String propertyName, int defaultValue);

    
    /**
     * Get a string associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated string or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a String.
     */
    String getString(String propertyName);

    
    /**
     * Get a string associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated string if propertyName is found and has valid
     *         format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a String.
     */
    String getString(String propertyName, String defaultValue);
	/**
	 * Return a collection of property names
	 * @return the property names
	 */
	Collection<String> 	getPropertyNames();
	/**
     * Filter the configured value.
     * This can e.g. be used for decryption.
     * @return the filtered value
     */
    String filterConfigValue(String key, String value);

    /**
     * Filter the configured value for logging.
     * This can e.g. be used for displaying ***** instead of a real password.
     * @return the filtered value
     */
    String filterConfigValueForLog(String key, String value);
    /**
     * @return all currently registered {@link ConfigSource}s
     */
    Collection<ConfigSource> getConfigSources();

}
