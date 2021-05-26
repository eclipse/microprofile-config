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
 */

package org.eclipse.microprofile.config.tck;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.eclipse.microprofile.config.Config;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * Test cases for the statement in configsources.asciidoc#default_configsources.env.mapping
 *
 * Prerequisite: The following environment variables must be set prior to running this test: "my_int_property" with the
 * value of "45" "MY_BOOLEAN_PROPERTY" with the value of "true" "my_string_property" with the value of "haha"
 * "MY_STRING_PROPERTY" with the value of "woohoo"
 *
 * @author Emily Jiang
 */
public class CDIPropertyNameMatchingTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static Archive deployment() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "CDIPropertyNameMatchingTest.jar")
                .addClasses(CDIPropertyNameMatchingTest.class, SimpleValuesBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "CDIPropertyNameMatchingTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @BeforeClass
    public void checkSetup() {
        // check whether the environment variables were populated by the executor correctly

        if (!"45".equals(System.getenv("my_int_property"))) {
            Assert.fail(
                    "Before running this test, the environment variable \"my_int_property\" must be set with the value of 45");
        }
        if (!"true".equals(System.getenv("MY_BOOLEAN_PROPERTY"))) {
            Assert.fail(
                    "Before running this test, the environment variable \"MY_BOOLEAN_Property\" must be set with the value of true");
        }

        // Environment variables are case insensitive on Windows platforms
        if (System.getProperty("os.name").contains("Windows")) {
            String myStringProp = System.getenv("MY_STRING_PROPERTY");
            if (!"woohoo".equals(myStringProp) && !"haha".equals(myStringProp)) {
                Assert.fail("Before running this test on a Windows platform, " +
                        "the environment variable \"MY_STRING_PROPERTY\" must be set with the value of woohoo or haha");
            }
        } else { // Not operating on Windows platform
            if (!"haha".equals(System.getenv("my_string_property"))) {
                Assert.fail("Before running this test on a non-Windows platform, " +
                        "the environment variable \"my_string_property\" must be set with the value of haha");
            }
            if (!"woohoo".equals(System.getenv("MY_STRING_PROPERTY"))) {
                Assert.fail("Before running this test on a non-Windows platform, " +
                        "the environment variable \"MY_STRING_PROPERTY\" must be set with the value of woohoo");
            }
        }

    }

    @Test
    public void testPropertyFromEnvironmentVariables() {
        SimpleValuesBean bean = getBeanOfType(SimpleValuesBean.class);

        // Environment variables are case insensitive on Windows platforms, use Config to
        // retrieve the "os.name" System property.
        if (config.getValue("os.name", String.class).contains("Windows")) {
            assertThat(bean.getStringProperty(), anyOf(equalTo("haha"), equalTo("woohoo")));
        } else { // non-Windows
            assertThat(bean.getStringProperty(), is(equalTo("haha")));
        }

        assertThat(bean.getBooleanProperty(), is(true));
        assertThat(bean.getIntProperty(), is(equalTo(45)));
    }

    private <T> T getBeanOfType(Class<T> beanClass) {
        return CDI.current().select(beanClass).get();
    }
}
