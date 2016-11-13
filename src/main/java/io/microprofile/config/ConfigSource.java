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

import java.util.Map;
/**
 * <p> Represent a config source, which provides properties. The config source includes: properties, xml, json files or datastore. <p>
 * The default config sources:
 * <ol> 
 * <li>System properties (ordinal=400)</li>
 * <li>Environment properties (ordinal=300)</li>
 * <li>/META-INF/config.properties (ordinal=100)</li>
 * <li>/META-INF/config.xml (ordinal=100)</li>
 * <li>/META-INF/config.json (ordinal=100)</li>
 * </ol>
 * The other custom config source can be added programmatically via {@link ConfigBuilder}. 
 * @author Emily
 *
 */
public interface ConfigSource {
	/**
	 * Return the properties in this config source
	 * @return the map containing the properties in this config source
	 */
	Map<String, String> getProperties();
	/**
	 * Return the ordinal for this config source. The higher the more important. If a property is specified in multiple config sources, the value 
	 * in the config source with the highest ordinal will be used.
	 * The ordinal for the default config sources:
	 *  <ol> 
	 * <li>System properties (ordinal=400)</li>
	 * <li>Environment properties (ordinal=300)</li>
	 * <li>/META-INF/config.properties (ordinal=100)</li>
	 * <li>/META-INF/config.xml (ordinal=100)</li>
	 * <li>/META-INF/config.json (ordinal=100)</li>
	 * </ol>
	 * @return the ordinal value
	 */
	int getOrdinal();
	/**
	 * Return the value for the specified property in this config source.
	 * @param propertyName the property name
	 * @return the property value 
	 */
	String getProperty(String propertyName);

}
