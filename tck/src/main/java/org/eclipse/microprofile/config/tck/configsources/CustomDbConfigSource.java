/**********************************************************************
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation 
 *
 * See the NOTICES file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************************/
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
