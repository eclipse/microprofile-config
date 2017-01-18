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

package io.microprofile.config.spi;

/**
 * Represent a group of {@link ConfigSource}s. <config-sources>
 * <source>http://shared:8080/config.xml</source>
 * <source>/cfg/myconf.json</source> </config-sources>
 *
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public interface ConfigSources {
    /**
    * Return the collection of {@link ConfigSource}s.
    *
    * @return the {@link ConfigSource}s specified in the file of
    *         config-sources.xml.
    */
    Iterable<ConfigSource> getConfigSources();
}
