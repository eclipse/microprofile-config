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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.eclipse.microprofile.config.tck.configsources.CustomConfigSourceProvider;
import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.eclipse.microprofile.config.tck.converters.Duck;
import org.eclipse.microprofile.config.tck.converters.DuckConverter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public class ConverterTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "converterTest.jar")
                .addClass(ConverterTest.class)
                .addPackage(CustomDbConfigSource.class.getPackage())
                .addClasses(DuckConverter.class, Duck.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(ConfigSource.class, CustomDbConfigSource.class)
                .addAsServiceProvider(ConfigSourceProvider.class, CustomConfigSourceProvider.class)
                .addAsServiceProvider(Converter.class, DuckConverter.class)
                .as(JavaArchive.class);

        AbstractTest.addFile(testJar, "META-INF/microprofile-config.properties");
        AbstractTest.addFile(testJar, "sampleconfig.yaml");

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "converterTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.duckname")
    private Duck namedDuck;


    @Test
    public void testInteger() {
        Integer value = config.getValue("tck.config.test.javaconfig.converter.integervalue", Integer.class);
        Assert.assertEquals(value, Integer.valueOf(1234));
    }

    @Test
    public void testInt() {
        int value = config.getValue("tck.config.test.javaconfig.converter.integervalue", int.class);
        Assert.assertEquals(value, 1234);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInteger_Broken() {
        Integer value = config.getValue("tck.config.test.javaconfig.converter.integervalue.broken", Integer.class);
    }

    @Test
    public void testLong() {
        Long value = config.getValue("tck.config.test.javaconfig.converter.longvalue", Long.class);
        Assert.assertEquals(value, Long.valueOf(1234567890));
    }

    @Test
    public void testlong() {
        long primitiveValue = config.getValue("tck.config.test.javaconfig.converter.longvalue", long.class);
        Assert.assertEquals(primitiveValue, 1234567890L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLong_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.longvalue.broken", Long.class);
    }

    @Test
    public void testFloat() {
        Float value = config.getValue("tck.config.test.javaconfig.converter.floatvalue", Float.class);
        Assert.assertEquals(value, 12.34f);
    }

    @Test
    public void testfloat() {
        float value = config.getValue("tck.config.test.javaconfig.converter.floatvalue", float.class);
        Assert.assertEquals(value, 12.34f);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFloat_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.floatvalue.broken", Float.class);
    }

    @Test
    public void testDouble() {
        Double value = config.getValue("tck.config.test.javaconfig.converter.doublevalue", Double.class);
        Assert.assertEquals(value, 12.34d);
    }

    @Test
    public void testdouble() {
        double value = config.getValue("tck.config.test.javaconfig.converter.doublevalue", double.class);
        Assert.assertEquals(value,12.34d);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDouble_Broken() {
        Double value = config.getValue("tck.config.test.javaconfig.converter.doublevalue.broken", Double.class);
    }

    @Test
    public void testDuration() {
        Duration value = config.getValue("tck.config.test.javaconfig.converter.durationvalue", Duration.class);
        Assert.assertEquals(value, Duration.parse("PT15M"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuration_Broken() {
        Duration value = config.getValue("tck.config.test.javaconfig.converter.durationvalue.broken", Duration.class);
    }

    @Test
    public void testLocalTime() {
        LocalTime value = config.getValue("tck.config.test.javaconfig.converter.localtimevalue", LocalTime.class);
        Assert.assertEquals(value, LocalTime.parse("10:37"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLocalTime_Broken() {
        LocalTime value = config.getValue("tck.config.test.javaconfig.converter.localtimevalue.broken", LocalTime.class);
    }

    @Test
    public void testLocalDate() {
        LocalDate value = config.getValue("tck.config.test.javaconfig.converter.localdatevalue", LocalDate.class);
        Assert.assertEquals(value, LocalDate.parse("2017-12-24"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLocalDate_Broken() {
        LocalDate value = config.getValue("tck.config.test.javaconfig.converter.localdatevalue.broken", LocalDate.class);
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime value = config.getValue("tck.config.test.javaconfig.converter.localdatetimevalue", LocalDateTime.class);
        Assert.assertEquals(value, LocalDateTime.parse("2017-12-24T10:25:30"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLocalDateTime_Broken() {
        LocalDateTime value = config.getValue("tck.config.test.javaconfig.converter.localdatetimevalue.broken", LocalDateTime.class);
    }

    @Test
    public void testOffsetDateTime() {
        OffsetDateTime value = config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalue", OffsetDateTime.class);
        Assert.assertEquals(value, OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOffsetDateTime_Broken() {
        OffsetDateTime value = config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalue.broken", OffsetDateTime.class);
    }
    
    @Test
    public void testOffsetTime() {
        OffsetTime value = config.getValue("tck.config.test.javaconfig.converter.offsettimevalue", OffsetTime.class);
        OffsetTime parsed = OffsetTime.parse("13:45:30.123456789+02:00");
        Assert.assertEquals(value, parsed);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOffsetTime_Broken() {
        OffsetTime value = config.getValue("tck.config.test.javaconfig.converter.offsettimevalue.broken", OffsetTime.class);
    }
    
    @Test
    public void testInstant() {
        Instant value = config.getValue("tck.config.test.javaconfig.converter.instantvalue", Instant.class);
        Assert.assertEquals(value, Instant.parse("2015-06-02T21:34:33.616Z"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInstant_Broken() {
        Instant value = config.getValue("tck.config.test.javaconfig.converter.instantvalue.broken", Instant.class);
    }

    @Test
    public void testBoolean() {
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.true", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.true", boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.true_uppercase", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.true_mixedcase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.false", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.one", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.zero", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.seventeen", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.yes", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.yes_uppercase", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.yes_mixedcase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.no", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.y", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.y_uppercase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.n", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.on", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.on_uppercase", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.on_mixedcase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.off", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.off", boolean.class));
    }

    @Test
    public void testCustomConverter() {
        Assert.assertEquals(namedDuck.getName(), "Hannelore");
    }
}
