/*
 * Copyright (c) 2017, 2021 Contributors to the Eclipse Foundation
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

import jakarta.inject.Inject;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.MapConverterBean.EnumKey;
import org.eclipse.microprofile.config.tck.MapConverterBean.EnumValue;
import org.eclipse.microprofile.config.tck.converters.Pizza;
import org.eclipse.microprofile.config.tck.converters.PizzaConverter;
import org.eclipse.microprofile.config.tck.util.AdditionalAssertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;
import static org.eclipse.microprofile.config.tck.util.AdditionalAssertions.assertURLArrayEquals;
import static org.eclipse.microprofile.config.tck.util.AdditionalAssertions.assertURLListEquals;
import static org.eclipse.microprofile.config.tck.util.AdditionalAssertions.assertURLSetEquals;

/**
 * Test the implicit converter handling.
 **/
public class MapConverterTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "mapConverterTest.jar")
            .addPackage(PizzaConverter.class.getPackage())
            .addClasses(MapConverterTest.class, MapConverterBean.class, AdditionalAssertions.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsServiceProvider(Converter.class, PizzaConverter.class)
            .as(JavaArchive.class);
        addFile(testJar, "META-INF/microprofile-config.properties");
        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "mapConverterTest.war")
            .addAsLibrary(testJar);
        return war;
    }

    @Inject
    private MapConverterBean converterBean;

    /////////////////////////////////// Test Map//////////////////////////


    @Test
    public void testStringStringMapInjection() {
        Assert.assertEquals(converterBean.getMyStringStringMap().size(), 2);
        Assert.assertEquals(converterBean.getMyStringStringMap().get("key1"), "string.string.value1");
        Assert.assertEquals(converterBean.getMyStringStringMap().get("key2"), "string.string.value2");
    }

    @Test
    public void testStringIntegerMapInjection() {
        Assert.assertEquals(converterBean.getMyStringIntegerMap().size(), 2);
        Assert.assertEquals(converterBean.getMyStringIntegerMap().get("key1"), Integer.valueOf(100));
        Assert.assertEquals(converterBean.getMyStringIntegerMap().get("key2"), Integer.valueOf(200));
    }

    @Test
    public void testIntegerStringMapInjection() {
        Assert.assertEquals(converterBean.getMyIntegerStringMap().size(), 2);
        Assert.assertEquals(converterBean.getMyIntegerStringMap().get(100), "integer.string.value1");
        Assert.assertEquals(converterBean.getMyIntegerStringMap().get(200), "integer.string.value2");
    }

    @Test
    public void testEnumEnumMapInjection() {
        Assert.assertEquals(converterBean.getMyEnumEnumMap().size(), 2);
        Assert.assertEquals(converterBean.getMyEnumEnumMap().get(EnumKey.key1), EnumValue.enum1);
        Assert.assertEquals(converterBean.getMyEnumEnumMap().get(EnumKey.key2), EnumValue.enum2);
    }
}
