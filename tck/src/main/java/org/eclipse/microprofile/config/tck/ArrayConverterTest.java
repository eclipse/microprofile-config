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
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.LinkedHashSet;

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

    ///////////////////////////////////Test Boolean[] boolean[]//////////////////////////

    @Test
    public void testBooleanLookupProgrammatically() {
        Boolean[] value = config.getValue("tck.config.test.javaconfig.converter.booleanvalues",
            Boolean[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new Boolean[]{true, false, true});
    }

    @Test
    public void testBooleanArrayInjection() {
        Assert.assertEquals(converterBean.getMyBooleans().length, 3);
        Assert.assertEquals(converterBean.getMyBooleans(), new Boolean[]{true, false, true});
    }

    //test bool[] support

    @Test
    public void testbooleanArrayInjection() {
        Assert.assertEquals(converterBean.getMybooleans().length, 3);
        Assert.assertEquals(converterBean.getMybooleans(), new boolean[]{true, false, true});
    }

    @Test
    public void testbooleanListInjection() {
        Assert.assertEquals(converterBean.getMyBooleanList().size(), 3);
        Assert.assertEquals(converterBean.getMyBooleanList(), Arrays.asList(true, false, true));
    }

    @Test
    public void testbooleanSetInjection() {
        Assert.assertEquals(converterBean.getMyBooleanSet().size(), 2);
        Assert.assertEquals(converterBean.getMyBooleanSet(), new LinkedHashSet<>(Arrays.asList(true, false, true)));
    }

    ///////////////////////////////////Test String[] //////////////////////////

    @Test
    public void testStringLookupProgrammatically() {
        String[] value = config.getValue("tck.config.test.javaconfig.converter.stringvalues",
            String[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 4);
        Assert.assertEquals(value, new String[]{"microservice", "microprofile", "m,f", "microservice"});
    }

    @Test
    public void testStringArrayInjection() {
        Assert.assertEquals(converterBean.getMyStrings().length, 4);
        Assert.assertEquals(converterBean.getMyStrings(), new String[]{"microservice", "microprofile", "m,f", "microservice"});
    }

    @Test
    public void testStringListInjection() {
        Assert.assertEquals(converterBean.getMyStringList().size(), 4);
        Assert.assertEquals(converterBean.getMyStringList(), Arrays.asList("microservice", "microprofile", "m,f", "microservice"));
    }

    @Test
    public void testStringSetInjection() {
        Assert.assertEquals(converterBean.getMyStringSet().size(), 3);
        Assert.assertEquals(converterBean.getMyStringSet(),
            new LinkedHashSet<>(Arrays.asList("microservice", "microprofile", "m,f", "microservice")));
    }

    ///////////////////////////////////////////////////////////////////
    //test Integer[] support
    ///////////////////////////////////Test Integer[] int[]//////////////////////////

    @Test
    public void testIntLookupProgrammatically() {
        Integer[] value = config.getValue("tck.config.test.javaconfig.converter.integervalues",
            Integer[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Integer[]{1234, 9999});
    }

    @Test
    public void testIntArrayInjection() {
        Assert.assertEquals(converterBean.getMyInts().length, 2);
        Assert.assertEquals(converterBean.getMyInts(), new Integer[]{1234, 9999});
    }

    //test int[] support

    @Test
    public void testintArrayInjection() {
        Assert.assertEquals(converterBean.getMyints().length, 2);
        Assert.assertEquals(converterBean.getMyInts(), new int[]{1234, 9999});
    }

    @Test
    public void testIntListInjection() {
        Assert.assertEquals(converterBean.getMyIntList().size(), 2);
        Assert.assertEquals(converterBean.getMyIntList(), Arrays.asList(1234, 9999));
    }

    @Test
    public void testIntSetInjection() {
        Assert.assertEquals(converterBean.getMyIntSet().size(), 2);
        Assert.assertEquals(converterBean.getMyIntSet(), new LinkedHashSet<>(Arrays.asList(1234, 9999)));
    }

    ///////////////////////////////////////////////////////////////////
    //////////////////Test Long[] long[]///////////////////////////////
    //test Long[] support

    @Test
    public void testLongLookupProgrammatically() {
        Long[] value = config.getValue("tck.config.test.javaconfig.converter.longvalues",
            Long[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Long[] {1234567890L, 1999999999L});
    }

    @Test
    public void testLongArrayInjection() {
        Assert.assertEquals(converterBean.getMyLongs().length, 2);
        Assert.assertEquals(converterBean.getMyLongs(), new Long[] {1234567890L, 1999999999L});
    }

    //test long[] support

    @Test
    public void testlongArrayInjection() {
        Assert.assertEquals(converterBean.getMylongs().length, 2);
        Assert.assertEquals(converterBean.getMylongs(), new long[] {1234567890L, 1999999999L});
    }

    @Test
    public void testLongListInjection() {
        Assert.assertEquals(converterBean.getMyLongList().size(), 2);
        Assert.assertEquals(converterBean.getMyLongList(), Arrays.asList(1234567890L, 1999999999L));
    }

    @Test
    public void testLongSetInjection() {
        Assert.assertEquals(converterBean.getMyLongSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLongSet(), new LinkedHashSet<>(Arrays.asList(1234567890L, 1999999999L)));
    }

   ///////////////////////////////////Test Float[] float[]/////////////////////

    //test Float[] support

    @Test
    public void testFloatLookupProgrammatically() {
        Float[] value = config.getValue("tck.config.test.javaconfig.converter.floatvalues",
            Float[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new Float[]{12.34f, 99.99f});
    }

    @Test
    public void testFloatArrayInjection() {
        Assert.assertEquals(converterBean.getMyFloats().length, 2);
        Assert.assertEquals(converterBean.getMyFloats(), new Float[]{12.34f, 99.99f});
    }

    //test float[] support

    @Test
    public void testfloatArrayInjection() {
        Assert.assertEquals(converterBean.getMyfloats().length, 2);
        Assert.assertEquals(converterBean.getMyfloats(), new float[]{12.34f, 99.99f});
    }

    @Test
    public void testFloatListInjection() {
        Assert.assertEquals(converterBean.getMyFloatList().size(), 2);
        Assert.assertEquals(converterBean.getMyFloatList(), Arrays.asList(12.34f, 99.99f));
    }

    @Test
    public void testFloatSetInjection() {
        Assert.assertEquals(converterBean.getMyFloatSet().size(), 2);
        Assert.assertEquals(converterBean.getMyFloatSet(), new LinkedHashSet<>(Arrays.asList(12.34f, 99.99f)));
    }

    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Test Double[] double[]/////////////

    @Test
    public void testDoubleLookupProgrammatically() {
        Double[] value = config.getValue("tck.config.test.javaconfig.converter.doublevalues",
            Double[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value, new Double[]{12.34d,99.9999d});
    }

    @Test
    public void testDoubleArrayInjection() {
        Assert.assertEquals(converterBean.getMyDoubles().length, 2);
        Assert.assertEquals( converterBean.getMyDoubles(), new Double[]{12.34d,99.9999d});
    }

    //test double[] support

    @Test
    public void testdoubleArrayInjection() {
        Assert.assertEquals(converterBean.getMydoubles().length, 2);
        Assert.assertEquals( converterBean.getMydoubles(), new double[]{12.34d,99.9999d});
    }

        @Test
    public void testDoubleListInjection() {
        Assert.assertEquals(converterBean.getMyDoubleList().size(), 2);
        Assert.assertEquals(converterBean.getMyDoubleList(), Arrays.asList(12.34d,99.9999d));
    }

    @Test
    public void testDoubleSetInjection() {
        Assert.assertEquals(converterBean.getMyDoubleSet().size(), 2);
        Assert.assertEquals(converterBean.getMyDoubleSet(), new LinkedHashSet<>(Arrays.asList(12.34d,99.9999d)));
    }

    //////////////////////////////////////////////////////////////////////
    // test duration
    ///////////////////////////////////Test Duration/////////////////////

    @Test
    public void testDurationLookupProgrammatically() {
        Duration[] value = config.getValue("tck.config.test.javaconfig.converter.durationvalues",
            Duration[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value, new Duration[]{
            Duration.parse("PT15M"),
            Duration.parse("PT20M")});
    }

    @Test
    public void testDurationArrayInjection() {
        Assert.assertEquals(converterBean.getMyDurations().length, 2);
        Assert.assertEquals(converterBean.getMyDurations(), new Duration[]{
            Duration.parse("PT15M"),
            Duration.parse("PT20M")});
    }

    @Test
    public void testDurationListInjection() {
        Assert.assertEquals(converterBean.getMyDurationList().size(), 2);
        Assert.assertEquals(converterBean.getMyDurationList(), Arrays.asList(
            Duration.parse("PT15M"),
            Duration.parse("PT20M")));
    }

    @Test
    public void testDurationSetInjection() {
        Assert.assertEquals(converterBean.getMyDurationSet().size(), 2);
        Assert.assertEquals(converterBean.getMyDurationList(), new LinkedHashSet<>(Arrays.asList(
            Duration.parse("PT15M"),
            Duration.parse("PT20M"))));
    }

    //////////////////////////////////////////////////////////////////////
    //LocalTime
    ///////////////////////////////////Test LocalTime/////////////////////

    @Test
    public void testLocalTimeLookupProgrammatically() {
        LocalTime[] value = config.getValue("tck.config.test.javaconfig.converter.localtimevalues",
            LocalTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value, new LocalTime[]{
            LocalTime.parse("10:37"),
            LocalTime.parse("11:44")});
    }

    @Test
    public void testLocalTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyLocaltimes().length, 2);
        Assert.assertEquals(converterBean.getMyLocaltimes(), new LocalTime[]{
            LocalTime.parse("10:37"),
            LocalTime.parse("11:44")});
    }

    @Test
    public void testLocalTimeListInjection() {
        Assert.assertEquals(converterBean.getMyLocalTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalTimeList(), Arrays.asList(
            LocalTime.parse("10:37"),
            LocalTime.parse("11:44")));
    }

    @Test
    public void testLocalTimeSetInjection() {
        Assert.assertEquals(converterBean.getMyLocalTimeSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalTimeSet(), new LinkedHashSet<>(Arrays.asList(
            LocalTime.parse("10:37"),
            LocalTime.parse("11:44"))));
    }

    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Test LocalDate////////////////////////////

    @Test
    public void testLocalDateLookupProgrammatically() {
        LocalDate[] value = config.getValue("tck.config.test.javaconfig.converter.localdatevalues",
            LocalDate[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value, new LocalDate[]{
            LocalDate.parse("2017-12-24"),
            LocalDate.parse("2017-11-29")});
    }

    @Test
    public void testLocalDateArrayInjection() {
        Assert.assertEquals(converterBean.getMyDates().length, 2);
        Assert.assertEquals(converterBean.getMyDates(), new LocalDate[]{
            LocalDate.parse("2017-12-24"),
            LocalDate.parse("2017-11-29")});
    }

    @Test
    public void testLocalDateListInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateList().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateList(), Arrays.asList(
            LocalDate.parse("2017-12-24"),
            LocalDate.parse("2017-11-29")));
    }

    @Test
    public void testLocalDateSetInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateSet().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateSet(), new LinkedHashSet<>(Arrays.asList(
            LocalDate.parse("2017-12-24"),
            LocalDate.parse("2017-11-29"))));
    }

    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test LocalDateTime////////////////////////////

    @Test
    public void testLocalDateTimeLookupProgrammatically() {
        LocalDateTime[] value = config.getValue("tck.config.test.javaconfig.converter.localdatetimevalues",
             LocalDateTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value, new LocalDateTime[]{
            LocalDateTime.parse("2017-12-24T10:25:30"),
            LocalDateTime.parse("2017-12-24T10:25:33")});
    }

    @Test
    public void testLocalDateTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateTimes().length, 2);
        Assert.assertEquals(converterBean.getMyLocalDateTimes(), new LocalDateTime[]{
            LocalDateTime.parse("2017-12-24T10:25:30"),
            LocalDateTime.parse("2017-12-24T10:25:33")});
    }

    @Test
    public void testLocalDateTimeListInjection() {
        Assert.assertEquals(converterBean.getMyLocalDateTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyLocalDateTimeList(), Arrays.asList(
            LocalDateTime.parse("2017-12-24T10:25:30"),
            LocalDateTime.parse("2017-12-24T10:25:33")));
    }

    @Test
    public void testLocalDateTimeSetInjection() {
         Assert.assertEquals(converterBean.getMyLocalDateTimeSet().size(), 2);
         Assert.assertEquals(converterBean.getMyLocalDateTimeList(), new LinkedHashSet<>(Arrays.asList(
             LocalDateTime.parse("2017-12-24T10:25:30"),
             LocalDateTime.parse("2017-12-24T10:25:33"))));
    }

    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test OffsetDateTime////////////////////////////

    @Test
    public void testOffsetDateTimeLookupProgrammatically() {
        OffsetDateTime[] value = config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalues",
                OffsetDateTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value, new OffsetDateTime[] {
            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
            OffsetDateTime.parse("2007-12-03T10:15:30+02:00")});
    }

    @Test
    public void testOffsetDateTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyOffsetDateTimes().length, 2);
        Assert.assertEquals( converterBean.getMyOffsetDateTimes(), new OffsetDateTime[] {
            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
            OffsetDateTime.parse("2007-12-03T10:15:30+02:00")});
    }

    @Test
    public void testOffsetDateTimeListInjection() {
        Assert.assertEquals(converterBean.getMyOffsetDateTimeList().size(), 2);
        Assert.assertEquals( converterBean.getMyOffsetDateTimeList(), Arrays.asList(
            OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
            OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
    }

    @Test
    public void testOffsetDateTimeSetInjection() {
         Assert.assertEquals(converterBean.getMyOffsetDateTimeSet().size(), 2);
         Assert.assertEquals( converterBean.getMyOffsetDateTimeSet(), new LinkedHashSet<>(Arrays.asList(
             OffsetDateTime.parse("2007-12-03T10:15:30+01:00"),
             OffsetDateTime.parse("2007-12-03T10:15:30+02:00"))));
    }

    //////////////////////////////////////////////////////////////////////

    ////////////////////////Test OffsetTime////////////////////////////

    @Test
    public void testOffsetTimeLookupProgrammatically() {
        OffsetTime[] value = config.getValue("tck.config.test.javaconfig.converter.offsettimevalues",
              OffsetTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value, new OffsetTime[]{
            OffsetTime.parse("13:45:30.123456789+02:00"),
            OffsetTime.parse("13:45:30.123456789+03:00")});
    }

    @Test
    public void testOffsetTimeArrayInjection() {
        Assert.assertEquals(converterBean.getMyOffsetTimes().length, 2);
        Assert.assertEquals(converterBean.getMyOffsetTimes(), new OffsetTime[]{
            OffsetTime.parse("13:45:30.123456789+02:00"),
            OffsetTime.parse("13:45:30.123456789+03:00")});
    }

    @Test
    public void testOffsetTimeListInjection() {
        Assert.assertEquals(converterBean.getMyOffsetTimeList().size(), 2);
        Assert.assertEquals(converterBean.getMyOffsetTimeList(), Arrays.asList(
            OffsetTime.parse("13:45:30.123456789+02:00"),
            OffsetTime.parse("13:45:30.123456789+03:00")));
    }

    @Test
    public void testOffsetTimeSetInjection() {
         Assert.assertEquals(converterBean.getMyOffsetTimeSet().size(), 2);
         Assert.assertEquals(converterBean.getMyOffsetTimeList(), new LinkedHashSet<>(Arrays.asList(
             OffsetTime.parse("13:45:30.123456789+02:00"),
             OffsetTime.parse("13:45:30.123456789+03:00"))));
    }

    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test instant////////////////////////////


    @Test
    public void testInstantLookupProgrammatically() {
        Instant[] value = config.getValue("tck.config.test.javaconfig.converter.instantvalues",
               Instant[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals(value,  new Instant[]{
            Instant.parse("2015-06-02T21:34:33.616Z"),
            Instant.parse("2017-06-02T21:34:33.616Z")});
    }

    @Test
    public void testInstantArrayInjection() {
        Assert.assertEquals(converterBean.getMyInstants().length, 2);
        Assert.assertEquals(converterBean.getMyInstants(),  new Instant[]{
            Instant.parse("2015-06-02T21:34:33.616Z"),
            Instant.parse("2017-06-02T21:34:33.616Z")});
    }

    @Test
    public void testInstantListInjection() {
        Assert.assertEquals(converterBean.getMyInstantList().size(), 2);
        Assert.assertEquals(converterBean.getMyInstantList(), Arrays.asList(
            Instant.parse("2015-06-02T21:34:33.616Z"),
            Instant.parse("2017-06-02T21:34:33.616Z")));
    }

    @Test
    public void testInstantSetInjection() {
         Assert.assertEquals(converterBean.getMyInstantSet().size(), 2);
         Assert.assertEquals(converterBean.getMyInstantList(), new LinkedHashSet<>(Arrays.asList(
             Instant.parse("2015-06-02T21:34:33.616Z"),
             Instant.parse("2017-06-02T21:34:33.616Z"))));
    }

    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Test URL[] //////////////////////////

    @Test
    public void testUrlLookupProgrammatically() throws MalformedURLException {
        URL[] value = config.getValue("tck.config.test.javaconfig.converter.urlvalues", URL[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new URL[]{
            new URL("http://microprofile.io"),
            new URL("http://openliberty.io"),
            new URL("http://microprofile.io")});
    }

    @Test
    public void testUrlArrayInjection() throws MalformedURLException {
        Assert.assertEquals(converterBean.getMyUrls().length, 3);
        Assert.assertEquals(converterBean.getMyUrls(), new URL[]{
            new URL("http://microprofile.io"),
            new URL("http://openliberty.io"),
            new URL("http://microprofile.io")});
    }

    @Test
    public void testURLListInjection() throws MalformedURLException {
        Assert.assertEquals(converterBean.getMyUrlList().size(), 3);
        Assert.assertEquals(converterBean.getMyUrlList(), Arrays.asList(
            new URL("http://microprofile.io"),
            new URL("http://openliberty.io"),
            new URL("http://microprofile.io")));
    }

    @Test
    public void testURLSetInjection() throws MalformedURLException {
        Assert.assertEquals(converterBean.getMyUrlSet().size(), 2);
        Assert.assertEquals(converterBean.getMyUrlSet(), new LinkedHashSet<>(Arrays.asList(
                new URL("http://openliberty.io"),
                new URL("http://microprofile.io"))));
    }

    ///////////////////////////////////Test URI[] //////////////////////////

    @Test
    public void testUriLookupProgrammatically() {
        URI[] value = config.getValue("tck.config.test.javaconfig.converter.urlvalues", URI[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals(value, new URI[]{
            URI.create("http://microprofile.io"),
            URI.create("http://openliberty.io"),
            URI.create("http://microprofile.io")});
    }

    @Test
    public void testUriArrayInjection() {
        Assert.assertEquals(converterBean.getMyUris().length, 3);
        Assert.assertEquals(converterBean.getMyUris(), new URI[]{
            URI.create("http://microprofile.io"),
            URI.create("http://openliberty.io"),
            URI.create("http://microprofile.io")});
    }

    @Test
    public void testURIListInjection() {
        Assert.assertEquals(converterBean.getMyUriList().size(), 3);
        Assert.assertEquals(converterBean.getMyUriList(), Arrays.asList(
            URI.create("http://microprofile.io"),
            URI.create("http://openliberty.io"),
            URI.create("http://microprofile.io")));
    }

    @Test
    public void testURISetInjection() {
        Assert.assertEquals(converterBean.getMyUriSet().size(), 2);
        Assert.assertEquals(converterBean.getMyUriSet(), new LinkedHashSet<>(Arrays.asList(
            URI.create("http://openliberty.io"),
            URI.create("http://microprofile.io"))));
    }


    ///////////////////////////////////////////////////////////////////
    //test custom class array support


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
    }

    @Test
    public void testCustomTypeArrayInjection() {
        Assert.assertEquals(converterBean.getPizzas().length, 3);
        Assert.assertEquals(converterBean.getPizzas(), new Pizza[]{
            new Pizza("cheese,mushroom", "large"),
            new Pizza("chicken", "medium"),
            new Pizza("pepperoni", "small")});
    }

    @Test
    public void testCustomTypeListInjection()  {
        Assert.assertEquals(converterBean.getPizzaList().size(), 3);
        Assert.assertEquals(converterBean.getPizzaList(), Arrays.asList(
            new Pizza("cheese,mushroom", "large"),
            new Pizza("chicken", "medium"),
            new Pizza("pepperoni", "small")));
    }

    @Test
    public void testCustomTypeSetInjection()  {
        Assert.assertEquals(converterBean.getPizzaSet().size(), 3);
        Assert.assertEquals(converterBean.getPizzaSet(), new LinkedHashSet<>(Arrays.asList(
            new Pizza("cheese,mushroom", "large"),
            new Pizza("chicken", "medium"),
            new Pizza("pepperoni", "small"))));
    }

}
