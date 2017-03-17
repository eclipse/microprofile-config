/*
 * Copyright (c) 2016-2017 Payara Services Ltd. and others
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
 *
 */

package org.eclipse.microprofile.config.tck.testsupport;

import java.time.ZonedDateTime;
import java.util.Date;
import javax.enterprise.inject.spi.CDI;

/**
 * Utility class for common functionality used in test scenarios.
 * @author Ondrej Mihalyi
 */
public final class TestSetup {
    
    private TestSetup() {
        // utility class
    }

    public static Date toDate(String isoDateTime) {
        return Date.from(ZonedDateTime.parse(isoDateTime).toInstant());
    }

    public static void ensurePropertyDefined(String key, String value) {
        // setting configuration via system properties
        System.setProperty(key, value);
    }

    public static void ensurePropertyUndefined(String key) {
        // clearing configuration in system properties if previously set
        System.getProperties().remove(key);
    }

    public static <T> T getBeanOfType(Class<T> beanClass) {
        return CDI.current().select(beanClass).get();
    }

}
