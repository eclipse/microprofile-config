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

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.eclipse.microprofile.config.tck.converters.custom.BooleanConverter;
import org.eclipse.microprofile.config.tck.converters.custom.CharacterConverter;
import org.eclipse.microprofile.config.tck.converters.custom.DoubleConverter;
import org.eclipse.microprofile.config.tck.converters.custom.IntegerConverter;
import org.eclipse.microprofile.config.tck.converters.custom.LongConverter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class CustomConverterTest extends Arquillian {
    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "customConverterTest.jar")
            .addClass(CustomConverterTest.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsServiceProviderAndClasses(Converter.class,
                                  IntegerConverter.class,
                                  LongConverter.class,
                                  DoubleConverter.class,
                                  BooleanConverter.class,
                                  CharacterConverter.class)
            .as(JavaArchive.class);

        AbstractTest.addFile(testJar, "META-INF/microprofile-config.properties");

        return ShrinkWrap
            .create(WebArchive.class, "customConverterTest.war")
            .addAsLibrary(testJar);
    }

    @Test
    public void testInteger() {
        Integer value = config.getValue("tck.config.test.javaconfig.custom.converter.integervalue", Integer.class);
        Assert.assertEquals(value, Integer.valueOf(999));
    }

    @Test
    public void testIntPrimitive() {
        int value = config.getValue("tck.config.test.javaconfig.custom.converter.integervalue", int.class);
        Assert.assertEquals(value, 999);
    }

    @Test
    public void testLong() {
        Long value = config.getValue("tck.config.test.javaconfig.custom.converter.longvalue", Long.class);
        Assert.assertEquals(value, Long.valueOf(999));
    }

    @Test
    public void testLongPrimitive() {
        long value = config.getValue("tck.config.test.javaconfig.custom.converter.longvalue", long.class);
        Assert.assertEquals(value, 999);
    }

    @Test
    public void testDouble() {
        Double value = config.getValue("tck.config.test.javaconfig.custom.converter.doublevalue", Double.class);
        Assert.assertEquals(value, 999.9);
    }

    @Test
    public void testDoublePrimitive() {
        double value = config.getValue("tck.config.test.javaconfig.custom.converter.doublevalue", double.class);
        Assert.assertEquals(value, 999.9);
    }

    @Test
    public void testBoolean() {
        Boolean value = config.getValue("tck.config.test.javaconfig.custom.converter.booleanvalue", Boolean.class);
        Assert.assertEquals(value, Boolean.TRUE);
    }

    @Test
    public void testBooleanPrimitive() {
        boolean value = config.getValue("tck.config.test.javaconfig.custom.converter.booleanvalue", boolean.class);
        Assert.assertTrue(value);
    }

    @Test
    public void testCharacter() {
        Character value = config.getValue("tck.config.test.javaconfig.custom.converter.charvalue", Character.class);
        Assert.assertEquals(value, Character.valueOf('r'));
    }

    @Test
    public void testCharPrimitive() {
        char value = config.getValue("tck.config.test.javaconfig.custom.converter.charvalue", char.class);
        Assert.assertEquals(value, 'r');
    }

    @Test
    public void testGetIntegerConverter() {
        Integer value = config.getConverter(Integer.class).get().convert("1");
        Assert.assertEquals(value, Integer.valueOf(999));
    }

    @Test
    public void testGetIntPrimitiveConverter() {
        int value = config.getConverter(int.class).get().convert("1");
        Assert.assertEquals(value, 999);
    }

    @Test
    public void testGetLongConverter() {
        Long value = config.getConverter(Long.class).get().convert("1");
        Assert.assertEquals(value, Long.valueOf(999));
    }

    @Test
    public void testGetLongPrimitiveConverter() {
        long value = config.getConverter(long.class).get().convert("1");
        Assert.assertEquals(value, 999);
    }

    @Test
    public void testGetDoubleConverter() {
        Double value = config.getConverter(Double.class).get().convert("1.0");
        Assert.assertEquals(value, 999.9);
    }

    @Test
    public void testGetDoublePrimitiveConverter() {
        double value = config.getConverter(double.class).get().convert("1.0");
        Assert.assertEquals(value, 999.9);
    }

    @Test
    public void testGetBooleanConverter() {
        Boolean value = config.getConverter(Boolean.class).get().convert("false");
        Assert.assertEquals(value, Boolean.TRUE);
    }

    @Test
    public void testGetBooleanPrimitiveConverter() {
        boolean value = config.getConverter(boolean.class).get().convert("false");
        Assert.assertTrue(value);
    }

    @Test
    public void testGetCharacterConverter() {
        Character value = config.getConverter(Character.class).get().convert("c");
        Assert.assertEquals(value, Character.valueOf('r'));
    }

    @Test
    public void testGetCharPrimitiveConverter() {
        char value = config.getConverter(char.class).get().convert("c");
        Assert.assertEquals(value, 'r');
    }
}
