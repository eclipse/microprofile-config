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
 *
 */
package org.eclipse.microprofile.config.tck.configsources;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;


/**
 * A ConfigSource which can be used to play through multiple
 * different scenarios in our TCK.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigurableConfigSource implements ConfigSource {

    private Consumer<Set<String>> reportAttributeChange;

    private Map<String, String> properties = new HashMap<>();

    @Override
    public int getOrdinal() {
        return 110;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getValue(String propertyName) {
        return properties.get(propertyName);
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public ConfigSource.ChangeSupport onAttributeChange(Consumer<Set<String>> reportAttributeChange) {
        this.reportAttributeChange = reportAttributeChange;
        return () -> ChangeSupport.Type.SUPPORTED;
    }

    public static void configure(Config cfg, String propertyName, String value) {
        for (ConfigSource configSource : cfg.getConfigSources()) {
            if (configSource instanceof ConfigurableConfigSource) {
                ConfigurableConfigSource configurableConfigSource = (ConfigurableConfigSource) configSource;
                configurableConfigSource.properties.put(propertyName, value);
                configurableConfigSource.reportAttributeChange.accept(Collections.singleton(propertyName));
                return;
            }
        }

        throw new IllegalStateException("This Config doesn't contain a ConfigurableConfigSource!");
    }
}
