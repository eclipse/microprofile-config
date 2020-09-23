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
package org.eclipse.microprofile.config.tck;

import static org.testng.Assert.assertEquals;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

public class WarPropertiesLocationTest extends Arquillian {
    private static final String SOME_KEY = "org.eclipse.microprofile.config.test.internal.somekey";

    @Deployment
    public static WebArchive deploy() {
        StringAsset mpConfig = new StringAsset(SOME_KEY + "=someval");
        return ShrinkWrap
                .create(WebArchive.class, "configProviderTest.war")
                .addClasses(WarPropertiesLocationTest.class)
                .addAsWebInfResource(mpConfig, "classes/META-INF/microprofile-config.properties");
    }

    @Test
    public void testReadPropertyInWar() {
        String value = ConfigProvider.getConfig().getValue(SOME_KEY, String.class);
        assertEquals(value, "someval");
    }
}
