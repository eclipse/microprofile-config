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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class CustomConfigSourceTest extends AbstractTest {

    private @Inject Config config;

    @Deployment
    public static JavaArchive deploy() {
        return allIn("customConfigSourceTest.jar").addClass(CustomConfigSourceTest.class);
    }


    @Test
    public void testConfigSourceProvider() {
        Assert.assertEquals(config.getString("tck.config.test.customDbConfig.key1").get(), "valueFromDb1");
    }
}
