/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
 *     IBM Corporation - initial API and implementation
 *     Emily Jiang - newly created class
 *******************************************************************************/

package io.microprofile.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collection;

/**
 * <p> Resolves the property value by searching through all configured {@link ConfigSource}s. 
 * If the same property is specified in multiple {@link ConfigSource}s, the value in the {@link ConfigSource} with the 
 * highest ordinal will be used. If multiple {@link ConfigSource}s are specified with the same ordinal, non portable behaviour will occur.<p>
 * 
 * 
 * @author Emily
 *
 */
public interface Config {	
	
	/**
	 * Return the resolved property value with the specified type for the specified property name
	 * @param propertyName 
	 * @param propertyType
	 * @return the resolved property value or {@code null} if the property does not exist.
	 */
	<T> T getProperty(String propertyName, Class<T> propertyType);
	


	/**
	 * Return the resolved property value with the specified type for the specified property name or
	 * defaultValue if the property does not exist.
	 * @param propertyName the property name
	 * @param propertyType the property type
	 * @param defaultValue the default value
	 * @return the property value or the defult value if the property does not exist.
	 */
	<T> T getProperty(String propertyName, Class<T> propertyType, T defaultValue);
	
    /**
     * Get a Boolean associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated boolean or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Boolean.
     */
    Boolean getBoolean(String propertyName);

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
     * Get a {@link Boolean} associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated boolean if propertyName is found and has valid
     *         format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Boolean.
     */
    Boolean getBoolean(String propertyName, Boolean defaultValue);

    /**
     * Get a Byte associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated byte or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Byte.
     */
    Byte getByte(String propertyName);

    /**
     * Get a byte associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated byte.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Byte.
     */
    byte getByte(String propertyName, byte defaultValue);

    /**
     * Get a {@link Byte} associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated byte if propertyName is found and has valid format, default
     *         value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a Byte.
     */
    Byte getByte(String propertyName, Byte defaultValue);

    /**
     * Get a double associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated double or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Double.
     */
    Double getDouble(String propertyName);

    /**
     * Get a double associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated double.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Double.
     */
    double getDouble(String propertyName, double defaultValue);

    /**
     * Get a {@link Double} associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated double if propertyName is found and has valid
     *         format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Double.
     */
    Double getDouble(String propertyName, Double defaultValue);

    /**
     * Get a float associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated float or {@code null} if the property does not exist.
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Float.
     */
    Float getFloat(String propertyName);

    /**
     * Get a float associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated float.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Float.
     */
    float getFloat(String propertyName, float defaultValue);

    /**
     * Get a {@link Float} associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated float if propertyName is found and has valid
     *         format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Float.
     */
    Float getFloat(String propertyName, Float defaultValue);

    /**
     * Get an integer associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated integer or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Integer.
     */
    Integer getInt(String propertyName);

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
     * Get an {@link Integer} associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated int if propertyName is found and has valid format, default
     *         value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a Integer.
     */
    Integer getInteger(String propertyName, Integer defaultValue);

    /**
     * Get a long associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated long or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Long.
     */
    Long getLong(String propertyName);

    /**
     * Get a long associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated long.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Long.
     */
    long getLong(String propertyName, long defaultValue);

    /**
     * Get a {@link Long} associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated long if propertyName is found and has valid
     * format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Long.
     */
    Long getLong(String propertyName, Long defaultValue);

    /**
     * Get a short associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated short or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Short.
     */
    Short getShort(String propertyName);

    /**
     * Get a short associated with the given configuration propertyName.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated short.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Short.
     */
    short getShort(String propertyName, short defaultValue);

    /**
     * Get a {@link Short} associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated short if propertyName is found and has valid
     *         format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an
     *         object that is not a Short.
     */
    Short getShort(String propertyName, Short defaultValue);

    /**
     * Get a {@link BigDecimal} associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated BigDecimal if propertyName is found and has valid format or {@code null} if the property does not exist.
     */
    BigDecimal getBigDecimal(String propertyName);

    /**
     * Get a {@link BigDecimal} associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName  The configuration propertyName.
     * @param defaultValue The default value.
     *
     * @return The associated BigDecimal if propertyName is found and has valid
     *         format, default value otherwise.
     */
    BigDecimal getBigDecimal(String propertyName, BigDecimal defaultValue);

    /**
     * Get a {@link BigInteger} associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     *
     * @return The associated BigInteger if propertyName is found and has valid format or {@code null} if the property does not exist.
     */
    BigInteger getBigInteger(String propertyName);

    /**
     * Get a {@link BigInteger} associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     *
     * @return The associated BigInteger if propertyName is found and has valid
     *         format, default value otherwise.
     */
    BigInteger getBigInteger(String propertyName, BigInteger defaultValue);

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
     * Get a URL associated with the given configuration propertyName.
     * If the propertyName doesn't map to an existing object, the default value
     * is returned.
     *
     * @param propertyName The configuration propertyName.
     * @param defaultValue The default value.
     * @return The associated URL if propertyName is found and has valid
     *         format, default value otherwise.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a URL.
     */
    URL getURL(String propertyName, URL defaultValue);
	
    /**
     * Get a URL associated with the given configuration propertyName or {@code null} if the property does not exist.
     *
     * @param propertyName The configuration propertyName.
     * @return The associated URL or {@code null} if the property does not exist.
     *
     * @throws IllegalArgumentException is thrown if the propertyName maps to an object that
     *         is not a URL.
     */
    URL getURL(String propertyName);

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
}
