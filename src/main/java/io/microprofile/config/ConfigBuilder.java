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

import java.util.Collection;

/**
 * The builder for creating an instance of a {@code Config} by adding a number of config sources
 * @author Emily
 *
 */
public interface ConfigBuilder {
   
	/**
	 * Remove the default config sources including:
	 * <ol> 
	 * <li>System properties</li>
	 * <li>Environment properties</li>
	 * <li>/META-INF/config.properties</li>
	 * <li>/META-INF/config.xml</li>
	 * <li>/META-INF/config.json</li>
	 * </ol>
	 * 
	 * @return the ConfigBuilder without the default config sources
	 */
     ConfigBuilder removeDefaultSources();
     /**
      * Remove all config sources from the builder and return an empty config builder
      * @return an empty {@link ConfigBuilder}
      */
     ConfigBuilder removeAllSources();
     /**
      * Add the {@link ConfigSource}s to the builder
      * @param sources the {@link ConfigSource}
      * @return the builder
      */
     ConfigBuilder addSources(ConfigSource... sources);  
    
     /**
      * Add the {@link ConfigSources} to the builder.
      * @param sources
      * @return the builder
      */
     ConfigBuilder addSources(ConfigSources sources);  
    
     /**
      * Add a converter {@link Converter} to the builder
      * @param converter the converter
      * @param convererTyper the class type that the converter is supposed to convert from a string 
      * @return the builder
      */
     <T> ConfigBuilder addConverter(Converter<T> converter, Class<?> converterType);
     /**
      * Return the collection of {@link ConfigSource}s.
      * @return the collection of {@link ConfigSource}s.
      */
     public Collection<ConfigSource> getSources();
     /**
      * Return the collection of {@link Converter}s.
      * @return the collection of {@link Converter}s.
      */
     public Collection<Converter<?>> getConverters();
     
     /**
      * Aggregate all resources and sort them according to their ordinal and then build a {@link Config} object. 
      * @return the {@link Config} object
      */
     Config build();
    
}
