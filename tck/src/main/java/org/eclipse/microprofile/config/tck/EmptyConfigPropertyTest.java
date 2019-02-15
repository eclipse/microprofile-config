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

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.tck.configsources.KeyValuesConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

public class EmptyConfigPropertyTest  extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "EmptyConfigPropertyTest.jar")
            .addClass(KeyValuesConfigSource.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .as(JavaArchive.class);

        addFile(testJar, "META-INF/microprofile-config.properties");

        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "EmptyConfigPropertyTest.war")
            .addAsLibrary(testJar);
        return war;
    }


    private @Inject
    Config config;

    static final String EMPTY_PROP = "tck.config.empty";
    static final String MISSING_PROP = "tck.config.this.property.is.not.defined.anywhere";

    @Test
    public void testEmptyPropertyWithConfig() {
        // | Property value | Output type | `Config` method | Output result
        // | `""` (empty string) | `String` | `getValue` | throws `NoSuchElementException`
        try {
            config.getValue(EMPTY_PROP, String.class);
            Assert.fail("An empty string is considered as missing and must raise a NoSuchElementException");
        }
        catch (NoSuchElementException e) {
        }
        try {
            config.getValue(EMPTY_PROP, Double.class);
            Assert.fail(("an empty String is considered missing regardless of the output type"));
        }
        catch (NoSuchElementException e) {
        }

        // | `""`        | `String[]` | `getValue` | throws `NoSuchElementException`
        try {
            config.getValue(EMPTY_PROP, String[].class);
            Assert.fail(("an empty String is considered missing regardless of the output type"));
        }
        catch (NoSuchElementException e) {
        }
        try {
            config.getValue(EMPTY_PROP, Double[].class);
            Assert.fail(("an empty String is considered missing regardless of the output type"));
        }
        catch (NoSuchElementException e) {
        }


        // | `""`        | `String` | `getOptionalValue` | `Optional.empty()`
        assertEquals(config.getOptionalValue(EMPTY_PROP, String.class), Optional.empty());
        assertEquals(config.getOptionalValue(EMPTY_PROP, Double.class), Optional.empty());

        // | `""` | `String[]` | `getOptionalValue` | `Optional.empty()`
        assertEquals( config.getOptionalValue(EMPTY_PROP, String[].class), Optional.empty());
        assertEquals( config.getOptionalValue(EMPTY_PROP, Double[].class), Optional.empty());
    }

    @Test
    public void testMissingProperty() {
        // | Property value | Output type | `Config` method | Output result
        // | missing     | `String` | `getValue` | throws `NoSuchElementException`
        try {
            config.getValue(MISSING_PROP, String.class);
            Assert.fail("A missing property must raise a NoSuchElementException");
        }
        catch (NoSuchElementException e) {
        }
        // | missing     | `String[]` | `getValue` | throws `NoSuchElementException`
        try {
            config.getValue(MISSING_PROP, String[].class);
            Assert.fail("A missing property must raise a NoSuchElementException");
        }
        catch (NoSuchElementException e) {
        }
        // regardless of the output type
        try {
            config.getValue(MISSING_PROP, Double[].class);
            Assert.fail("A missing property must raise a NoSuchElementException");
        }
        catch (NoSuchElementException e) {
        }

        // | missing     | `String` |`getOptionalValue` | `Optional.empty()`
        assertEquals( config.getOptionalValue(MISSING_PROP, String.class), Optional.empty());
        // | missing | `String[]` | `getOptionalValue` | `Optional.empty()`
        assertEquals( config.getOptionalValue(MISSING_PROP, String[].class), Optional.empty());
    }

    @Test
    public void testArrayCommasRules() {
        Config config = KeyValuesConfigSource.buildConfig(
            "arrayWithSpace", " ",
            "arrayWithSingleComma", ",",
            "arrayWithTwoCommas", ",,",
            "arrayWithSingleValue", "foo",
            "arrayWithTwoValues", "foo,bar",
            "arrayWithTrailingComma", "foo,",
            "arrayWithLeadingComma", ",bar",
            "arrayWithLeadingAndTrailingCommas", ",bar,");

        // | Property value | Output type | `Config` method | Output result
        // | `" "`       | `String[]` | `getValue` | `{ " " }`
        assertEquals(config.getValue("arrayWithSpace", String[].class), new String[] { " " });
        // | `","`       | `String[]` | `getValue` | throws `NoSuchElementException`
        try {
            config.getValue("arrayWithSingleComma", String[].class);
            Assert.fail("An array with only empty elements must raise a NoSuchElementException");
        }
        catch (NoSuchElementException e) {
        }
        // | `",,"`      | `String[]` | `getValue` | throws `NoSuchElementException`
        try {
            config.getValue("arrayWithTwoCommas", String[].class);
            Assert.fail("An array with only empty elements must raise a NoSuchElementException");
        }
        catch (NoSuchElementException e) {
        }
        // | `"foo"`     | `String[]` | `getValue` | `{ "foo" }`
        assertEquals(config.getValue("arrayWithSingleValue", String[].class), new String[] { "foo" });
        // | `"foo,bar"` | `String[]` | `getValue` | `{ "foo", "bar" }`
        assertEquals(config.getValue("arrayWithTwoValues", String[].class), new String[] { "foo", "bar" });
        // | `"foo,"`    | `String[]` | `getValue` | `{ "foo" }`
        assertEquals(config.getValue("arrayWithTrailingComma", String[].class), new String[] { "foo" });
        // | `",bar"`    | `String[]` | `getValue` | `{ "bar" }`
        assertEquals(config.getValue("arrayWithLeadingComma", String[].class), new String[] { "bar" });
        // | `",bar,"`   | `String[]` | `getValue` | `{ "bar" }`
        assertEquals(config.getValue("arrayWithLeadingAndTrailingCommas", String[].class), new String[] { "bar" });

        // | `","` | `String[]` | `getOptionalValue` | `Optional.empty()`
        assertEquals(config.getOptionalValue("arrayWithSingleComma", String[].class), Optional.empty());
        // | `",,"` | `String[]` | `getOptionalValue` | `Optional.empty()`
        assertEquals(config.getOptionalValue("arrayWithTwoCommas", String[].class), Optional.empty());
    }

    @Test
    public void testEmptyPropertyResetsLowerOrdinalProperty() {
        Config config = ConfigProviderResolver.instance().getBuilder()
            .withSources(
                // lower-ordinal configures a prop
                KeyValuesConfigSource.config(10,
                    "my.prop", "foo"),
                // higher-ordinal resets it with an empty string
                KeyValuesConfigSource.config(20,
                    "my.prop", ""))
            .build();

        try {
            config.getValue("my.prop", String.class);
            fail("The property must be reset by the empty string in the higher-ordinal config source");
        }
        catch (NoSuchElementException e) {
        }

        assertEquals(config.getOptionalValue("my.prop", String.class), Optional.empty());
    }

}
