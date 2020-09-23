/*
 * Copyright (c) 2017 Contributors to the Eclipse Foundation
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

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;

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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.converters.Pizza;
import org.eclipse.microprofile.config.tck.converters.PizzaConverter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test the implicit converter handling.
 *
 * @author <a href="mailto:emijiang6@googlemail.com">Emily Jiang</a>
 */
public class ArrayConverterTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "arrayConverterTest.jar")
                .addPackage(PizzaConverter.class.getPackage())
                .addClasses(ArrayConverterTest.class, ArrayConverterBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Converter.class, PizzaConverter.class)
                .as(JavaArchive.class);
        addFile(testJar, "META-INF/microprofile-config.properties");
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "arrayConverterTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Inject
    private Config config;

    @Inject
    private ArrayConverterBean converterBean;

    /////////////////////////////////// Test Boolean[] boolean[]//////////////////////////

    @Test
    public void testBooleanArrayLookupProgrammatically() {
        Boolean[] value = config.getValue("tck.config.test.javaconfig.converter.booleanvalues",
                Boolean[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Boolean[]{true, false, true});
        Boolean[] single = config.getValue("tck.config.test.javaconfig.configvalue.boolean.true",
                Boolean[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Boolean[]{true});
    }

    @Test
    public void testGetBooleanArrayConverter() {
        Boolean[] value = config.getConverter(Boolean[].class).get().convert("true,false,true");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Boolean[]{true, false, true});
        Boolean[] single = config.getConverter(Boolean[].class).get().convert("true");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Boolean[]{true});
    }

    @Test
    public void testOptionalBooleanArrayLookupProgrammatically() {
        Optional<Boolean[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.booleanvalues",
                        Boolean[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Boolean[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Boolean[]{true, false, true});
        Optional<Boolean[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.configvalue.boolean.true",
                        Boolean[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Boolean[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Boolean[]{true});
    }

    @Test
    public void testBooleanListLookupProgrammatically() {
        List<Boolean> values = config.getValues("tck.config.test.javaconfig.converter.booleanvalues",
                Boolean.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values, Arrays.asList(true, false, true));
        List<Boolean> single = config.getValues("tck.config.test.javaconfig.configvalue.boolean.true",
                Boolean.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(true));
    }

    @Test
    public void testOptionalBooleanListLookupProgrammatically() {
        Optional<List<Boolean>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.booleanvalues",
                        Boolean.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Boolean> value = optionalValues.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.size(), 3);
        Assert.assertEquals(value, Arrays.asList(true, false, true));
        Optional<List<Boolean>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.configvalue.boolean.true",
                        Boolean.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Boolean> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(true));
    }

    @Test
    public void testBooleanArrayInjection() {
        Assert.assertEquals(converterBean.getMyBooleanArray().length, 3);
        Assert.assertEquals(converterBean.getMyBooleanArray(), new Boolean[]{true, false, true});
        Assert.assertEquals(converterBean.getMySingleBooleanArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleBooleanArray(), new Boolean[]{true});
    }

    // test boolean[] support

    @Test
    public void testGetbooleanArrayConverter() {
        boolean[] value = config.getConverter(boolean[].class).get().convert("true,false,true");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Boolean[]{true, false, true});
        boolean[] single = config.getConverter(boolean[].class).get().convert("true");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Boolean[]{true});
    }

    @Test
    public void testbooleanArrayInjection() {
        Assert.assertEquals(converterBean.getMybooleanArray().length, 3);
        Assert.assertEquals(converterBean.getMybooleanArray(), new boolean[]{true, false, true});
        Assert.assertEquals(converterBean.getMySinglebooleanArray().length, 1);
        Assert.assertEquals(converterBean.getMySinglebooleanArray(), new boolean[]{true});
    }

    @Test
    public void testbooleanListInjection() {
        Assert.assertEquals(converterBean.getMyBooleanList().size(), 3);
        Assert.assertEquals(converterBean.getMyBooleanList(), Arrays.asList(true, false, true));
        Assert.assertEquals(converterBean.getMySingleBooleanList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleBooleanList(), Arrays.asList(true));
    }

    @Test
    public void testbooleanSetInjection() {
        Assert.assertEquals(converterBean.getMyBooleanSet().size(), 2);
        Assert.assertEquals(converterBean.getMyBooleanSet(), new LinkedHashSet<>(Arrays.asList(true, false, true)));
        Assert.assertEquals(converterBean.getMySingleBooleanSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleBooleanSet(), Collections.singleton(true));
    }

    /////////////////////////////////// Test String[] //////////////////////////

    @Test
    public void testStringArrayLookupProgrammatically() {
        String[] value = config.getValue("tck.config.test.javaconfig.converter.stringvalues",
                String[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 4);
        Assert.assertEquals(value, new String[]{"microservice", "microprofile", "m,f", "microservice"});
        String[] single = config.getValue("tck.config.test.javaconfig.configvalue.key1",
                String[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new String[]{"value1"});
    }

    @Test
    public void testGetStringArrayConverter() {
        String[] value =
                config.getConverter(String[].class).get().convert("microservice,microprofile,m\\,f,microservice");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 4);
        Assert.assertEquals(value, new String[]{"microservice", "microprofile", "m,f", "microservice"});
        String[] single = config.getConverter(String[].class).get().convert("value1");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new String[]{"value1"});
    }

    @Test
    public void testOptionalStringArrayLookupProgrammatically() {
        Optional<String[]> optionalValue = config.getOptionalValue("tck.config.test.javaconfig.converter.stringvalues",
                String[].class);
        Assert.assertTrue(optionalValue.isPresent());
        String[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 4);
        Assert.assertEquals(value, new String[]{"microservice", "microprofile", "m,f", "microservice"});
        Optional<String[]> optionalSingle = config.getOptionalValue("tck.config.test.javaconfig.configvalue.key1",
                String[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        String[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new String[]{"value1"});
    }

    @Test
    public void testStringListLookupProgrammatically() {
        List<String> values = config.getValues("tck.config.test.javaconfig.converter.stringvalues",
                String.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 4);
        Assert.assertEquals(values, Arrays.asList("microservice", "microprofile", "m,f", "microservice"));
        List<String> single = config.getValues("tck.config.test.javaconfig.configvalue.key1",
                String.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList("value1"));
    }

    @Test
    public void testOptionalStringListLookupProgrammatically() {
        Optional<List<String>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.stringvalues",
                        String.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<String> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 4);
        Assert.assertEquals(values, Arrays.asList("microservice", "microprofile", "m,f", "microservice"));
        Optional<List<String>> optionalSingle = config.getOptionalValues("tck.config.test.javaconfig.configvalue.key1",
                String.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<String> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList("value1"));
    }

    @Test
    public void testStringArrayInjection() {
        Assert.assertEquals(converterBean.getMyStringArray().length, 4);
        Assert.assertEquals(converterBean.getMyStringArray(),
                new String[]{"microservice", "microprofile", "m,f", "microservice"});
        Assert.assertEquals(converterBean.getMySingleStringArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleStringArray(), new String[]{"value1"});
    }

    @Test
    public void testStringListInjection() {
        Assert.assertEquals(converterBean.getMyStringList().size(), 4);
        Assert.assertEquals(converterBean.getMyStringList(),
                Arrays.asList("microservice", "microprofile", "m,f", "microservice"));
        Assert.assertEquals(converterBean.getMySingleStringList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleStringList(), Arrays.asList("value1"));
    }

    @Test
    public void testStringSetInjection() {
        Assert.assertEquals(converterBean.getMyStringSet().size(), 3);
        Assert.assertEquals(converterBean.getMyStringSet(),
                new LinkedHashSet<>(Arrays.asList("microservice", "microprofile", "m,f", "microservice")));
        Assert.assertEquals(converterBean.getMySingleStringSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleStringSet(), Collections.singleton("value1"));
    }

    ///////////////////////////////////////////////////////////////////
    // test Integer[] support
    /////////////////////////////////// Test Integer[] int[]//////////////////////////

    @Test
    public void testIntegerArrayLookupProgrammatically() {
        Integer[] value = config.getValue("tck.config.test.javaconfig.converter.integervalues",
                Integer[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Integer[]{1234, 9999});
        Integer[] single = config.getValue("tck.config.test.javaconfig.converter.integervalue",
                Integer[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Integer[]{1234});
    }

    @Test
    public void testGetIntegerArrayConverter() {
        Integer[] value = config.getConverter(Integer[].class).get().convert("1234,9999");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Integer[]{1234, 9999});
        Integer[] single = config.getConverter(Integer[].class).get().convert("1234");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Integer[]{1234});
    }

    @Test
    public void testOptionalIntegerArrayLookupProgrammatically() {
        Optional<Integer[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.integervalues",
                        Integer[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Integer[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Integer[]{1234, 9999});
        Optional<Integer[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.integervalue",
                        Integer[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Integer[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Integer[]{1234});
    }

    @Test
    public void testIntegerListLookupProgrammatically() {
        List<Integer> values = config.getValues("tck.config.test.javaconfig.converter.integervalues",
                Integer.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(1234, 9999));
        List<Integer> single = config.getValues("tck.config.test.javaconfig.converter.integervalue",
                Integer.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(1234));
    }

    @Test
    public void testOptionalIntegerListLookupProgrammatically() {
        Optional<List<Integer>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.integervalues",
                        Integer.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Integer> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(1234, 9999));
        Optional<List<Integer>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.integervalue",
                        Integer.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Integer> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(1234));
    }

    @Test
    public void testIntArrayInjection() {
        Assert.assertEquals(converterBean.getMyIntegerArray().length, 2);
        Assert.assertEquals(converterBean.getMyIntegerArray(), new Integer[]{1234, 9999});
        Assert.assertEquals(converterBean.getMySingleIntegerArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleIntegerArray(), new Integer[]{1234});
    }

    // test int[] support

    @Test
    public void testGetIntArrayConverter() {
        int[] value = config.getConverter(int[].class).get().convert("1234,9999");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Integer[]{1234, 9999});
        int[] single = config.getConverter(int[].class).get().convert("1234");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Integer[]{1234});
    }

    @Test
    public void testintArrayInjection() {
        Assert.assertEquals(converterBean.getMyintArray().length, 2);
        Assert.assertEquals(converterBean.getMyIntegerArray(), new int[]{1234, 9999});
        Assert.assertEquals(converterBean.getMySingleintArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleIntegerArray(), new int[]{1234});
    }

    @Test
    public void testIntListInjection() {
        Assert.assertEquals(converterBean.getMyIntegerList().size(), 2);
        Assert.assertEquals(converterBean.getMyIntegerList(), Arrays.asList(1234, 9999));
        Assert.assertEquals(converterBean.getMySingleIntegerList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleIntegerList(), Arrays.asList(1234));
    }

    @Test
    public void testIntSetInjection() {
        Assert.assertEquals(converterBean.getMyIntegerSet().size(), 2);
        Assert.assertEquals(converterBean.getMyIntegerSet(), new LinkedHashSet<>(Arrays.asList(1234, 9999)));
        Assert.assertEquals(converterBean.getMySingleIntegerSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleIntegerSet(), Collections.singleton(1234));
    }

    ///////////////////////////////////////////////////////////////////
    ////////////////// Test Long[] long[]///////////////////////////////
    // test Long[] support

    @Test
    public void testLongArrayLookupProgrammatically() {
        Long[] value = config.getValue("tck.config.test.javaconfig.converter.longvalues",
                Long[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Long[]{1234567890L, 1999999999L});
        Long[] single = config.getValue("tck.config.test.javaconfig.converter.longvalue",
                Long[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Long[]{1234567890L});
    }

    @Test
    public void testGetLongArrayCoverter() {
        Long[] value = config.getConverter(Long[].class).get().convert("1234567890,1999999999");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Long[]{1234567890L, 1999999999L});
        Long[] single = config.getConverter(Long[].class).get().convert("1234567890");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Long[]{1234567890L});
    }

    @Test
    public void testOptionalLongArrayLookupProgrammatically() {
        Optional<Long[]> optionalValue = config.getOptionalValue("tck.config.test.javaconfig.converter.longvalues",
                Long[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Long[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Long[]{1234567890L, 1999999999L});
        Optional<Long[]> optionalSingle = config.getOptionalValue("tck.config.test.javaconfig.converter.longvalue",
                Long[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Long[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Long[]{1234567890L});
    }

    @Test
    public void testLongListLookupProgrammatically() {
        List<Long> values = config.getValues("tck.config.test.javaconfig.converter.longvalues",
                Long.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(1234567890L, 1999999999L));
        List<Long> single = config.getValues("tck.config.test.javaconfig.converter.longvalue",
                Long.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(1234567890L));
    }

    @Test
    public void testOptionalLongListLookupProgrammatically() {
        Optional<List<Long>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.longvalues",
                        Long.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Long> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(1234567890L, 1999999999L));
        Optional<List<Long>> optionalSingle = config.getOptionalValues("tck.config.test.javaconfig.converter.longvalue",
                Long.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Long> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(1234567890L));
    }

    @Test
    public void testLongArrayInjection() {
        Assert.assertEquals(converterBean.getMyLongArray().length, 2);
        Assert.assertEquals(converterBean.getMyLongArray(), new Long[]{1234567890L, 1999999999L});
        Assert.assertEquals(converterBean.getMySingleLongArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleLongArray(), new Long[]{1234567890L});
    }

    // test long[] support

    @Test
    public void testGetlongArrayCoverter() {
        long[] value = config.getConverter(long[].class).get().convert("1234567890,1999999999");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Long[]{1234567890L, 1999999999L});
        long[] single = config.getConverter(long[].class).get().convert("1234567890");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Long[]{1234567890L});
    }

    @Test
    public void testlongArrayInjection() {
        Assert.assertEquals(converterBean.getMylongArray().length, 2);
        Assert.assertEquals(converterBean.getMylongArray(), new long[]{1234567890L, 1999999999L});
        Assert.assertEquals(converterBean.getMySinglelongArray().length, 1);
        Assert.assertEquals(converterBean.getMySinglelongArray(), new long[]{1234567890L});
    }

    @Test
    public void testLongListInjection() {
        Assert.assertEquals(converterBean.getMyLongList().size(), 2);
        Assert.assertEquals(converterBean.getMyLongList(), Arrays.asList(1234567890L, 1999999999L));
        Assert.assertEquals(converterBean.getMySingleLongList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLongList(), Arrays.asList(1234567890L));
    }

    @Test
    public void testLongSetInjection() {
        Assert.assertEquals(converterBean.getMyLongSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLongSet(), new LinkedHashSet<>(Arrays.asList(1234567890L, 1999999999L)));
        Assert.assertEquals(converterBean.getMySingleLongSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLongSet(), Collections.singleton(1234567890L));
    }

    /////////////////////////////////// Test Float[] float[]/////////////////////

    // test Float[] support

    @Test
    public void testFloatArrayLookupProgrammatically() {
        Float[] value = config.getValue("tck.config.test.javaconfig.converter.floatvalues",
                Float[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Float[]{12.34f, 99.99f});
        Float[] single = config.getValue("tck.config.test.javaconfig.converter.floatvalue",
                Float[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Float[]{12.34f});
    }

    @Test
    public void testGetFloatArrayConverter() {
        Float[] value = config.getConverter(Float[].class).get().convert("12.34,99.99");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Float[]{12.34f, 99.99f});
        Float[] single = config.getConverter(Float[].class).get().convert("12.34");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Float[]{12.34f});
    }

    @Test
    public void testOptionalFloatArrayLookupProgrammatically() {
        Optional<Float[]> optionalValue = config.getOptionalValue("tck.config.test.javaconfig.converter.floatvalues",
                Float[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Float[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Float[]{12.34f, 99.99f});
        Optional<Float[]> optionalSingle = config.getOptionalValue("tck.config.test.javaconfig.converter.floatvalue",
                Float[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Float[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Float[]{12.34f});
    }

    @Test
    public void testFloatListLookupProgrammatically() {
        List<Float> values = config.getValues("tck.config.test.javaconfig.converter.floatvalues",
                Float.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(12.34f, 99.99f));
        List<Float> single = config.getValues("tck.config.test.javaconfig.converter.floatvalue",
                Float.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(12.34f));
    }

    @Test
    public void testOptionalFloatListLookupProgrammatically() {
        Optional<List<Float>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.floatvalues",
                        Float.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Float> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(12.34f, 99.99f));
        Optional<List<Float>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.floatvalue",
                        Float.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Float> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(12.34f));
    }

    @Test
    public void testFloatArrayInjection() {
        Assert.assertEquals(converterBean.getMyFloatArray().length, 2);
        Assert.assertEquals(converterBean.getMyFloatArray(), new Float[]{12.34f, 99.99f});
        Assert.assertEquals(converterBean.getMySingleFloatArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleFloatArray(), new Float[]{12.34f});
    }

    // test float[] support

    @Test
    public void testGetfloatArrayConverter() {
        float[] value = config.getConverter(float[].class).get().convert("12.34,99.99");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Float[]{12.34f, 99.99f});
        float[] single = config.getConverter(float[].class).get().convert("12.34");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Float[]{12.34f});
    }

    @Test
    public void testfloatArrayInjection() {
        Assert.assertEquals(converterBean.getMyfloatArray().length, 2);
        Assert.assertEquals(converterBean.getMyfloatArray(), new float[]{12.34f, 99.99f});
        Assert.assertEquals(converterBean.getMySinglefloatArray().length, 1);
        Assert.assertEquals(converterBean.getMySinglefloatArray(), new float[]{12.34f});
    }

    @Test
    public void testFloatListInjection() {
        Assert.assertEquals(converterBean.getMyFloatList().size(), 2);
        Assert.assertEquals(converterBean.getMyFloatList(), Arrays.asList(12.34f, 99.99f));
        Assert.assertEquals(converterBean.getMySingleFloatList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleFloatList(), Arrays.asList(12.34f));
    }

    @Test
    public void testFloatSetInjection() {
        Assert.assertEquals(converterBean.getMyFloatSet().size(), 2);
        Assert.assertEquals(converterBean.getMyFloatSet(), new LinkedHashSet<>(Arrays.asList(12.34f, 99.99f)));
        Assert.assertEquals(converterBean.getMySingleFloatSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleFloatSet(), Collections.singleton(12.34f));
    }

    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////// Test Double[] double[]/////////////

    @Test
    public void testDoubleArrayLookupProgrammatically() {
        Double[] value = config.getValue("tck.config.test.javaconfig.converter.doublevalues",
                Double[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Double[]{12.34d, 99.9999d});
        Double[] single = config.getValue("tck.config.test.javaconfig.converter.doublevalue",
                Double[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Double[]{12.34d});
    }

    @Test
    public void testGetDoubleArrayConverter() {
        Double[] value = config.getConverter(Double[].class).get().convert("12.34,99.9999");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Double[]{12.34d, 99.9999d});
        Double[] single = config.getConverter(Double[].class).get().convert("12.34");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Double[]{12.34d});
    }

    @Test
    public void testOptionalDoubleArrayLookupProgrammatically() {
        Optional<Double[]> optionalValue = config.getOptionalValue("tck.config.test.javaconfig.converter.doublevalues",
                Double[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Double[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Double[]{12.34d, 99.9999d});
        Optional<Double[]> optionalSingle = config.getOptionalValue("tck.config.test.javaconfig.converter.doublevalue",
                Double[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Double[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Double[]{12.34d});
    }

    @Test
    public void testDoubleListLookupProgrammatically() {
        List<Double> values = config.getValues("tck.config.test.javaconfig.converter.doublevalues",
                Double.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(12.34d, 99.9999d));
        List<Double> single = config.getValues("tck.config.test.javaconfig.converter.doublevalue",
                Double.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(12.34d));
    }

    @Test
    public void testOptionalDoubleListLookupProgrammatically() {
        Optional<List<Double>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.doublevalues",
                        Double.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Double> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(12.34d, 99.9999d));
        Optional<List<Double>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.doublevalue",
                        Double.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Double> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(12.34d));
    }

    @Test
    public void testDoubleArrayInjection() {
        Assert.assertEquals(converterBean.getMyDoubleArray().length, 2);
        Assert.assertEquals(converterBean.getMyDoubleArray(), new Double[]{12.34d, 99.9999d});
        Assert.assertEquals(converterBean.getMySingleDoubleArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleDoubleArray(), new Double[]{12.34d});
    }

    // test double[] support

    @Test
    public void testGetdoubleArrayConverter() {
        double[] value = config.getConverter(double[].class).get().convert("12.34,99.9999");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Double[]{12.34d, 99.9999d});
        double[] single = config.getConverter(double[].class).get().convert("12.34");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Double[]{12.34d});
    }

    @Test
    public void testdoubleArrayInjection() {
        Assert.assertEquals(converterBean.getMydoubleArray().length, 2);
        Assert.assertEquals(converterBean.getMydoubleArray(), new double[]{12.34d, 99.9999d});
        Assert.assertEquals(converterBean.getMySingledoubleArray().length, 1);
        Assert.assertEquals(converterBean.getMySingledoubleArray(), new double[]{12.34d});
    }

    @Test
    public void testDoubleListInjection() {
        Assert.assertEquals(converterBean.getMyDoubleList().size(), 2);
        Assert.assertEquals(converterBean.getMyDoubleList(), Arrays.asList(12.34d, 99.9999d));
        Assert.assertEquals(converterBean.getMySingleDoubleList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleDoubleList(), Arrays.asList(12.34d));
    }

    @Test
    public void testDoubleSetInjection() {
        Assert.assertEquals(converterBean.getMyDoubleSet().size(), 2);
        Assert.assertEquals(converterBean.getMyDoubleSet(), new LinkedHashSet<>(Arrays.asList(12.34d, 99.9999d)));
        Assert.assertEquals(converterBean.getMySingleDoubleSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleDoubleSet(), Collections.singleton(12.34d));
    }

    //////////////////////////////////////////////////////////////////////
    // test duration
    /////////////////////////////////// Test Duration/////////////////////

    @Test
    public void testDurationArrayLookupProgrammatically() {
        Duration[] value = config.getValue("tck.config.test.javaconfig.converter.durationvalues",
                Duration[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Duration[]{
                Duration.parse("PT15M"),
                Duration.parse("PT20M")});
        Duration[] single = config.getValue("tck.config.test.javaconfig.converter.durationvalue",
                Duration[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Duration[]{Duration.parse("PT15M")});
    }

    @Test
    public void testGetDurationArrayConverter() {
        Duration[] value = config.getConverter(Duration[].class).get().convert("PT15M,PT20M");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Duration[]{
                Duration.parse("PT15M"),
                Duration.parse("PT20M")});
        Duration[] single = config.getConverter(Duration[].class).get().convert("PT15M");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Duration[]{Duration.parse("PT15M")});
    }

    @Test
    public void testOptionalDurationArrayLookupProgrammatically() {
        Optional<Duration[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.durationvalues",
                        Duration[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Duration[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Duration[]{
                Duration.parse("PT15M"),
                Duration.parse("PT20M")});
        Optional<Duration[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.durationvalue",
                        Duration[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Duration[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Duration[]{Duration.parse("PT15M")});
    }

    @Test
    public void testDurationListLookupProgrammatically() {
        List<Duration> values = config.getValues("tck.config.test.javaconfig.converter.durationvalues",
                Duration.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                Duration.parse("PT15M"),
                Duration.parse("PT20M")));
        List<Duration> single = config.getValues("tck.config.test.javaconfig.converter.durationvalue",
                Duration.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(Duration.parse("PT15M")));
    }

    @Test
    public void testOptionalDurationListLookupProgrammatically() {
        Optional<List<Duration>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.durationvalues",
                        Duration.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Duration> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                Duration.parse("PT15M"),
                Duration.parse("PT20M")));
        Optional<List<Duration>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.durationvalue",
                        Duration.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Duration> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(Duration.parse("PT15M")));
    }

    @Test
    public void testDurationArrayInjection() {
        Assert.assertEquals(converterBean.getMyDurationArray().length, 2);
        Assert.assertEquals(converterBean.getMyDurationArray(), new Duration[]{
                Duration.parse("PT15M"),
                Duration.parse("PT20M")});
        Assert.assertEquals(converterBean.getMySingleDurationArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleDurationArray(), new Duration[]{Duration.parse("PT15M")});
    }

    @Test
    public void testDurationListInjection() {
        Assert.assertEquals(converterBean.getMyDurationList().size(), 2);
        Assert.assertEquals(converterBean.getMyDurationList(), Arrays.asList(
                Duration.parse("PT15M"),
                Duration.parse("PT20M")));
        Assert.assertEquals(converterBean.getMySingleDurationList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleDurationList(), Arrays.asList(Duration.parse("PT15M")));
    }

    @Test
    public void testDurationSetInjection() {
        Assert.assertEquals(converterBean.getMyDurationSet().size(), 2);
        Assert.assertEquals(converterBean.getMyDurationSet(), new LinkedHashSet<>(Arrays.asList(
                Duration.parse("PT15M"),
                Duration.parse("PT20M"))));
        Assert.assertEquals(converterBean.getMySingleDurationSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleDurationSet(), Collections.singleton(Duration.parse("PT15M")));
    }

    //////////////////////////////////////////////////////////////////////
    // LocalTime
    /////////////////////////////////// Test LocalTime/////////////////////

    @Test
    public void testLocalTimeArrayLookupProgrammatically() {
        LocalTime[] value = config.getValue("tck.config.test.javaconfig.converter.localtimevalues",
                LocalTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalTime[]{
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")});
        LocalTime[] single = config.getValue("tck.config.test.javaconfig.converter.localtimevalue",
                LocalTime[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalTime[]{LocalTime.parse("10:37")});
    }

    @Test
    public void testGetLocalTimeArrayConverter() {
        LocalTime[] value = config.getConverter(LocalTime[].class).get().convert("10:37,11:44");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalTime[]{
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")});
        LocalTime[] single = config.getConverter(LocalTime[].class).get().convert("10:37");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalTime[]{LocalTime.parse("10:37")});
    }

    @Test
    public void testOptionalLocalTimeArrayLookupProgrammatically() {
        Optional<LocalTime[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.localtimevalues",
                        LocalTime[].class);
        Assert.assertTrue(optionalValue.isPresent());
        LocalTime[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalTime[]{
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")});
        Optional<LocalTime[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.localtimevalue",
                        LocalTime[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        LocalTime[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalTime[]{LocalTime.parse("10:37")});
    }

    @Test
    public void testLocalTimeListLookupProgrammatically() {
        List<LocalTime> values = config.getValues("tck.config.test.javaconfig.converter.localtimevalues",
                LocalTime.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")));
        List<LocalTime> single = config.getValues("tck.config.test.javaconfig.converter.localtimevalue",
                LocalTime.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(LocalTime.parse("10:37")));
    }

    @Test
    public void testOptionalLocalTimeListLookupProgrammatically() {
        Optional<List<LocalTime>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.localtimevalues",
                        LocalTime.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<LocalTime> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")));
        Optional<List<LocalTime>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.localtimevalue",
                        LocalTime.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<LocalTime> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(LocalTime.parse("10:37")));
    }

    @Test
    public void testLocalTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyLocaltimeArray().length, 2);
        Assert.assertEquals(converterBean.getMyLocaltimeArray(), new LocalTime[]{
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")});
        Assert.assertEquals(converterBean.getMySingleLocaltimeArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleLocaltimeArray(), new LocalTime[]{LocalTime.parse("10:37")});
    }

    @Test
    public void testLocalTimeListInjection() {
        Assert.assertEquals(converterBean.getMyLocalTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalTimeList(), Arrays.asList(
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44")));
        Assert.assertEquals(converterBean.getMySingleLocalTimeList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLocalTimeList(), Arrays.asList(LocalTime.parse("10:37")));
    }

    @Test
    public void testLocalTimeSetInjection() {
        Assert.assertEquals(converterBean.getMyLocalTimeSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalTimeSet(), new LinkedHashSet<>(Arrays.asList(
                LocalTime.parse("10:37"),
                LocalTime.parse("11:44"))));
        Assert.assertEquals(converterBean.getMySingleLocalTimeSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLocalTimeSet(), Collections.singleton(LocalTime.parse("10:37")));
    }

    //////////////////////////////////////////////////////////////////////
    /////////////////////////// Test LocalDate////////////////////////////

    @Test
    public void testLocalDateArrayLookupProgrammatically() {
        LocalDate[] value = config.getValue("tck.config.test.javaconfig.converter.localdatevalues",
                LocalDate[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDate[]{
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")});
        LocalDate[] single = config.getValue("tck.config.test.javaconfig.converter.localdatevalue",
                LocalDate[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalDate[]{LocalDate.parse("2017-12-24")});
    }

    @Test
    public void testGetLocalDateArrayConverter() {
        LocalDate[] value = config.getConverter(LocalDate[].class).get().convert("2017-12-24,2017-11-29");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDate[]{
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")});
        LocalDate[] single = config.getConverter(LocalDate[].class).get().convert("2017-12-24");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalDate[]{LocalDate.parse("2017-12-24")});
    }

    @Test
    public void testOptionalLocalDateArrayLookupProgrammatically() {
        Optional<LocalDate[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.localdatevalues",
                        LocalDate[].class);
        Assert.assertTrue(optionalValue.isPresent());
        LocalDate[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDate[]{
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")});
        Optional<LocalDate[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.localdatevalue",
                        LocalDate[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        LocalDate[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalDate[]{LocalDate.parse("2017-12-24")});
    }

    @Test
    public void testLocalDateListLookupProgrammatically() {
        List<LocalDate> values = config.getValues("tck.config.test.javaconfig.converter.localdatevalues",
                LocalDate.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")));
        List<LocalDate> single = config.getValues("tck.config.test.javaconfig.converter.localdatevalue",
                LocalDate.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(LocalDate.parse("2017-12-24")));
    }

    @Test
    public void testOptionalLocalDateListLookupProgrammatically() {
        Optional<List<LocalDate>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.localdatevalues",
                        LocalDate.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<LocalDate> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")));
        Optional<List<LocalDate>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.localdatevalue",
                        LocalDate.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<LocalDate> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(LocalDate.parse("2017-12-24")));
    }

    @Test
    public void testLocalDateArrayInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateArray().length, 2);
        Assert.assertEquals(converterBean.getMyLocalDateArray(), new LocalDate[]{
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")});
        Assert.assertEquals(converterBean.getMySingleLocalDateArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleLocalDateArray(), new LocalDate[]{LocalDate.parse("2017-12-24")});
    }

    @Test
    public void testLocalDateListInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateList().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateList(), Arrays.asList(
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29")));
        Assert.assertEquals(converterBean.getMySingleLocalDateList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLocalDateList(), Arrays.asList(LocalDate.parse("2017-12-24")));
    }

    @Test
    public void testLocalDateSetInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateSet(), new LinkedHashSet<>(Arrays.asList(
                LocalDate.parse("2017-12-24"),
                LocalDate.parse("2017-11-29"))));
        Assert.assertEquals(converterBean.getMySingleLocalDateSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLocalDateSet(),
                Collections.singleton(LocalDate.parse("2017-12-24")));
    }

    //////////////////////////////////////////////////////////////////////
    //////////////////////// Test LocalDateTime////////////////////////////

    @Test
    public void testLocalDateTimeArrayLookupProgrammatically() {
        LocalDateTime[] value = config.getValue("tck.config.test.javaconfig.converter.localdatetimevalues",
                LocalDateTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDateTime[]{
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")});
        LocalDateTime[] single = config.getValue("tck.config.test.javaconfig.converter.localdatetimevalue",
                LocalDateTime[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalDateTime[]{LocalDateTime.parse("2017-12-24T10:25:30")});
    }

    @Test
    public void testGetLocalDateTimeArrayConverter() {
        LocalDateTime[] value =
                config.getConverter(LocalDateTime[].class).get().convert("2017-12-24T10:25:30,2017-12-24T10:25:33");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDateTime[]{
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")});
        LocalDateTime[] single = config.getConverter(LocalDateTime[].class).get().convert("2017-12-24T10:25:30");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalDateTime[]{LocalDateTime.parse("2017-12-24T10:25:30")});
    }

    @Test
    public void testOptionalLocalDateTimeArrayLookupProgrammatically() {
        Optional<LocalDateTime[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.localdatetimevalues",
                        LocalDateTime[].class);
        Assert.assertTrue(optionalValue.isPresent());
        LocalDateTime[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDateTime[]{
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")});
        Optional<LocalDateTime[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.localdatetimevalue",
                        LocalDateTime[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        LocalDateTime[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new LocalDateTime[]{LocalDateTime.parse("2017-12-24T10:25:30")});
    }

    @Test
    public void testLocalDateTimeListLookupProgrammatically() {
        List<LocalDateTime> values = config.getValues("tck.config.test.javaconfig.converter.localdatetimevalues",
                LocalDateTime.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")));
        List<LocalDateTime> single = config.getValues("tck.config.test.javaconfig.converter.localdatetimevalue",
                LocalDateTime.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(LocalDateTime.parse("2017-12-24T10:25:30")));
    }

    @Test
    public void testOptionalLocalDateTimeListLookupProgrammatically() {
        Optional<List<LocalDateTime>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.localdatetimevalues",
                        LocalDateTime.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<LocalDateTime> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")));
        Optional<List<LocalDateTime>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.localdatetimevalue",
                        LocalDateTime.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<LocalDateTime> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(LocalDateTime.parse("2017-12-24T10:25:30")));
    }

    @Test
    public void testLocalDateTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateTimeArray().length, 2);
        Assert.assertEquals(converterBean.getMyLocalDateTimeArray(), new LocalDateTime[]{
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")});
        Assert.assertEquals(converterBean.getMySingleLocalDateTimeArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleLocalDateTimeArray(),
                new LocalDateTime[]{LocalDateTime.parse("2017-12-24T10:25:30")});
    }

    @Test
    public void testLocalDateTimeListInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateTimeList(), Arrays.asList(
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33")));
        Assert.assertEquals(converterBean.getMySingleLocalDateTimeList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLocalDateTimeList(),
                Arrays.asList(LocalDateTime.parse("2017-12-24T10:25:30")));
    }

    @Test
    public void testLocalDateTimeSetInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateTimeSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateTimeSet(), new LinkedHashSet<>(Arrays.asList(
                LocalDateTime.parse("2017-12-24T10:25:30"),
                LocalDateTime.parse("2017-12-24T10:25:33"))));
        Assert.assertEquals(converterBean.getMySingleLocalDateTimeSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleLocalDateTimeSet(),
                Collections.singleton(LocalDateTime.parse("2017-12-24T10:25:30")));
    }

    //////////////////////////////////////////////////////////////////////
    //////////////////////// Test OffsetDateTime////////////////////////////

    @Test
    public void testOffsetDateTimeArrayLookupProgrammatically() {
        OffsetDateTime[] value = config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalues",
                OffsetDateTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new OffsetDateTime[]{
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")});
        OffsetDateTime[] single = config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalue",
                OffsetDateTime[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new OffsetDateTime[]{OffsetDateTime.parse("2007-12-03T10:15:30+01:00")});
    }

    @Test
    public void testGetOffsetDateTimeArrayConverter() {
        OffsetDateTime[] value = config.getConverter(OffsetDateTime[].class).get()
                .convert("2007-12-03T10:15:30+01:00,2007-12-03T10:15:30+02:00");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new OffsetDateTime[]{
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")});
        OffsetDateTime[] single =
                config.getConverter(OffsetDateTime[].class).get().convert("2007-12-03T10:15:30+01:00");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new OffsetDateTime[]{OffsetDateTime.parse("2007-12-03T10:15:30+01:00")});
    }

    @Test
    public void testOptionalOffsetDateTimeArrayLookupProgrammatically() {
        Optional<OffsetDateTime[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.offsetdatetimevalues",
                        OffsetDateTime[].class);
        Assert.assertTrue(optionalValue.isPresent());
        OffsetDateTime[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new OffsetDateTime[]{
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")});
        Optional<OffsetDateTime[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.offsetdatetimevalue",
                        OffsetDateTime[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        OffsetDateTime[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new OffsetDateTime[]{OffsetDateTime.parse("2007-12-03T10:15:30+01:00")});
    }

    @Test
    public void testOffsetDateTimeListLookupProgrammatically() {
        List<OffsetDateTime> values = config.getValues("tck.config.test.javaconfig.converter.offsetdatetimevalues",
                OffsetDateTime.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
        List<OffsetDateTime> single = config.getValues("tck.config.test.javaconfig.converter.offsetdatetimevalue",
                OffsetDateTime.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
    }

    @Test
    public void testOptionalOffsetDateTimeListLookupProgrammatically() {
        Optional<List<OffsetDateTime>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.offsetdatetimevalues",
                        OffsetDateTime.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<OffsetDateTime> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
        Optional<List<OffsetDateTime>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.offsetdatetimevalue",
                        OffsetDateTime.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<OffsetDateTime> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
    }

    @Test
    public void testOffsetDateTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyOffsetDateTimeArray().length, 2);
        Assert.assertEquals(converterBean.getMyOffsetDateTimeArray(), new OffsetDateTime[]{
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")});
        Assert.assertEquals(converterBean.getMySingleOffsetDateTimeArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleOffsetDateTimeArray(), new OffsetDateTime[]{
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00")});
    }

    @Test
    public void testOffsetDateTimeListInjection() {
        Assert.assertEquals(converterBean.getMyOffsetDateTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyOffsetDateTimeList(), Arrays.asList(
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
        Assert.assertEquals(converterBean.getMySingleOffsetDateTimeList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleOffsetDateTimeList(),
                Arrays.asList(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
    }

    @Test
    public void testOffsetDateTimeSetInjection() {
        Assert.assertEquals(converterBean.getMyOffsetDateTimeSet().size(), 2);
        Assert.assertEquals(converterBean.getMyOffsetDateTimeSet(), new LinkedHashSet<>(Arrays.asList(
                OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
                OffsetDateTime.parse("2007-12-03T10:15:30+02:00"))));
        Assert.assertEquals(converterBean.getMySingleOffsetDateTimeSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleOffsetDateTimeSet(),
                Collections.singleton(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
    }

    //////////////////////////////////////////////////////////////////////

    //////////////////////// Test OffsetTime////////////////////////////

    @Test
    public void testOffsetTimeArrayLookupProgrammatically() {
        OffsetTime[] value = config.getValue("tck.config.test.javaconfig.converter.offsettimevalues",
                OffsetTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new OffsetTime[]{
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")});
        OffsetTime[] single = config.getValue("tck.config.test.javaconfig.converter.offsettimevalue",
                OffsetTime[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new OffsetTime[]{OffsetTime.parse("13:45:30.123456789+02:00")});
    }

    @Test
    public void testGetOffsetTimeArrayConverter() {
        OffsetTime[] value = config.getConverter(OffsetTime[].class).get()
                .convert("13:45:30.123456789+02:00,13:45:30.123456789+03:00");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new OffsetTime[]{
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")});
        OffsetTime[] single = config.getConverter(OffsetTime[].class).get().convert("13:45:30.123456789+02:00");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new OffsetTime[]{OffsetTime.parse("13:45:30.123456789+02:00")});
    }

    @Test
    public void testOptionalOffsetTimeArrayLookupProgrammatically() {
        Optional<OffsetTime[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.offsettimevalues",
                        OffsetTime[].class);
        Assert.assertTrue(optionalValue.isPresent());
        OffsetTime[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new OffsetTime[]{
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")});
        Optional<OffsetTime[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.offsettimevalue",
                        OffsetTime[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        OffsetTime[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new OffsetTime[]{OffsetTime.parse("13:45:30.123456789+02:00")});
    }

    @Test
    public void testOffsetTimeListLookupProgrammatically() {
        List<OffsetTime> values = config.getValues("tck.config.test.javaconfig.converter.offsettimevalues",
                OffsetTime.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")));
        List<OffsetTime> single = config.getValues("tck.config.test.javaconfig.converter.offsettimevalue",
                OffsetTime.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(OffsetTime.parse("13:45:30.123456789+02:00")));
    }

    @Test
    public void testOptionalOffsetTimeListLookupProgrammatically() {
        Optional<List<OffsetTime>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.offsettimevalues",
                        OffsetTime.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<OffsetTime> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")));
        Optional<List<OffsetTime>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.offsettimevalue",
                        OffsetTime.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<OffsetTime> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(OffsetTime.parse("13:45:30.123456789+02:00")));
    }

    @Test
    public void testOffsetTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyOffsetTimeArray().length, 2);
        Assert.assertEquals(converterBean.getMyOffsetTimeArray(), new OffsetTime[]{
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")});
        Assert.assertEquals(converterBean.getMySingleOffsetTimeArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleOffsetTimeArray(),
                new OffsetTime[]{OffsetTime.parse("13:45:30.123456789+02:00")});
    }

    @Test
    public void testOffsetTimeListInjection() {
        Assert.assertEquals(converterBean.getMyOffsetTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyOffsetTimeList(), Arrays.asList(
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00")));
        Assert.assertEquals(converterBean.getMySingleOffsetTimeList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleOffsetTimeList(),
                Arrays.asList(OffsetTime.parse("13:45:30.123456789+02:00")));
    }

    @Test
    public void testOffsetTimeSetInjection() {
        Assert.assertEquals(converterBean.getMyOffsetTimeSet().size(), 2);
        Assert.assertEquals(converterBean.getMyOffsetTimeSet(), new LinkedHashSet<>(Arrays.asList(
                OffsetTime.parse("13:45:30.123456789+02:00"),
                OffsetTime.parse("13:45:30.123456789+03:00"))));
        Assert.assertEquals(converterBean.getMySingleOffsetTimeSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleOffsetTimeSet(),
                Collections.singleton(OffsetTime.parse("13:45:30.123456789+02:00")));
    }

    //////////////////////////////////////////////////////////////////////
    //////////////////////// Test instant////////////////////////////

    @Test
    public void testInstantArrayLookupProgrammatically() {
        Instant[] value = config.getValue("tck.config.test.javaconfig.converter.instantvalues",
                Instant[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Instant[]{
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")});
        Instant[] single = config.getValue("tck.config.test.javaconfig.converter.instantvalue",
                Instant[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Instant[]{Instant.parse("2015-06-02T21:34:33.616Z")});
    }

    @Test
    public void testGetInstantArrayConverter() {
        Instant[] value =
                config.getConverter(Instant[].class).get().convert("2015-06-02T21:34:33.616Z,2017-06-02T21:34:33.616Z");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Instant[]{
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")});
        Instant[] single = config.getConverter(Instant[].class).get().convert("2015-06-02T21:34:33.616Z");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Instant[]{Instant.parse("2015-06-02T21:34:33.616Z")});
    }

    @Test
    public void testOptionalInstantArrayLookupProgrammatically() {
        Optional<Instant[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.instantvalues",
                        Instant[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Instant[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Instant[]{
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")});
        Optional<Instant[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.instantvalue",
                        Instant[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Instant[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Instant[]{Instant.parse("2015-06-02T21:34:33.616Z")});
    }

    @Test
    public void testInstantListLookupProgrammatically() {
        List<Instant> values = config.getValues("tck.config.test.javaconfig.converter.instantvalues",
                Instant.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")));
        List<Instant> single = config.getValues("tck.config.test.javaconfig.converter.instantvalue",
                Instant.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(Instant.parse("2015-06-02T21:34:33.616Z")));
    }

    @Test
    public void testOptionalInstantListLookupProgrammatically() {
        Optional<List<Instant>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.instantvalues",
                        Instant.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Instant> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values, Arrays.asList(
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")));
        Optional<List<Instant>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.instantvalue",
                        Instant.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Instant> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(Instant.parse("2015-06-02T21:34:33.616Z")));
    }

    @Test
    public void testInstantArrayInjection() {
        Assert.assertEquals(converterBean.getMyInstantArray().length, 2);
        Assert.assertEquals(converterBean.getMyInstantArray(), new Instant[]{
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")});
        Assert.assertEquals(converterBean.getMySingleInstantArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleInstantArray(),
                new Instant[]{Instant.parse("2015-06-02T21:34:33.616Z")});
    }

    @Test
    public void testInstantListInjection() {
        Assert.assertEquals(converterBean.getMyInstantList().size(), 2);
        Assert.assertEquals(converterBean.getMyInstantList(), Arrays.asList(
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z")));
        Assert.assertEquals(converterBean.getMySingleInstantList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleInstantList(),
                Arrays.asList(Instant.parse("2015-06-02T21:34:33.616Z")));
    }

    @Test
    public void testInstantSetInjection() {
        Assert.assertEquals(converterBean.getMyInstantSet().size(), 2);
        Assert.assertEquals(converterBean.getMyInstantList(), new LinkedHashSet<>(Arrays.asList(
                Instant.parse("2015-06-02T21:34:33.616Z"),
                Instant.parse("2017-06-02T21:34:33.616Z"))));
        Assert.assertEquals(converterBean.getMySingleInstantSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleInstantSet(),
                Collections.singleton(Instant.parse("2015-06-02T21:34:33.616Z")));
    }

    //////////////////////////////////////////////////////////////////////

    /////////////////////////////////// Test URL[] //////////////////////////

    private void assertURLArrayEquals(URL[] value, URL[] expectedValue) throws MalformedURLException {
        assertURLListEquals(Arrays.asList(value), Arrays.asList(expectedValue));
    }

    private void assertURLListEquals(List<URL> value, List<URL> expectedValue) throws MalformedURLException {

        Assert.assertTrue(IntStream.range(0, expectedValue.size()).allMatch(i -> {
            try {
                return expectedValue.get(i).toURI().equals(value.get(i).toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return false;
        }));
    }

    @Test
    public void testUrlArrayLookupProgrammatically() throws MalformedURLException, URISyntaxException {
        URL[] value = config.getValue("tck.config.test.javaconfig.converter.urlvalues", URL[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        URL[] expectedValue = new URL[]{
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io")};
        assertURLArrayEquals(value, expectedValue);
        URL[] single = config.getValue("tck.config.test.javaconfig.converter.urlvalue", URL[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single[0].toURI(), new URL[]{new URL("http://microprofile.io")}[0].toURI());
    }

    @Test
    public void testGetUrlArrayConverter() throws MalformedURLException, URISyntaxException {
        URL[] value = config.getConverter(URL[].class).get()
                .convert("http://microprofile.io,http://openliberty.io,http://microprofile.io");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        URL[] expectedValue = new URL[]{
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io")};
        assertURLArrayEquals(value, expectedValue);
        URL[] single = config.getConverter(URL[].class).get().convert("http://microprofile.io");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        URL[] expectedSingleValue = new URL[]{
                new URL("http://microprofile.io")};
        assertURLArrayEquals(single, expectedSingleValue);
    }

    @Test
    public void testOptionalUrlArrayLookupProgrammatically() throws MalformedURLException, URISyntaxException {
        Optional<URL[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.urlvalues", URL[].class);
        Assert.assertTrue(optionalValue.isPresent());
        URL[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        URL[] expectedValue = new URL[]{
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io")};
        assertURLArrayEquals(value, expectedValue);
        Optional<URL[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.urlvalue", URL[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        URL[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        URL[] expectedSingleValue = new URL[]{
                new URL("http://microprofile.io")};
        assertURLArrayEquals(single, expectedSingleValue);
    }

    @Test
    public void testUrlListLookupProgrammatically() throws MalformedURLException, URISyntaxException {
        List<URL> values = config.getValues("tck.config.test.javaconfig.converter.urlvalues", URL.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        List<URL> expectedValue = Arrays.asList(
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io"));
        assertURLListEquals(values, expectedValue);
        List<URL> single = config.getValues("tck.config.test.javaconfig.converter.urlvalue", URL.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        List<URL> expectedSingleValue = Arrays.asList(new URL("http://microprofile.io"));
        Assert.assertEquals(single, expectedSingleValue);
    }

    @Test
    public void testOptionalUrlListLookupProgrammatically() throws MalformedURLException, URISyntaxException {
        Optional<List<URL>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.urlvalues", URL.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<URL> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        List<URL> expectedValue = Arrays.asList(
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io"));
        assertURLListEquals(values, expectedValue);
        Optional<List<URL>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.urlvalue", URL.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<URL> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        List<URL> expectedSingleValue = Arrays.asList(new URL("http://microprofile.io"));
        Assert.assertEquals(single, expectedSingleValue);
    }

    @Test
    public void testUrlArrayInjection() throws MalformedURLException, URISyntaxException {
        URL[] values = converterBean.getMyUrlArray();
        Assert.assertEquals(values.length, 3);
        URL[] expectedValue = new URL[]{
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io")};
        assertURLArrayEquals(values, expectedValue);
        Assert.assertEquals(converterBean.getMySingleUrlArray().length, 1);
        URL[] expectedSingleValue = new URL[]{
                new URL("http://microprofile.io")};
        assertURLArrayEquals(converterBean.getMySingleUrlArray(), expectedSingleValue);
    }

    @Test
    public void testURLListInjection() throws MalformedURLException, URISyntaxException {
        List<URL> values = converterBean.getMyUrlList();
        Assert.assertEquals(values.size(), 3);
        List<URL> expectedValue = Arrays.asList(
                new URL("http://microprofile.io"),
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io"));
        assertURLListEquals(values, expectedValue);
        Assert.assertEquals(converterBean.getMySingleUrlList().size(), 1);
        List<URL> expectedSingleValue = Arrays.asList(new URL("http://microprofile.io"));
        Assert.assertEquals(converterBean.getMySingleUrlList(), expectedSingleValue);
    }

    private boolean assertURLSetEquals(Set<URL> valueSet, Set<URL> expectedURLSet)
            throws MalformedURLException, URISyntaxException {
        if (valueSet.size() != expectedURLSet.size()) {
            return false;
        }
        Iterator<URL> it = valueSet.iterator();
        boolean isEquals = true;
        while (it.hasNext()) {
            boolean found = false;
            URL url = it.next();
            for (URL thisURL : expectedURLSet) {
                if (thisURL.toURI().equals(url.toURI())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                isEquals = false;
                break;
            }
        }
        return isEquals;

    }
    @Test
    public void testURLSetInjection() throws MalformedURLException, URISyntaxException {
        Set<URL> values = converterBean.getMyUrlSet();
        Assert.assertEquals(values.size(), 2);
        Set<URL> expectedURLSet = new LinkedHashSet<>(Arrays.asList(
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io")));
        Assert.assertTrue(assertURLSetEquals(values, expectedURLSet));

        Assert.assertEquals(converterBean.getMySingleUrlSet().size(), 1);
        Set<URL> expectedSingleUrlSet = Collections.singleton(new URL("http://microprofile.io"));
        Assert.assertTrue(assertURLSetEquals(converterBean.getMySingleUrlSet(), expectedSingleUrlSet));
    }

    /////////////////////////////////// Test URI[] //////////////////////////

    @Test
    public void testUriArrayLookupProgrammatically() {
        URI[] value = config.getValue("tck.config.test.javaconfig.converter.urlvalues", URI[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new URI[]{
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")});
        URI[] single = config.getValue("tck.config.test.javaconfig.converter.urlvalue", URI[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new URI[]{URI.create("http://microprofile.io")});
    }

    @Test
    public void testGetUriArrayConverter() {
        URI[] value = config.getConverter(URI[].class).get()
                .convert("http://microprofile.io,http://openliberty.io,http://microprofile.io");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new URI[]{
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")});
        URI[] single = config.getConverter(URI[].class).get().convert("http://microprofile.io");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new URI[]{URI.create("http://microprofile.io")});
    }

    @Test
    public void testOptionalUriArrayLookupProgrammatically() {
        Optional<URI[]> optionalValue =
                config.getOptionalValue("tck.config.test.javaconfig.converter.urlvalues", URI[].class);
        Assert.assertTrue(optionalValue.isPresent());
        URI[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new URI[]{
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")});
        Optional<URI[]> optionalSingle =
                config.getOptionalValue("tck.config.test.javaconfig.converter.urlvalue", URI[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        URI[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new URI[]{URI.create("http://microprofile.io")});
    }

    @Test
    public void testUriListLookupProgrammatically() {
        List<URI> values = config.getValues("tck.config.test.javaconfig.converter.urlvalues", URI.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values, Arrays.asList(
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")));
        List<URI> single = config.getValues("tck.config.test.javaconfig.converter.urlvalue", URI.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(URI.create("http://microprofile.io")));
    }

    @Test
    public void testOptionalUriListLookupProgrammatically() {
        Optional<List<URI>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.urlvalues", URI.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<URI> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values, Arrays.asList(
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")));
        Optional<List<URI>> optionalSingle =
                config.getOptionalValues("tck.config.test.javaconfig.converter.urlvalue", URI.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<URI> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(URI.create("http://microprofile.io")));
    }

    @Test
    public void testUriArrayInjection() {
        Assert.assertEquals(converterBean.getMyUriArray().length, 3);
        Assert.assertEquals(converterBean.getMyUriArray(), new URI[]{
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")});
        Assert.assertEquals(converterBean.getMySingleUriArray().length, 1);
        Assert.assertEquals(converterBean.getMySingleUriArray(), new URI[]{URI.create("http://microprofile.io")});
    }

    @Test
    public void testUriListInjection() {
        Assert.assertEquals(converterBean.getMyUriList().size(), 3);
        Assert.assertEquals(converterBean.getMyUriList(), Arrays.asList(
                URI.create("http://microprofile.io"),
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io")));
        Assert.assertEquals(converterBean.getMySingleUriList().size(), 1);
        Assert.assertEquals(converterBean.getMySingleUriList(), Arrays.asList(URI.create("http://microprofile.io")));
    }

    @Test
    public void testUriSetInjection() {
        Assert.assertEquals(converterBean.getMyUriSet().size(), 2);
        Assert.assertEquals(converterBean.getMyUriSet(), new LinkedHashSet<>(Arrays.asList(
                URI.create("http://openliberty.io"),
                URI.create("http://microprofile.io"))));
        Assert.assertEquals(converterBean.getMySingleUriSet().size(), 1);
        Assert.assertEquals(converterBean.getMySingleUriSet(),
                Collections.singleton(URI.create("http://microprofile.io")));
    }

    ///////////////////////////////////////////////////////////////////
    // test custom class array support

    @Test
    public void testCustomTypeArrayLookupProgrammatically() {
        Pizza[] value = config.getValue("tck.config.test.javaconfig.converter.array.pizza",
                Pizza[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Pizza[]{
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")});
        Pizza[] single = config.getValue("tck.config.test.javaconfig.converter.pizza",
                Pizza[].class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Pizza[]{new Pizza("cheese,mushroom", "large")});
    }

    @Test
    public void testGetCustomTypeArrayConverter() {
        Pizza[] value = config.getConverter(Pizza[].class).get()
                .convert("large:cheese\\,mushroom,medium:chicken,small:pepperoni");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Pizza[]{
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")});
        Pizza[] single = config.getConverter(Pizza[].class).get().convert("large:cheese\\,mushroom");
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Pizza[]{new Pizza("cheese,mushroom", "large")});
    }

    @Test
    public void testOptionalCustomTypeArrayLookupProgrammatically() {
        Optional<Pizza[]> optionalValue = config.getOptionalValue("tck.config.test.javaconfig.converter.array.pizza",
                Pizza[].class);
        Assert.assertTrue(optionalValue.isPresent());
        Pizza[] value = optionalValue.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Pizza[]{
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")});
        Optional<Pizza[]> optionalSingle = config.getOptionalValue("tck.config.test.javaconfig.converter.pizza",
                Pizza[].class);
        Assert.assertTrue(optionalSingle.isPresent());
        Pizza[] single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.length, 1);
        Assert.assertEquals(single, new Pizza[]{new Pizza("cheese,mushroom", "large")});
    }

    @Test
    public void testCustomTypeListLookupProgrammatically() {
        List<Pizza> values = config.getValues("tck.config.test.javaconfig.converter.array.pizza",
                Pizza.class);
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values, Arrays.asList(
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")));
        List<Pizza> single = config.getValues("tck.config.test.javaconfig.converter.pizza",
                Pizza.class);
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(new Pizza("cheese,mushroom", "large")));
    }

    @Test
    public void testOptionalCustomTypeListLookupProgrammatically() {
        Optional<List<Pizza>> optionalValues =
                config.getOptionalValues("tck.config.test.javaconfig.converter.array.pizza",
                        Pizza.class);
        Assert.assertTrue(optionalValues.isPresent());
        List<Pizza> values = optionalValues.get();
        Assert.assertNotNull(values);
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values, Arrays.asList(
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")));
        Optional<List<Pizza>> optionalSingle = config.getOptionalValues("tck.config.test.javaconfig.converter.pizza",
                Pizza.class);
        Assert.assertTrue(optionalSingle.isPresent());
        List<Pizza> single = optionalSingle.get();
        Assert.assertNotNull(single);
        Assert.assertEquals(single.size(), 1);
        Assert.assertEquals(single, Arrays.asList(new Pizza("cheese,mushroom", "large")));
    }

    @Test
    public void testCustomTypeArrayInjection() {
        Assert.assertEquals(converterBean.getMyPizzaArray().length, 3);
        Assert.assertEquals(converterBean.getMyPizzaArray(), new Pizza[]{
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")});
        Assert.assertEquals(converterBean.getMySinglePizzaArray().length, 1);
        Assert.assertEquals(converterBean.getMySinglePizzaArray(), new Pizza[]{new Pizza("cheese,mushroom", "large")});
    }

    @Test
    public void testCustomTypeListInjection() {
        Assert.assertEquals(converterBean.getMyPizzaList().size(), 3);
        Assert.assertEquals(converterBean.getMyPizzaList(), Arrays.asList(
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small")));
        Assert.assertEquals(converterBean.getMySinglePizzaList().size(), 1);
        Assert.assertEquals(converterBean.getMySinglePizzaList(), Arrays.asList(new Pizza("cheese,mushroom", "large")));
    }

    @Test
    public void testCustomTypeSetInjection() {
        Assert.assertEquals(converterBean.getMyPizzaSet().size(), 3);
        Assert.assertEquals(converterBean.getMyPizzaSet(), new LinkedHashSet<>(Arrays.asList(
                new Pizza("cheese,mushroom", "large"),
                new Pizza("chicken", "medium"),
                new Pizza("pepperoni", "small"))));
        Assert.assertEquals(converterBean.getMySinglePizzaSet().size(), 1);
        Assert.assertEquals(converterBean.getMySinglePizzaSet(),
                Collections.singleton(new Pizza("cheese,mushroom", "large")));
    }

}
