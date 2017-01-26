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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.spi.ConfigSource;


/**
 * Base class for all our ConfigSources
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 */
public abstract class BaseConfigSource implements ConfigSource {
    protected Logger log = Logger.getLogger(getClass().getName());

    private int ordinal = 1000; // default

    @Override
    public int getOrdinal() {
        return ordinal;
    }

    /**
     * Init method e.g. for initializing the ordinal.
     * This method can be used from a subclass to determine
     * the ordinal value
     *
     * @param defaultOrdinal the default value for the ordinal if not set via configuration
     */
    protected void initOrdinal(int defaultOrdinal) {
        ordinal = defaultOrdinal;

        String configuredOrdinalString = getValue(ConfigSource.CONFIG_ORDINAL);

        try {
            if (configuredOrdinalString != null) {
                ordinal = Integer.parseInt(configuredOrdinalString.trim());
            }
        }
        catch (NumberFormatException e) {
            log.log(Level.WARNING,
                    "The configured config-ordinal isn't a valid integer. Invalid value: " + configuredOrdinalString);
        }
    }

}
