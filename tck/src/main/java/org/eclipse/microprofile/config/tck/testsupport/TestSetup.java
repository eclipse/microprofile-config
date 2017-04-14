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
