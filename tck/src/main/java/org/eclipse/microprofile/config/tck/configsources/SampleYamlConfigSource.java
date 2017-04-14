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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.microprofile.config.spi.ConfigSource;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class SampleYamlConfigSource implements ConfigSource {
    private Map<String, String> config = new HashMap<>();

    public SampleYamlConfigSource(URL url) {
        config.put("tck.config.test.sampleyaml.key1", "yamlvalue1");
    }

    @Override
    public int getOrdinal() {
        return 110;
    }

    @Override
    public Map<String, String> getProperties() {
        return config;
    }

    @Override
    public String getValue(String key) {
        return config.get(key);
    }

    @Override
    public String getName() {
        return null;
    }

}
