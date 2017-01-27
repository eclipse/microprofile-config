/*
 * Copyright (c) 2009-2017 Mark Struberg and others
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
 */
package org.eclipse.microprofile.impl.config.configsource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.spi.ConfigSource;


/**
 * Register all property files with the given propertyFileName
 * as {@link ConfigSource}.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class PropertyFileConfigSourceProvider {
    private static final Logger LOG = Logger.getLogger(PropertyFileConfigSourceProvider.class.getName());

    private List<ConfigSource> configSources = new ArrayList<>();

    public PropertyFileConfigSourceProvider(String propertyFileName, boolean optional, ClassLoader forClassLoader) {
        try {
            Enumeration<URL> propertyFileUrls = resolvePropertyFiles(forClassLoader, propertyFileName);

            if (!optional && !propertyFileUrls.hasMoreElements()) {
                throw new IllegalStateException(propertyFileName + " wasn't found.");
            }

            while (propertyFileUrls.hasMoreElements()) {
                URL propertyFileUrl = propertyFileUrls.nextElement();
                LOG.log(Level.INFO,
                        "Custom config found by GeronimoConfig. Name: ''{0}'', URL: ''{1}''",
                        new Object[]{propertyFileName, propertyFileUrl});
                configSources.add(new PropertyFileConfigSource(propertyFileUrl));
            }
        }
        catch (IOException ioe) {
            throw new IllegalStateException("problem while loading GeronimoConfig property files", ioe);
        }

    }

    public Enumeration<URL> resolvePropertyFiles(ClassLoader cl, String propertyFileName) throws IOException {
        Enumeration<URL> propertyFileUrls = cl.getResources(propertyFileName);

        return propertyFileUrls;
    }


    public List<ConfigSource> getConfigSources() {
        return configSources;
    }
}
