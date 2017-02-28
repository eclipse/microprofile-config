/*
 * Copyright (c) 2016-2017 Payara Services Ltd. and others
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
package org.eclipse.microprofile.config;

/**
 * Accessor to a configured value. Suitable when a configured value should be
 * accessed dynamically or additional metadata is necessary on top of the
 * configured value.
 *
 * @author Ondrej Mihalyi
 * 
 * @param <T_VALUE> Type of the configuration value
 */
public interface ConfigValue<T_VALUE> {

    /**
     * Returns the converted resolved value.
     *
     * @return the resolved value
     */
    T_VALUE getValue();
    
    /**
     * Whether the value is defined in one of the config sources or not.
     * @return True if a value for the key is defined, false otherwise
     */
    boolean isValueAvailable();

    /**
     * Returns the key used to retrieve the configuration value.
     *
     * @return the original key
     */
    String getKey();
}
