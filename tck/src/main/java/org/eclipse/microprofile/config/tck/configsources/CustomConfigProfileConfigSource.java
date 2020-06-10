/*
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * @author Emily Jiang
 */
public class CustomConfigProfileConfigSource implements ConfigSource {

    private Map<String, String> configValues = new HashMap<>();

    public CustomConfigProfileConfigSource() {
        configValues.put("mp.config.profile", "test");
        configValues.put("%dev.vehicle.name", "bike");
        configValues.put("%prod.vehicle.name", "bus");
        configValues.put("%test.vehicle.name", "van");
        configValues.put("vehicle.name", "car");
    }

    @Override
    public int getOrdinal() {
        return 500;
    }

    @Override
    public String getValue(String key) {
        return configValues.get(key);
    }

    @Override
    public String getName() {
        return "customConfigProfileSource";
    }

    @Override
    public Set<String> getPropertyNames() {    
        return configValues.keySet();
    }
}
