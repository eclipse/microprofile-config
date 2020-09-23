/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.microprofile.config;

/**
 * The ConfigValue holds additional information after the lookup of a configuration property and is itself immutable.
 * <p>
 * Holds information about the configuration property name, configuration value, the
 * {@link org.eclipse.microprofile.config.spi.ConfigSource} name from where the configuration property was loaded and
 * the ordinal of the {@link org.eclipse.microprofile.config.spi.ConfigSource}.
 * <p>
 * This is used together with {@link Config} to expose the configuration property lookup metadata.
 *
 * @since 2.0
 * @author <a href="mailto:radcortez@yahoo.com">Roberto Cortez</a>
 */
public interface ConfigValue {
    /**
     * The name of the property.
     *
     * @return the name of the property.
     */
    String getName();

    /**
     * The value of the property lookup with transformations (expanded, etc).
     *
     * @return the value of the property lookup or {@code null} if the property could not be found
     */
    String getValue();

    /**
     * The value of the property lookup without any transformation (expanded , etc).
     *
     * @return the raw value of the property lookup or {@code null} if the property could not be found.
     */
    String getRawValue();

    /**
     * The {@link org.eclipse.microprofile.config.spi.ConfigSource} name that loaded the property lookup.
     *
     * @return the ConfigSource name that loaded the property lookup or {@code null} if the property could not be found
     */
    String getSourceName();

    /**
     * The {@link org.eclipse.microprofile.config.spi.ConfigSource} ordinal that loaded the property lookup.
     *
     * @return the ConfigSource ordinal that loaded the property lookup or {@code 0} if the property could not be found
     */
    int getSourceOrdinal();
}
