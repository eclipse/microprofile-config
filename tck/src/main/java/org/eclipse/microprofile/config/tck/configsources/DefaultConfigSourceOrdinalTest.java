/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck.configsources;

import org.eclipse.microprofile.config.Config;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import jakarta.inject.Inject;

/**
 *
 * @author <a href="mailto:emijiang6@googlemail.com">Emily Jiang</a>
 */
public class DefaultConfigSourceOrdinalTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static Archive deployment() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "DefaultConfigSourceOrdinalTest.jar")
                .addClasses(DefaultConfigSourceOrdinalTest.class)
                .addAsManifestResource(
                        new StringAsset(
                                "config_ordinal=200\n" +
                                        "customer_name=Bill\n" +
                                        "customer.hobby=Badminton"),
                        "microprofile-config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "DefaultConfigSourceOrdinalTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @BeforeClass
    public void checkSetup() {
        // check whether the environment variables were populated by the executor correctly

        if (!"45".equals(System.getenv("config_ordinal"))) {
            Assert.fail(
                    "Before running this test, the environment variable \"config_ordinal\" must be set with the value of 45");
        }
        if (!"Bob".equals(System.getenv("customer_name"))) {
            Assert.fail(
                    "Before running this test, the environment variable \"customer_name\" must be set with the value of Bob");
        }
        if (!"120".equals(System.getProperty("config_ordinal"))) {
            Assert.fail(
                    "Before running this test, the system property \"config_ordinal\" must be set with the value of 120");
        }
        if (!"Tennis".equals(System.getProperty("customer.hobby"))) {
            Assert.fail(
                    "Before running this test, the system property \"customer.hobby\" must be set with the value of Tennis");
        }
    }

    @Test
    public void testOrdinalForEnv() {
        Assert.assertEquals("Bill", config.getValue("customer_name", String.class));
        Assert.assertEquals(200, config.getConfigValue("customer_name").getSourceOrdinal());
    }

    @Test
    public void testOrdinalForSystemProps() {
        Assert.assertEquals("Badminton", config.getValue("customer.hobby", String.class));
        Assert.assertEquals(200, config.getConfigValue("customer.hobby").getSourceOrdinal());
    }

}
