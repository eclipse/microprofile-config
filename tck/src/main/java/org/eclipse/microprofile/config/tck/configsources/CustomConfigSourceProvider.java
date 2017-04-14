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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class CustomConfigSourceProvider implements ConfigSourceProvider {

    @Override
    public Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        List<ConfigSource> detectedConfigSources = new ArrayList<>();

        Enumeration<URL> yamlFiles = null;
        try {
            yamlFiles = forClassLoader.getResources("sampleconfig.yaml");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (yamlFiles.hasMoreElements()) {
            detectedConfigSources.add(new SampleYamlConfigSource(yamlFiles.nextElement()));
        }
        return detectedConfigSources;
    }
}
