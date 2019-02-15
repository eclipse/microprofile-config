/*
 * Copyright (c) 2016-2019 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck.configsources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;

public class KeyValuesConfigSource implements ConfigSource {


    private final Map<String, String> properties = new HashMap<>();
    private final int ordinal;

    private KeyValuesConfigSource(Map<String, String> properties, int ordinal) {
        this.properties.putAll(properties);
        this.ordinal = ordinal;
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    @Override
    public String getName() {
        return "KeyValuesConfigSource";
    }

    public static ConfigSource config(String... keyValues) {
        return config(DEFAULT_ORDINAL, keyValues);
    }

    public static ConfigSource config(int ordinal, String... keyValues) {
        if (keyValues.length %2 != 0) {
            throw new IllegalArgumentException("keyValues array must be a multiple of 2");
        }

        Map<String, String> props = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            props.put(keyValues[i], keyValues[i+1]);
        }
        return new KeyValuesConfigSource(props, ordinal);
    }

    public static Config buildConfig(String... keyValues) {
        return ConfigProviderResolver.instance().getBuilder()
            .withSources(KeyValuesConfigSource.config(keyValues))
            .build();
    }
}
