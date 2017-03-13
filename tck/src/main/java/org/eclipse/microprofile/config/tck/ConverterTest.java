/*
 * Copyright (c) 2016-2017 Mark Struberg and others
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
package org.eclipse.microprofile.config.tck;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConverterTest extends AbstractTest {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        return allIn("converterTest").addClass(ConverterTest.class);
    }


    @Test
    public void testIntegerConverter() {
        Integer value = config.getValue("tck.config.test.javaconfig.converter.integervalue", Integer.class).get();
        Assert.assertEquals(value, Integer.valueOf(1234));

    }

    @Test
    public void testFloatConverter() {
        Float value = config.getValue("tck.config.test.javaconfig.converter.floatvalue", Float.class).get();
        Assert.assertEquals(value, Float.valueOf(12.34f));

    }


}
