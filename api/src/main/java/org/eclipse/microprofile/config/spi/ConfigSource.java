/*
 * Copyright (c) 2009-2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
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
 *   2009       - Mark Struberg
 *      Ordinal solution in Apache OpenWebBeans
 *   2011-12-28 - Mark Struberg & Gerhard Petracek
 *      Contributed to Apache DeltaSpike fb0131106481f0b9a8fd
 *   2016-11-14 - Emily Jiang / IBM Corp
 *      Methods renamed, JavaDoc and cleanup
 */
package org.eclipse.microprofile.config.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A <em>configuration source</em> which provides configuration values from a specific place. Some examples of
 * configuration sources may include:
 *
 * <ul>
 * <li>a JNDI-backed naming service</li>
 * <li>a properties file</li>
 * <li>a database table</li>
 * </ul>
 *
 * <p>
 * Implementations of this interface have the responsibility to get the value corresponding to a property name, and to
 * enumerate available property names.
 * <p>
 * A <em>configuration source</em> is always read-only; any potential updates of the backing configuration values must
 * be handled directly inside each configuration source instance.
 *
 * <h2 id="default_config_sources">Default configuration sources</h2>
 *
 * <p>
 * Some configuration sources are known as <em>default configuration sources</em>. These configuration sources are
 * normally available in all automatically-created configurations, and can be
 * {@linkplain ConfigBuilder#addDefaultSources() manually added} to manually-created configurations as well. The default
 * configuration sources are:
 *
 * <ol>
 * <li>{@linkplain System#getProperties() System properties}, with an {@linkplain #getOrdinal() ordinal value} of
 * {@code 400}</li>
 * <li>{@linkplain System#getenv() Environment properties}, with an ordinal value of {@code 300}</li>
 * <li>The {@code /META-INF/microprofile-config.properties} {@linkplain ClassLoader#getResource(String) resource}, with
 * an ordinal value of {@code 100}</li>
 * </ol>
 *
 * <h3>Environment variable name mapping rules</h3>
 *
 * <p>
 * Some operating systems allow only alphabetic characters or an underscore ({@code _}) in environment variable names.
 * Other characters such as {@code .}, {@code /}, etc. may be disallowed. In order to set a value for a config property
 * that has a name containing such disallowed characters from an environment variable, the following rules are used.
 *
 * <p>
 * Three environment variables are searched for a given property name (e.g. "{@code com.ACME.size}"):
 * <ol>
 * <li>The exact name (i.e. "{@code com.ACME.size}")</li>
 * <li>The name, with each character that is neither alphanumeric nor _ replaced with _ (i.e.
 * "{@code com_ACME_size}")</li>
 * <li>The name, with each character that is neither alphanumeric nor _ replaced with _ and then converted to upper case
 * (i.e. "{@code COM_ACME_SIZE}")</li>
 * </ol>
 *
 * <p>
 * The first of these environment variables that is found for a given name is returned.
 *
 * <h3 id="discovery">Configuration source discovery</h3>
 *
 * <p>
 * Discovered configuration sources are loaded via the {@link java.util.ServiceLoader} mechanism and and can be
 * registered by providing a resource named {@code META-INF/services/org.eclipse.microprofile.config.spi.ConfigSource},
 * which contains the fully qualified {@code ConfigSource} implementation class name as its content.
 *
 * <p>
 * Configuration sources may also be added by defining {@link org.eclipse.microprofile.config.spi.ConfigSourceProvider}
 * classes which are discoverable in this manner.
 *
 * <h3>Closing configuration sources</h3>
 *
 * <p>
 * If a configuration source implements the {@link AutoCloseable} interface, then its {@linkplain AutoCloseable#close()
 * close method} will be called when the underlying configuration is released.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:john.d.ament@gmail.com">John D. Ament</a>
 */
public interface ConfigSource {
    /**
     * The name of the configuration ordinal property, "{@code config_ordinal}".
     */
    String CONFIG_ORDINAL = "config_ordinal";

    /**
     * The default configuration ordinal value, {@code 100}.
     */
    int DEFAULT_ORDINAL = 100;

    /**
     * Return the properties in this configuration source as a map.
     *
     * @return a map containing properties of this configuration source
     */
    default Map<String, String> getProperties() {
        Map<String, String> props = new HashMap<>();
        getPropertyNames().forEach((prop) -> props.put(prop, getValue(prop)));
        return props;
    }

    /**
     * Gets all property names known to this configuration source, potentially without evaluating the values. The
     * returned property names may be a subset of the names of the total set of retrievable properties in this config
     * source.
     * <p>
     * The returned set is not required to allow concurrent or multi-threaded iteration; however, if the same set is
     * returned by multiple calls to this method, then the implementation must support concurrent and multi-threaded
     * iteration of that set.
     * <p>
     * The set of keys returned <em>may</em> be a point-in-time snapshot, or <em>may</em> change over time (even during
     * active iteration) to reflect dynamic changes to the available set of keys.
     *
     * @return a set of property names that are known to this configuration source
     */
    Set<String> getPropertyNames();

    /**
     * Return the ordinal priority value of this configuration source.
     * <p>
     * If a property is specified in multiple config sources, the value in the config source with the highest ordinal
     * takes precedence. For configuration sources with the same ordinal value, the configuration source name will be
     * used for sorting according to string sorting criteria.
     * <p>
     * Note that this method is only evaluated during the construction of the configuration, and does not affect the
     * ordering of configuration sources within a configuration after that time.
     * <p>
     * The ordinal values for the default configuration sources can be found
     * <a href="#default_config_sources">above</a>.
     * <p>
     * Any configuration source which is a part of an application will typically use an ordinal between 0 and 200.
     * Configuration sources provided by the container or 'environment' typically use an ordinal higher than 200. A
     * framework which intends have values overridden by the application will use ordinals between 0 and 100.
     * <p>
     * The default implementation of this method looks for a configuration property named "{@link #CONFIG_ORDINAL
     * config_ordinal}" to determine the ordinal value for this configuration source. If the property is not found, then
     * the {@linkplain #DEFAULT_ORDINAL default ordinal value} is used.
     * <p>
     * This method may be overridden by configuration source implementations to provide a different behavior.
     *
     * @return the ordinal value
     */
    default int getOrdinal() {
        String configOrdinal = getValue(CONFIG_ORDINAL);
        if (configOrdinal != null) {
            try {
                return Integer.parseInt(configOrdinal);
            } catch (NumberFormatException ignored) {

            }
        }
        return DEFAULT_ORDINAL;
    }

    /**
     * Return the value for the specified property in this configuration source.
     *
     * @param propertyName
     *            the property name
     * @return the property value, or {@code null} if the property is not present
     */
    String getValue(String propertyName);

    /**
     * The name of the configuration source. The name might be used for logging or for analysis of configured values,
     * and also may be used in {@linkplain #getOrdinal() ordering decisions}.
     * <p>
     * An example of a configuration source name is "{@code property-file mylocation/myprops.properties}".
     *
     * @return the name of the configuration source
     */
    String getName();
}
