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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.eclipse.microprofile.config.tck.configsources.CustomConfigSourceProvider;
import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.eclipse.microprofile.config.tck.converters.Donald;
import org.eclipse.microprofile.config.tck.converters.Duck;
import org.eclipse.microprofile.config.tck.converters.DuckConverter;
import org.eclipse.microprofile.config.tck.converters.UpperCaseDuckConverter;
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
 * @author <a href="mailto:john.d.ament@gmail.com">John D. Ament</a>
 * @author <a href="mailto:gunnar.morling@googlemail.com">Gunnar Morling</a>
 */
public class ConverterTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "converterTest.jar")
                .addClass(ConverterTest.class)
                .addPackage(CustomDbConfigSource.class.getPackage())
                .addClasses(DuckConverter.class, Duck.class, Donald.class, UpperCaseDuckConverter.class)
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

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDonaldNotConvertedByDefault() {
        config.getValue("tck.config.test.javaconfig.converter.donaldname", Donald.class);
    }

    @Test
    public void testNoDonaldConverterByDefault() {
        Assert.assertFalse(config.getConverter(Donald.class).isPresent());
    }

    @Test
    public void testDonaldConversionWithLambdaConverter() {
        Config newConfig = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverter(Donald.class, 100, (s) -> Donald.iLikeDonald(s))
                .build();
        Donald donald = newConfig.getValue("tck.config.test.javaconfig.converter.donaldname", Donald.class);
        Assert.assertNotNull(donald);
        Assert.assertEquals(donald.getName(), "Duck");
    }

    @Test
    public void testGetDonaldConverterWithLambdaConverter() {
        Config newConfig = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverter(Donald.class, 100, (s) -> Donald.iLikeDonald(s))
                .build();
        Donald donald = newConfig.getConverter(Donald.class).get().convert("Duck");
        Assert.assertNotNull(donald);
        Assert.assertEquals(donald.getName(), "Duck");
    }

    @Test
    public void testDonaldConversionWithMultipleLambdaConverters() {
        // defines 2 config with the lambda converters defined in different orders.
        // Order must not matter, the lambda with the upper case must always be used as it has the highest priority
        Config config1 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverter(Donald.class, 101, (s) -> Donald.iLikeDonald(s.toUpperCase()))
                .withConverter(Donald.class, 100, (s) -> Donald.iLikeDonald(s))
                .build();
        Config config2 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverter(Donald.class, 100, (s) -> Donald.iLikeDonald(s))
                .withConverter(Donald.class, 101, (s) -> Donald.iLikeDonald(s.toUpperCase()))
                .build();

        Donald donald = config1.getConverter(Donald.class).get().convert("Duck");
        Assert.assertNotNull(donald);
        Assert.assertEquals(donald.getName(), "DUCK",
                "The converter with the highest priority (using upper case) must be used.");
        donald = config2.getConverter(Donald.class).get().convert("Duck");
        Assert.assertNotNull(donald);
        Assert.assertEquals(donald.getName(), "DUCK",
                "The converter with the highest priority (using upper case) must be used.");
    }

    @Test
    public void testGetDonaldConverterWithMultipleLambdaConverters() {
        // defines 2 config with the lambda converters defined in different orders.
        // Order must not matter, the lambda with the upper case must always be used as it has the highest priority
        Config config1 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverter(Donald.class, 101, (s) -> Donald.iLikeDonald(s.toUpperCase()))
                .withConverter(Donald.class, 100, (s) -> Donald.iLikeDonald(s))
                .build();
        Config config2 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverter(Donald.class, 100, (s) -> Donald.iLikeDonald(s))
                .withConverter(Donald.class, 101, (s) -> Donald.iLikeDonald(s.toUpperCase()))
                .build();

        Donald donald = config1.getValue("tck.config.test.javaconfig.converter.donaldname", Donald.class);
        Assert.assertNotNull(donald);
        Assert.assertEquals(donald.getName(), "DUCK",
                "The converter with the highest priority (using upper case) must be used.");
        donald = config2.getValue("tck.config.test.javaconfig.converter.donaldname", Donald.class);
        Assert.assertNotNull(donald);
        Assert.assertEquals(donald.getName(), "DUCK",
                "The converter with the highest priority (using upper case) must be used.");
    }

    @Test
    public void testByte() {
        Byte value = config.getValue("tck.config.test.javaconfig.converter.bytevalue", Byte.class);
        Assert.assertEquals(value, Byte.valueOf((byte) 123));
    }

    @Test
    public void testbyte() {
        byte value = config.getValue("tck.config.test.javaconfig.converter.bytevalue", byte.class);
        Assert.assertEquals(value, (byte) 123);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testByte_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.bytevalue.broken", Byte.class);
    }

    @Test
    public void testGetByteConverter() {
        Byte value = config.getConverter(Byte.class).get().convert("123");
        Assert.assertEquals(value, Byte.valueOf((byte) 123));
    }

    @Test
    public void testGetbyteConverter() {
        byte value = config.getConverter(byte.class).get().convert("123");
        Assert.assertEquals(value, (byte) 123);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetByteConverter_Broken() {
        config.getConverter(Byte.class).get().convert("xxx");
    }

    @Test
    public void testShort() {
        Short value = config.getValue("tck.config.test.javaconfig.converter.shortvalue", Short.class);
        Assert.assertEquals(value, Short.valueOf((short) 1234));
    }

    @Test
    public void testshort() {
        short value = config.getValue("tck.config.test.javaconfig.converter.shortvalue", short.class);
        Assert.assertEquals(value, (short) 1234);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testShort_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.shortvalue.broken", Short.class);
    }

    @Test
    public void testGetShortConverter() {
        Short value = config.getConverter(Short.class).get().convert("1234");
        Assert.assertEquals(value, Short.valueOf((short) 1234));
    }

    @Test
    public void testGetshortConverter() {
        short value = config.getConverter(short.class).get().convert("1234");
        Assert.assertEquals(value, (short) 1234);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetShortConverter_Broken() {
        config.getConverter(Short.class).get().convert("xxx");
    }

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
        config.getValue("tck.config.test.javaconfig.converter.integervalue.broken", Integer.class);
    }

    @Test
    public void testGetIntegerConverter() {
        Integer value = config.getConverter(Integer.class).get().convert("1234");
        Assert.assertEquals(value, Integer.valueOf(1234));
    }

    @Test
    public void testGetIntConverter() {
        int value = config.getConverter(int.class).get().convert("1234");
        Assert.assertEquals(value, 1234);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetIntegerConverter_Broken() {
        config.getConverter(Integer.class).get().convert("xxx");
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
    public void testGetLongConverter() {
        Long value = config.getConverter(Long.class).get().convert("1234567890");
        Assert.assertEquals(value, Long.valueOf(1234567890));
    }

    @Test
    public void testGetlongConverter() {
        long primitiveValue = config.getConverter(long.class).get().convert("1234567890");
        Assert.assertEquals(primitiveValue, 1234567890L);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetLongConverter_Broken() {
        config.getConverter(Long.class).get().convert("alfasdf");
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
    public void testGetFloatConverter() {
        Float value = config.getConverter(Float.class).get().convert("12.34");
        Assert.assertEquals(value, 12.34f);
    }

    @Test
    public void testGetfloatConverter() {
        float value = config.getConverter(float.class).get().convert("12.34");
        Assert.assertEquals(value, 12.34f);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetFloatConverter_Broken() {
        config.getConverter(Float.class).get().convert("alfasdf");
    }

    @Test
    public void testDouble() {
        Double value = config.getValue("tck.config.test.javaconfig.converter.doublevalue", Double.class);
        Assert.assertEquals(value, 12.34d);
    }

    @Test
    public void testdouble() {
        double value = config.getValue("tck.config.test.javaconfig.converter.doublevalue", double.class);
        Assert.assertEquals(value, 12.34d);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDouble_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.doublevalue.broken", Double.class);
    }

    @Test
    public void testGetDoubleConverter() {
        Double value = config.getConverter(Double.class).get().convert("12.34");
        Assert.assertEquals(value, 12.34d);
    }

    @Test
    public void testGetdoubleConverter() {
        double value = config.getConverter(double.class).get().convert("12.34");
        Assert.assertEquals(value, 12.34d);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetDoubleConverter_Broken() {
        config.getConverter(Double.class).get().convert("alfasdf");
    }

    @Test
    public void testChar() {
        Character value = config.getValue("tck.config.test.javaconfig.converter.charvalue", Character.class);
        Assert.assertEquals(value, Character.valueOf('c'));
    }

    @Test
    public void testchar() {
        char value = config.getValue("tck.config.test.javaconfig.converter.charvalue", char.class);
        Assert.assertEquals(value, 'c');
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testChar_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.charvalue.broken", Character.class);
    }

    @Test
    public void testGetCharConverter() {
        Character value = config.getConverter(Character.class).get().convert("c");
        Assert.assertEquals(value, Character.valueOf('c'));
    }

    @Test
    public void testGetcharConverter() {
        char value = config.getConverter(char.class).get().convert("c");
        Assert.assertEquals(value, 'c');
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetCharConverter_Broken() {
        config.getConverter(Character.class).get().convert("xxx");
    }

    @Test
    public void testDuration() {
        Duration value = config.getValue("tck.config.test.javaconfig.converter.durationvalue", Duration.class);
        Assert.assertEquals(value, Duration.parse("PT15M"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDuration_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.durationvalue.broken", Duration.class);
    }

    @Test
    public void testGetDurationCoverter() {
        Duration value = config.getConverter(Duration.class).get().convert("PT15M");
        Assert.assertEquals(value, Duration.parse("PT15M"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetDurationConverter_Broken() {
        config.getConverter(Duration.class).get().convert("alfasdf");
    }

    @Test
    public void testLocalTime() {
        LocalTime value = config.getValue("tck.config.test.javaconfig.converter.localtimevalue", LocalTime.class);
        Assert.assertEquals(value, LocalTime.parse("10:37"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLocalTime_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.localtimevalue.broken", LocalTime.class);
    }

    @Test
    public void testGetLocalTimeConverter() {
        LocalTime value = config.getConverter(LocalTime.class).get().convert("10:37");
        Assert.assertEquals(value, LocalTime.parse("10:37"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetLocalTimeConverter_Broken() {
        config.getConverter(LocalTime.class).get().convert("alfasdf");
    }

    @Test
    public void testLocalDate() {
        LocalDate value = config.getValue("tck.config.test.javaconfig.converter.localdatevalue", LocalDate.class);
        Assert.assertEquals(value, LocalDate.parse("2017-12-24"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLocalDate_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.localdatevalue.broken", LocalDate.class);
    }

    @Test
    public void testGetLocalDateConverter() {
        LocalDate value = config.getConverter(LocalDate.class).get().convert("2017-12-24");
        Assert.assertEquals(value, LocalDate.parse("2017-12-24"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetLocalDateConverter_Broken() {
        config.getConverter(LocalDate.class).get().convert("alfasdf");
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime value =
                config.getValue("tck.config.test.javaconfig.converter.localdatetimevalue", LocalDateTime.class);
        Assert.assertEquals(value, LocalDateTime.parse("2017-12-24T10:25:30"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testLocalDateTime_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.localdatetimevalue.broken", LocalDateTime.class);
    }

    @Test
    public void testGetLocalDateTimeConverter() {
        LocalDateTime value = config.getConverter(LocalDateTime.class).get().convert("2017-12-24T10:25:30");
        Assert.assertEquals(value, LocalDateTime.parse("2017-12-24T10:25:30"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetLocalDateTimeConverter_Broken() {
        config.getConverter(LocalDateTime.class).get().convert("alfasdf");
    }

    @Test
    public void testOffsetDateTime() {
        OffsetDateTime value =
                config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalue", OffsetDateTime.class);
        Assert.assertEquals(value, OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOffsetDateTime_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalue.broken", OffsetDateTime.class);
    }

    @Test
    public void testGetOffsetDateTimeConverter() {
        OffsetDateTime value = config.getConverter(OffsetDateTime.class).get().convert("2007-12-03T10:15:30+01:00");
        Assert.assertEquals(value, OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetOffsetDateTimeConverter_Broken() {
        config.getConverter(OffsetDateTime.class).get().convert("alfasdf");
    }

    @Test
    public void testOffsetTime() {
        OffsetTime value = config.getValue("tck.config.test.javaconfig.converter.offsettimevalue", OffsetTime.class);
        OffsetTime parsed = OffsetTime.parse("13:45:30.123456789+02:00");
        Assert.assertEquals(value, parsed);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testOffsetTime_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.offsettimevalue.broken", OffsetTime.class);
    }

    @Test
    public void testGetOffsetTimeConverter() {
        OffsetTime value = config.getConverter(OffsetTime.class).get().convert("13:45:30.123456789+02:00");
        OffsetTime parsed = OffsetTime.parse("13:45:30.123456789+02:00");
        Assert.assertEquals(value, parsed);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetOffsetTimeConverter_Broken() {
        config.getConverter(OffsetTime.class).get().convert("alfasdf");
    }

    @Test
    public void testZoneOffset() {
        ZoneOffset value = config.getValue("tck.config.test.javaconfig.converter.zoneoffsetvalue", ZoneOffset.class);
        ZoneOffset parsed = ZoneOffset.of("+02:00");
        Assert.assertEquals(value, parsed);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testZoneOffset_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.zoneoffsetvalue.broken", ZoneOffset.class);
    }

    @Test
    public void testGetZoneOffsetConverter() {
        ZoneOffset value = config.getConverter(ZoneOffset.class).get().convert("+02:00");
        ZoneOffset parsed = ZoneOffset.of("+02:00");
        Assert.assertEquals(value, parsed);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetZoneOffsetConverter_Broken() {
        config.getConverter(ZoneOffset.class).get().convert("alfasdf");
    }

    @Test
    public void testInstant() {
        Instant value = config.getValue("tck.config.test.javaconfig.converter.instantvalue", Instant.class);
        Assert.assertEquals(value, Instant.parse("2015-06-02T21:34:33.616Z"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testInstant_Broken() {
        config.getValue("tck.config.test.javaconfig.converter.instantvalue.broken", Instant.class);
    }

    @Test
    public void testGetInstantConverter() {
        Instant value = config.getConverter(Instant.class).get().convert("2015-06-02T21:34:33.616Z");
        Assert.assertEquals(value, Instant.parse("2015-06-02T21:34:33.616Z"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetInstantConverter_Broken() {
        config.getConverter(Instant.class).get().convert("alfasdf");
    }

    @Test
    public void testBoolean() {
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.true", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.true", boolean.class));
        Assert.assertTrue(
                config.getValue("tck.config.test.javaconfig.configvalue.boolean.true_uppercase", Boolean.class));
        Assert.assertTrue(
                config.getValue("tck.config.test.javaconfig.configvalue.boolean.true_mixedcase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.false", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.one", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.zero", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.seventeen", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.yes", Boolean.class));
        Assert.assertTrue(
                config.getValue("tck.config.test.javaconfig.configvalue.boolean.yes_uppercase", Boolean.class));
        Assert.assertTrue(
                config.getValue("tck.config.test.javaconfig.configvalue.boolean.yes_mixedcase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.no", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.y", Boolean.class));
        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.y_uppercase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.n", Boolean.class));

        Assert.assertTrue(config.getValue("tck.config.test.javaconfig.configvalue.boolean.on", Boolean.class));
        Assert.assertTrue(
                config.getValue("tck.config.test.javaconfig.configvalue.boolean.on_uppercase", Boolean.class));
        Assert.assertTrue(
                config.getValue("tck.config.test.javaconfig.configvalue.boolean.on_mixedcase", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.off", Boolean.class));
        Assert.assertFalse(config.getValue("tck.config.test.javaconfig.configvalue.boolean.off", boolean.class));
    }

    @Test
    public void testGetBooleanConverter() {
        Converter<Boolean> converter = config.getConverter(Boolean.class).get();
        Converter<Boolean> primitiveConverter = config.getConverter(boolean.class).get();

        Assert.assertTrue(converter.convert("true"));
        Assert.assertTrue(primitiveConverter.convert("true"));
        Assert.assertTrue(converter.convert("TRUE"));
        Assert.assertTrue(converter.convert("TruE"));
        Assert.assertFalse(converter.convert("false"));

        Assert.assertTrue(converter.convert("1"));
        Assert.assertFalse(converter.convert("0"));
        Assert.assertFalse(converter.convert("17"));

        Assert.assertTrue(converter.convert("yes"));
        Assert.assertTrue(converter.convert("YES"));
        Assert.assertTrue(converter.convert("Yes"));
        Assert.assertFalse(converter.convert("no"));

        Assert.assertTrue(converter.convert("y"));
        Assert.assertTrue(converter.convert("Y"));
        Assert.assertFalse(converter.convert("n"));

        Assert.assertTrue(converter.convert("on"));
        Assert.assertTrue(converter.convert("ON"));
        Assert.assertTrue(converter.convert("oN"));
        Assert.assertFalse(converter.convert("off"));
        Assert.assertFalse(primitiveConverter.convert("off"));
    }

    @Test
    public void testCustomConverter() {
        Assert.assertEquals(namedDuck.getName(), "Hannelore");
    }

    @Test
    public void testGetCustomConverter() {
        Assert.assertEquals(config.getConverter(Duck.class).get().convert("Hannelore").getName(), "Hannelore");
    }

    @Test
    public void testDuckConversionWithMultipleConverters() {
        // defines 2 config with the converters defined in different orders.
        // Order must not matter, the UpperCaseDuckConverter must always be used as it has the highest priority
        Config config1 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverters(new UpperCaseDuckConverter(), new DuckConverter())
                .build();
        Config config2 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverters(new DuckConverter(), new UpperCaseDuckConverter())
                .build();

        Duck duck = config1.getValue("tck.config.test.javaconfig.converter.duckname", Duck.class);
        Assert.assertNotNull(duck);
        Assert.assertEquals(duck.getName(), "HANNELORE",
                "The converter with the highest priority (UpperCaseDuckConverter) must be used.");

        duck = config2.getValue("tck.config.test.javaconfig.converter.duckname", Duck.class);
        Assert.assertNotNull(duck);
        // the UpperCaseDuckConverter has highest priority
        Assert.assertEquals(duck.getName(), "HANNELORE",
                "The converter with the highest priority (UpperCaseDuckConverter) must be used.");
    }

    @Test
    public void testGetDuckConverterWithMultipleConverters() {
        // defines 2 config with the converters defined in different orders.
        // Order must not matter, the UpperCaseDuckConverter must always be used as it has the highest priority
        Config config1 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverters(new UpperCaseDuckConverter(), new DuckConverter())
                .build();
        Config config2 = ConfigProviderResolver.instance().getBuilder().addDefaultSources()
                .withConverters(new DuckConverter(), new UpperCaseDuckConverter())
                .build();

        Duck duck = config1.getConverter(Duck.class).get().convert("Hannelore");
        Assert.assertNotNull(duck);
        Assert.assertEquals(duck.getName(), "HANNELORE",
                "The converter with the highest priority (UpperCaseDuckConverter) must be used.");

        duck = config2.getConverter(Duck.class).get().convert("Hannelore");
        Assert.assertNotNull(duck);
        // the UpperCaseDuckConverter has highest priority
        Assert.assertEquals(duck.getName(), "HANNELORE",
                "The converter with the highest priority (UpperCaseDuckConverter) must be used.");
    }

    @Test
    public void testURLConverter() throws MalformedURLException {
        URL url = config.getValue("tck.config.test.javaconfig.converter.urlvalue", URL.class);
        Assert.assertEquals(url, new URL("http://microprofile.io"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testURLConverterBroken() throws Exception {
        config.getValue("tck.config.test.javaconfig.converter.urlvalue.broken", URL.class);
    }

    @Test
    public void testGetURLConverter() throws MalformedURLException {
        URL url = config.getConverter(URL.class).get().convert("http://microprofile.io");
        Assert.assertEquals(url, new URL("http://microprofile.io"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetURLConverterBroken() throws Exception {
        config.getConverter(URL.class).get().convert("tt:--location");
    }

    @Test
    public void testURIConverter() {
        URI uri = config.getValue("tck.config.test.javaconfig.converter.urivalue", URI.class);
        Assert.assertEquals(uri, URI.create("http://microprofile.io"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testURIConverterBroken() throws Exception {
        config.getValue("tck.config.test.javaconfig.converter.urivalue.broken", URI.class);
    }

    @Test
    public void testGetURIConverter() {
        URI uri = config.getConverter(URI.class).get().convert("http://microprofile.io");
        Assert.assertEquals(uri, URI.create("http://microprofile.io"));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testGetURIConverterBroken() throws Exception {
        config.getConverter(URI.class).get().convert("space is an illegal uri character");
    }

    @Test
    public void testConverterSerialization() {
        DuckConverter original = new DuckConverter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream)) {
            out.writeObject(original);
        } catch (IOException ex) {
            Assert.fail("Converter instance should be serializable, but could not serialize it", ex);
        }
        Object readObject = null;
        try (ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))) {
            readObject = in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Assert.fail(
                    "Converter instance should be serializable, but could not deserialize a previously serialized instance",
                    ex);
        }
        Assert.assertEquals(((DuckConverter) ((Converter<?>) readObject)).convert("Donald").getName(),
                original.convert("Donald").getName(), "Converted values to be equal");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetConverterSerialization() {
        Converter<Duck> original = config.getConverter(Duck.class).get();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream)) {
            out.writeObject(original);
        } catch (IOException ex) {
            Assert.fail("Converter instance should be serializable, but could not serialize it", ex);
        }
        Object readObject = null;
        try (ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))) {
            readObject = in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Assert.fail(
                    "Converter instance should be serializable, but could not deserialize a previously serialized instance",
                    ex);
        }
        Assert.assertEquals(((Converter<Duck>) readObject).convert("Donald").getName(),
                original.convert("Donald").getName(), "Converted values to be equal");
    }
}
