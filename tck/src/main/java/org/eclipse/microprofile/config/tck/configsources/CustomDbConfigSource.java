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

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class CustomDbConfigSource implements ConfigSource {

    private Map<String, String> configValues = new HashMap<>();

    public CustomDbConfigSource() {
        configValues.put("tck.config.test.customDbConfig.key1", "valueFromDb1");
        configValues.put("tck.config.test.customDbConfig.key2", "valueFromDb2");
        configValues.put("tck.config.test.customDbConfig.key3", "big:cheese");
    }

    @Override
    public int getOrdinal() {
        return 112;
    }

    @Override
    public Map<String, String> getProperties() {
        return readPropertiesFromDb();
    }

    @Override
    public String getValue(String key) {
        return readPropertyFromDb(key);
    }

    @Override
    public String getName() {
        return "customDbConfig";
    }

    private Map<String, String> readPropertiesFromDb() {
        return configValues;
    }

    private String readPropertyFromDb(String key) {
        return configValues.get(key);
    }


}
