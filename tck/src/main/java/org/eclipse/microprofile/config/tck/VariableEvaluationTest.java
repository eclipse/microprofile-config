/*
 * Copyright (c) 2016-2019 Contributors to the Eclipse Foundation
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.fail;

import java.util.NoSuchElementException;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.eclipse.microprofile.config.tck.configsources.KeyValuesConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

public class VariableEvaluationTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "VariableEvaluationTest.jar")
            .addPackage(AbstractTest.class.getPackage())
            .addClass(KeyValuesConfigSource.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "VariableEvaluationTest.war")
            .addAsLibrary(testJar);
        return war;
    }

    @Test
    public void testMPConfigEvaluateVariablesProperty() {
        final String previousValue = System.getProperty("mp.config.evaluateVariables");

        try {
            System.setProperty("mp.config.evaluateVariables", "false");

            Config config = buildConfig(
                "foo.one", "value",
                "foo.two", "${foo.one}",
                "foo.three", "+${foo.two}+");
            assertEquals(config.getValue("foo.two", String.class), "${foo.one}");
            assertEquals(config.getValue("foo.three", String.class), "+${foo.two}+");
        }
        finally {
            if (previousValue != null) {
                System.setProperty("mp.config.evaluateVariables", previousValue);
            }
            else {
                System.clearProperty("mp.config.evaluateVariables");
            }
        }
    }

    @Test
    public void testDefaultVariableEvaluation() {
        Config config = buildConfig(
            "foo.one", "value",
            "foo.two", "${foo.one}",
            "foo.three", "+${foo.two}+");

        assertEquals(config.getValue("foo.one", String.class), "value");

        assertEquals(config.getValue("foo.two", String.class), "value");
        assertEquals(config.access("foo.two", String.class).build().getValue(), "value");
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one}");

        assertEquals(config.getValue("foo.three", String.class), "+value+");
        assertEquals(config.access("foo.three", String.class).build().getValue(), "+value+");
        assertEquals(config.access("foo.three", String.class).evaluateVariables(false).build().getValue(), "+${foo.two}+");

    }

    @Test
    public void testVariableEvaluationWithDefaults() {
        Config config = buildConfig(
            "foo.two", "${foo.one:value}",
            "foo.three", "+${foo.two}+");

        assertFalse(config.getOptionalValue("foo.one", String.class).isPresent());

        assertEquals(config.getValue("foo.two", String.class), "value");
        assertEquals(config.access("foo.two", String.class).build().getValue(), "value");
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one:value}");

        assertEquals(config.getValue("foo.three", String.class), "+value+");
        assertEquals(config.access("foo.three", String.class).build().getValue(), "+value+");
        assertEquals(config.access("foo.three", String.class).evaluateVariables(false).build().getValue(), "+${foo.two}+");
    }

    @Test
    public void testVariableEvaluationWithMissingProperty() {
        Config config = buildConfig(
            "foo.two", "${foo.one}suffix",
            "foo.three", "+${foo.two}+");

        assertFalse(config.getOptionalValue("foo.two", String.class).isPresent());
        try {
            config.getValue("foo.two", String.class);
            fail("Expected exception");
        }
        catch (NoSuchElementException expected) {
            // OK
        }
        try {
            config.access("foo.two", String.class).build().getValue();
            fail("Expected exception");
        }
        catch (NoSuchElementException expected) {
            // OK
        }
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one}suffix");


        assertFalse(config.getOptionalValue("foo.three", String.class).isPresent());
        try {
            config.getValue("foo.three", String.class);
            fail("Expected exception");
        }
        catch (NoSuchElementException expected) {
            // OK
        }
        try {
            config.access("foo.tree", String.class).build().getValue();
            fail("Expected exception");
        }
        catch (NoSuchElementException expected) {
            // OK
        }
        assertEquals(config.access("foo.three", String.class).evaluateVariables(false).build().getValue(), "+${foo.two}+");

    }

    @Test
    public void testVariableEvaluationWithMissingOptionalProperty() {
        Config config = buildConfig(
            "foo.two", "${foo.one:}suffix",
            "foo.three", "+${foo.two}+");

        assertFalse(config.getOptionalValue("foo.one", String.class).isPresent());

        assertEquals(config.getValue("foo.two", String.class), "suffix");
        assertEquals(config.access("foo.two", String.class).build().getValue(), "suffix");
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one:}suffix");

        assertEquals(config.getValue("foo.three", String.class), "+suffix+");
        assertEquals(config.access("foo.three", String.class).build().getValue(), "+suffix+");
        assertEquals(config.access("foo.three", String.class).evaluateVariables(false).build().getValue(), "+${foo.two}+");
    }

    @Test
    public void testVariableEvaluationWithOptionalProperty() {
        Config config = buildConfig(
            "foo.one", "value",
            "foo.two", "${foo.one:}_suffix",
            "foo.three", "+value_suffix+");

        assertEquals(config.getValue("foo.two", String.class), "value_suffix");
        assertEquals(config.access("foo.two", String.class).build().getValue(), "value_suffix");
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one:}_suffix");

        assertEquals(config.getValue("foo.three", String.class), "+value_suffix+");
        assertEquals(config.access("foo.three", String.class).build().getValue(), "+value_suffix+");
        assertEquals(config.access("foo.three", String.class).evaluateVariables(false).build().getValue(), "+value_suffix+");
    }

    @Test
    public void testVariableEvaluationWithOptionalPropertyAndDefault() {
        Config config = buildConfig(
            "foo.two", "${foo.one:value}_suffix",
            "foo.three", "+${foo.two}+");

        assertFalse(config.getOptionalValue("foo.one", String.class).isPresent());

        assertEquals(config.getValue("foo.two", String.class), "value_suffix");
        assertEquals(config.access("foo.two", String.class).build().getValue(), "value_suffix");
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one:value}_suffix");

        assertEquals(config.getValue("foo.three", String.class), "+value_suffix+");
        assertEquals(config.access("foo.three", String.class).build().getValue(), "+value_suffix+");
        assertEquals(config.access("foo.three", String.class).evaluateVariables(false).build().getValue(), "+${foo.two}+");
    }

    @Test
    public void testRecursiveVariableEvaluation() {
        Config config = buildConfig(
            "foo.one", "${foo.two}",
            "foo.two", "${foo.one}");

        try {
            config.getOptionalValue("foo.one", String.class);
            fail("Expected exception");
        }
        catch (IllegalArgumentException expected) {
                // OK
        }

        try {
            config.getValue("foo.one", String.class);
            fail("Expected exception");
        }
        catch (IllegalArgumentException expected) {
            // OK
        }
        try {
            config.access("foo.one", String.class).build().getValue();
            fail("Expected exception");
        }
        catch (IllegalArgumentException expected) {
            // OK
        }
        assertEquals(config.access("foo.one", String.class).evaluateVariables(false).build().getValue(), "${foo.two}");


        try {
            config.getOptionalValue("foo.two", String.class);
            fail("Expected exception");
        }
        catch (IllegalArgumentException expected) {
            // OK
        }
        try {
            config.getValue("foo.two", String.class);
            fail("Expected exception");
        }
        catch (IllegalArgumentException expected) {
            // OK
        }
        try {
            config.access("foo.two", String.class).build().getValue();
            fail("Expected exception");
        }
        catch (IllegalArgumentException expected) {
            // OK
        }
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.one}");
    }

    @Test
    public void testDefaultValueIsNotEvaluated() {
        Config config = buildConfig(
            "foo.one", "value",
            "foo.two", "${foo.three:${foo.one}}");

        assertEquals(config.getValue("foo.two", String.class), "${foo.one}");
        assertEquals(config.access("foo.two", String.class).build().getValue(), "${foo.one}");
        assertEquals(config.access("foo.two", String.class).evaluateVariables(false).build().getValue(), "${foo.three:${foo.one}}");

    }

    @Test
    public void testConfigBuilderDisableVariableEvaluation() {
        Config config = ConfigProviderResolver.instance().getBuilder()
            .evaluateVariables(false)
            .withSources(KeyValuesConfigSource.config(
                "foo.one", "value",
                "foo.two", "${foo.one}"
            ))
            .build();

        assertEquals(config.getValue("foo.two", String.class), "${foo.one}");
    }

    private static Config buildConfig(String... keyValues) {
        return ConfigProviderResolver.instance().getBuilder()
            .addDefaultSources()
            .withSources(KeyValuesConfigSource.config(keyValues))
            .build();
    }
}
