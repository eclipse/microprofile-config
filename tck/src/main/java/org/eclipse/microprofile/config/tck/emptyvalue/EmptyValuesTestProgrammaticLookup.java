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
package org.eclipse.microprofile.config.tck.emptyvalue;

import java.util.Arrays;
import java.util.NoSuchElementException;

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
import org.testng.annotations.Test;

import javax.inject.Inject;

public class EmptyValuesTestProgrammaticLookup extends Arquillian {

    @Deployment
    public static Archive deployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "emptyValues.jar")
                .addClasses(EmptyValuesTest.class, EmptyValuesBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(new StringAsset(
                                            "empty.string=" + "\n" +
                                            "comma.string=," + "\n" +
                                            "backslash.comma.string=\\\\," + "\n" +
                                            "double.comma.string=,," + "\n" +
                                            "foo.bar.string=foo,bar"+ "\n" +
                                            "foo.comma.string=foo,"+ "\n" +
                                            "comma.bar.string=,bar" + "\n" +
                                            "space.string=\\u0020"
                                            ),
                                       "microprofile-config.properties");

        return ShrinkWrap.create(WebArchive.class)
                .addAsLibrary(jar)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private Config config;

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testMissingStringGetValueArray() {
        String[] values = config.getValue("missing.string", String[].class);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testMissingStringGetValue() {
        config.getValue("missing.string", String.class);
    }

    @Test
    public void testMissingStringGetOptionalValue() {
        assertConfigurationNotPresentForOptional("missing.string");
        assertConfigurationNotPresentForOptionalMultiple("missing.string");
    }
    @Test(expectedExceptions = NoSuchElementException.class)
    public void testEmptyStringGetValueArray() {
        String[] values = config.getValue("empty.string", String[].class);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testEmptyStringGetValue() {
        config.getValue("empty.string", String.class);
    }

    @Test
    public void testEmptyStringGetOptionalValue() {
        assertConfigurationNotPresentForOptional("empty.string");
        assertConfigurationNotPresentForOptionalMultiple("empty.string");
    }
    
    @Test(expectedExceptions = NoSuchElementException.class)
    public void testCommaStringGetValueArray() {
        String[] values = config.getValue("comma.string", String[].class);
    }

    @Test
    public void testCommaStringGetValue() {
        String value = config.getValue("comma.string", String.class);
        Assert.assertEquals(value, ",");
    }

    @Test
    public void testCommaStringGetOptionalValue() {
        Assert.assertEquals(config.getOptionalValue("comma.string", String.class).get(), ",");
        assertConfigurationNotPresentForOptionalMultiple("comma.string");
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testBackslashCommaStringGetValueArray() {
        String[] values = config.getValue("backslash.comma.string", String[].class);
    }

    @Test
    public void testBackslashCommaStringGetValue() {
        String value = config.getValue("backslash.comma.string", String.class);
        Assert.assertEquals(value, "\\,");
    }

    @Test
    public void testBackslashCommaStringGetOptionalValue() {
        Assert.assertEquals(config.getOptionalValue("backslash.comma.string", String.class).get(), "\\,");
        assertConfigurationNotPresentForOptionalMultiple("backslash.comma.string");
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testDoubleCommaStringGetValueArray() {
        String[] values = config.getValue("double.comma.string", String[].class);
    }

    @Test
    public void testDoubleCommaStringGetValue() {
        String value = config.getValue("double.comma.string", String.class);
        Assert.assertEquals(value, ",,");
    }

    @Test
    public void testDoubleCommaStringGetOptionalValues() {
        Assert.assertEquals(config.getOptionalValue("double.comma.string", String.class).get(), ",,");
        assertConfigurationNotPresentForOptionalMultiple("double.comma.string");
    }

    @Test
    public void testFooBarStringGetValueArray() {
        String[] values = config.getValue("foo.bar.string", String[].class);
        Assert.assertEquals(values, new String[] {"foo", "bar"});

    }

    @Test
    public void testFooBarStringGetValue() {
        String value = config.getValue("foo.bar.string", String.class);
        Assert.assertEquals(value, "foo,bar");
    }

    @Test
    public void testFooBarStringGetOptionalValues() {
        Assert.assertEquals(config.getOptionalValue("foo.bar.string", String.class).get(), "foo,bar");
        Assert.assertEquals(config.getOptionalValue("foo.bar.string", String[].class).get(), new String[] {"foo", "bar"});
        Assert.assertEquals(config.getOptionalValues("foo.bar.string", String.class).get(), Arrays.asList("foo", "bar"));
        
    }
    @Test
    public void testFooCommaStringGetValueArray() {
        String[] values = config.getValue("foo.comma.string", String[].class);
        Assert.assertEquals(values, new String[] {"foo"});
    }

    @Test
    public void testFooCommaStringGetValue() {
        String value = config.getValue("foo.comma.string", String.class);
        Assert.assertEquals(value, "foo,");
    }

    @Test
    public void testFooCommaStringGetOptionalValues() {
        Assert.assertEquals(config.getOptionalValue("foo.comma.string", String.class).get(), "foo,");
        Assert.assertEquals(config.getOptionalValue("foo.comma.string", String[].class).get(), new String[] {"foo"});
        Assert.assertEquals(config.getOptionalValues("foo.comma.string", String.class).get(), Arrays.asList("foo"));
    }

    @Test
    public void testCommaBarStringGetValueArray() {
        String[] values = config.getValue("comma.bar.string", String[].class);
        Assert.assertEquals(values, new String[] {"bar"});
    }

    @Test
    public void testCommaBarStringGetValue() {
        String value = config.getValue("comma.bar.string", String.class);
        Assert.assertEquals(value, ",bar");
    }

    @Test
    public void testCommaBarStringGetOptionalValues() {
        Assert.assertEquals(config.getOptionalValue("comma.bar.string", String.class).get(), ",bar");
        Assert.assertEquals(config.getOptionalValue("comma.bar.string", String[].class).get(), new String[] {"bar"});
        Assert.assertEquals(config.getOptionalValues("comma.bar.string", String.class).get(), Arrays.asList("bar"));    
    }

    @Test
    public void testSpaceStringGetValueArray() {
        String[] values = config.getValue("space.string", String[].class);
        Assert.assertEquals(values, new String[] {" "});
    }

    @Test
    public void testSpaceStringGetValue() {
        String value = config.getValue("space.string", String.class);
        Assert.assertEquals(value, " ");
    }

    @Test
    public void testSpaceStringGetOptionalValue() {
        Assert.assertEquals(config.getOptionalValue("space.string", String.class).get(), " ");
        Assert.assertEquals(config.getOptionalValue("space.string", String[].class).get(), new String[] {" "});
        Assert.assertEquals(config.getOptionalValues("space.string", String.class).get(), Arrays.asList(" "));
    }

    private void assertConfigurationNotPresentForOptionalMultiple(String property) {
        Assert.assertFalse(config.getOptionalValue(property, String[].class).isPresent());
        Assert.assertFalse(config.getOptionalValues(property, String.class).isPresent());
    }

    private void assertConfigurationNotPresentForOptional(String property) {
        Assert.assertFalse(config.getOptionalValue(property, String.class).isPresent());
    }
}
