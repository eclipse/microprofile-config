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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.dynamic.DynamicChangeConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class DynamicConfigSourceTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "dynamicValuesTest.jar")
                .addClass(DynamicConfigSourceTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(ConfigSource.class, DynamicChangeConfigSource.class)
                .as(JavaArchive.class);


        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "dynamicValuesTest.war")
                .addAsLibrary(testJar);
        return war;
    }


    @Test
    public void testBgCount() throws Exception {
        assertTrue(DynamicChangeConfigSource.callbackListenerSet);
        Integer value = config.getValue(DynamicChangeConfigSource.TEST_ATTRIBUTE, Integer.class);
        Thread.sleep(20L);
        Integer value2 = config.getValue(DynamicChangeConfigSource.TEST_ATTRIBUTE, Integer.class);
        assertTrue(value2 > value);
    }

    @Test
    public void testBgCallback() throws Exception {
        assertTrue(DynamicChangeConfigSource.callbackListenerSet);
        Integer value = config.getValue(DynamicChangeConfigSource.TEST_ATTRIBUTE, Integer.class);
        Map<String, Integer> vals = new ConcurrentHashMap<>();


        config.registerConfigChangedListener(s -> s.forEach(k -> vals.put(k, config.getValue(k, Integer.class))));
        vals.clear();

        Thread.sleep(12L);
        assertEquals(1, vals.size());

        int i1 = vals.get(DynamicChangeConfigSource.TEST_ATTRIBUTE);

        Thread.sleep(15L);
        assertEquals(1, vals.size());

        int i2 = vals.get(DynamicChangeConfigSource.TEST_ATTRIBUTE);
        assertTrue(i2 > i1);
    }

}
