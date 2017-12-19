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

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
 * @author <a href="mailto:emijiang6@googlemail.com">Mark Struberg</a>
 */
public class ArrayConverterTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "arrayConverterTest.jar")
                .addPackage(PizzaConverter.class.getPackage())
                .addClasses(ArrayConverterTest.class, ArrayBean.class)
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
    private ArrayBean arrayBean;

    @Dependent
    public static class ArrayBean {
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues")
        private Boolean[] myBooleans;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues")
        private boolean[] mybooleans;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues")
        private List<Boolean> myBooleanList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues")
        private Set<Boolean> myBooleanSet;

        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.stringvalues")
        private String[] myStrings;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.stringvalues")
        private List<String> myStringList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.stringvalues")
        private Set<String> myStringSet;

        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues")
        private Integer[] myInts;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues")
        private int[] myints;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues")
        private List<Integer> myIntList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues")
        private Set<Integer> myIntSet;

        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues")
        private Long[] myLongs;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues")
        private long[] mylongs;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues")
        private List<Long> myLongList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues")
        private Set<Long> myLongSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues")
        private Float[] myFloats;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues")
        private float[] myfloats;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues")
        private List<Float> myFloatList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues")
        private Set<Float> myFloatSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues")
        private Double[] myDoubles;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues")
        private double[] mydoubles;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues")
        private List<Double> myDoubleList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues")
        private Set<Double> myDoubleSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.durationvalues")
        private Duration[] myDurations;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.durationvalues")
        private List<Duration> myDurationList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.durationvalues")
        private Set<Duration> myDurationSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localtimevalues")
        private LocalTime[] myLocaltimes;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localtimevalues")
        private List<LocalTime> myLocalTimeList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localtimevalues")
        private Set<LocalTime> myLocalTimeSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatevalues")
        private LocalDate[] myDates;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatevalues")
        private List<LocalDate> myLocalDateList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatevalues")
        private Set<LocalDate> myLocalDateSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatetimevalues")
        private LocalDateTime[] myLocalDateTimes;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatetimevalues")
        private List<LocalDateTime> myLocalDateTimeList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatetimevalues")
        private Set<LocalDateTime> myLocalDateTimeSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsetdatetimevalues")
        private OffsetDateTime[] myOffsetDateTimes;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsetdatetimevalues")
        private List<OffsetDateTime> myOffsetDateTimeList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsetdatetimevalues")
        private Set<OffsetDateTime> myOffsetDateTimeSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsettimevalues")
        private OffsetTime[] myOffsetTimes;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsettimevalues")
        private List<OffsetTime> myOffsetTimeList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsettimevalues")
        private Set<OffsetTime> myOffsetTimeSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.instantvalues")
        private Instant[] myInstants;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.instantvalues")
        private List<Instant> myInstantList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.instantvalues")
        private Set<Instant> myInstantSet;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.urlvalues")
        private URL[] myUrls;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.urlvalues")
        private List<URL> myUrlList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.urlvalues")
        private Set<URL> myUrlSet;
        @Inject @ConfigProperty(name ="tck.config.test.javaconfig.converter.array.pizza")
        private Pizza[] pizzas;
        @Inject @ConfigProperty(name ="tck.config.test.javaconfig.converter.array.pizza")
        private List<Pizza> pizzaList;
        @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.array.pizza")
        private Set<Pizza> pizzaSet;
    }

    ///////////////////////////////////Test Boolean[] boolean[]//////////////////////////

    @Test
    public void testBooleanLookupProgrammatically() {
        Boolean[] value = config.getValue("tck.config.test.javaconfig.converter.booleanvalues",
            Boolean[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals( value[0].booleanValue(), true);
        Assert.assertEquals( value[1].booleanValue(), false);
        Assert.assertEquals( value[2].booleanValue(), true);
    }


    @Test
    public void testBooleanArrayInjection() {
        Assert.assertEquals(arrayBean.myBooleans.length, 3);
        Assert.assertEquals(arrayBean.myBooleans[0].booleanValue(), true);
        Assert.assertEquals(arrayBean.myBooleans[1].booleanValue(), false);
        Assert.assertEquals(arrayBean.myBooleans[2].booleanValue(), true);
    }

  //test bool[] support
    @Test
    public void testbooleanArrayInjection() {

        Assert.assertEquals(arrayBean.mybooleans.length, 3);

        Assert.assertEquals(arrayBean.mybooleans[0], true);
        Assert.assertEquals(arrayBean.mybooleans[1], false);
        Assert.assertEquals(arrayBean.mybooleans[2], true);
    }

    @Test
    public void testbooleanListInjection() {

        Assert.assertEquals(arrayBean.myBooleanList.size(), 3);
        Assert.assertTrue(arrayBean.myBooleanList.contains(true));
        Assert.assertTrue(arrayBean.myBooleanList.contains(false));
    }

    @Test
    public void testbooleanSetInjection() {

        Assert.assertEquals(arrayBean.myBooleanSet.size(), 2);
        Assert.assertTrue(arrayBean.myBooleanSet.contains(true));
        Assert.assertTrue(arrayBean.myBooleanSet.contains(false));
    }
    ///////////////////////////////////Test String[] //////////////////////////
    @Test
    public void testStringLookupProgrammatically() {
        String[] value = config.getValue("tck.config.test.javaconfig.converter.stringvalues",
            String[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 4);
        Assert.assertEquals( value[0], "microservice");
        Assert.assertEquals( value[1], "microprofile");
        Assert.assertEquals( value[2], "m,f");
        Assert.assertEquals( value[3], "microservice");

    }

    @Test
    public void testStringArrayInjection() {

        Assert.assertEquals(arrayBean.myStrings.length, 4);
        Assert.assertEquals(arrayBean.myStrings[0], "microservice");
        Assert.assertEquals(arrayBean.myStrings[1], "microprofile");
        Assert.assertEquals(arrayBean.myStrings[2], "m,f");
        Assert.assertEquals(arrayBean.myStrings[3], "microservice");
    }

    @Test
    public void testStringListInjection() {

        Assert.assertEquals(arrayBean.myStringList.size(), 4);
        Assert.assertTrue(arrayBean.myStringList.contains("microservice"));
        Assert.assertTrue(arrayBean.myStringList.contains("microprofile"));
        Assert.assertTrue(arrayBean.myStringList.contains("m,f"));
    }

    @Test
    public void testStringSetInjection() {
        Assert.assertEquals(arrayBean.myStringSet.size(), 3);
        Assert.assertTrue(arrayBean.myStringSet.contains("microservice"));
        Assert.assertTrue(arrayBean.myStringSet.contains("microprofile"));
        Assert.assertTrue(arrayBean.myStringSet.contains("m,f"));
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

        Assert.assertEquals( value[0].intValue(), 1234);
        Assert.assertEquals( value[1].intValue(), 9999);

    }

    @Test
    public void testIntArrayInjection() {

        Assert.assertEquals(arrayBean.myInts.length, 2);
        Assert.assertEquals(arrayBean.myInts[0].intValue(), 1234);
        Assert.assertEquals(arrayBean.myInts[1].intValue(), 9999);
    }

  //test int[] support
    @Test
    public void testintArrayInjection() {
        Assert.assertEquals(arrayBean.myints.length, 2);
        Assert.assertEquals(arrayBean.myints[0], 1234);
        Assert.assertEquals(arrayBean.myints[1], 9999);
    }

    @Test
    public void testIntListInjection() {

        Assert.assertEquals(arrayBean.myIntList.size(), 2);
        Assert.assertTrue(arrayBean.myIntList.contains(1234));
        Assert.assertTrue(arrayBean.myIntList.contains(9999));
    }

    @Test
    public void testIntSetInjection() {
        Assert.assertEquals(arrayBean.myIntSet.size(), 2);
        Assert.assertTrue(arrayBean.myIntSet.contains(1234));
        Assert.assertTrue(arrayBean.myIntSet.contains(9999));
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
        Assert.assertEquals(value[0].longValue(), 1234567890L);
        Assert.assertEquals(value[1].longValue(), 1999999999L);

    }

    @Test
    public void testLongArrayInjection() {

        Assert.assertEquals(arrayBean.myLongs.length, 2);
        Assert.assertEquals(arrayBean.myLongs[0].longValue(), 1234567890L);
        Assert.assertEquals(arrayBean.myLongs[1].longValue(), 1999999999L);
    }

  //test long[] support
    @Test
    public void testlongArrayInjection() {

        Assert.assertEquals(arrayBean.mylongs.length, 2);
        Assert.assertEquals(arrayBean.mylongs[0], 1234567890L);
        Assert.assertEquals(arrayBean.mylongs[1], 1999999999L);
    }

    @Test
    public void testLongListInjection() {

        Assert.assertEquals(arrayBean.myLongList.size(), 2);
        Assert.assertTrue(arrayBean.myLongList.contains(1234567890L));
        Assert.assertTrue(arrayBean.myLongList.contains(1999999999L));
    }
    @Test
    public void testLongSetInjection() {
        Assert.assertEquals(arrayBean.myLongSet.size(), 2);
        Assert.assertTrue(arrayBean.myLongSet.contains(1234567890L));
        Assert.assertTrue(arrayBean.myLongSet.contains(1999999999L));
    }

   ///////////////////////////////////Test Float[] float[]/////////////////////

    //test Float[] support

    @Test
    public void testFloatLookupProgrammatically() {
        Float[] value = config.getValue("tck.config.test.javaconfig.converter.floatvalues",
            Float[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);

        Assert.assertEquals(value[0], 12.34f);
        Assert.assertEquals(value[1], 99.99f);

    }

    @Test
    public void testFloatArrayInjection() {

        Assert.assertEquals(arrayBean.myFloats.length, 2);

        Assert.assertEquals(arrayBean.myFloats[0], 12.34f);
        Assert.assertEquals(arrayBean.myFloats[1], 99.99f);
    }

  //test float[] support
    @Test
    public void testfloatArrayInjection() {

        Assert.assertEquals(arrayBean.myfloats.length, 2);

        Assert.assertEquals(arrayBean.myfloats[0], 12.34f);
        Assert.assertEquals(arrayBean.myfloats[1], 99.99f);
    }

    @Test
    public void testFloatListInjection() {

        Assert.assertEquals(arrayBean.myFloatList.size(), 2);
        Assert.assertTrue(arrayBean.myFloatList.contains(12.34f));
        Assert.assertTrue(arrayBean.myFloatList.contains(99.99f));
    }

    @Test
    public void testFloatSetInjection() {
        Assert.assertEquals(arrayBean.myFloatSet.size(), 2);
        Assert.assertTrue(arrayBean.myFloatSet.contains(12.34f));
        Assert.assertTrue(arrayBean.myFloatSet.contains(99.99f));
    }

    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Test Double[] double[]/////////////


    @Test
    public void testDoubleLookupProgrammatically() {
        Double[] value = config.getValue("tck.config.test.javaconfig.converter.doublevalues",
            Double[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);

        Assert.assertEquals(value[0], 12.34d);
        Assert.assertEquals(value[1], 99.9999d);

    }
    @Test
    public void testDoubleArrayInjection() {

        Assert.assertEquals(arrayBean.myDoubles.length, 2);

        Assert.assertEquals(arrayBean.myDoubles[0], 12.34d);
        Assert.assertEquals(arrayBean.myDoubles[1], 99.9999d);
    }

    //test double[] support

    @Test
    public void testdoubleArrayInjection() {

        Assert.assertEquals(arrayBean.mydoubles.length, 2);

        Assert.assertEquals(arrayBean.mydoubles[0], 12.34d);
        Assert.assertEquals(arrayBean.mydoubles[1], 99.9999d);
    }

    @Test
    public void testDoubleListInjection() {

        Assert.assertEquals(arrayBean.myDoubleList.size(), 2);

        Assert.assertTrue(arrayBean.myDoubleList.contains(12.34d));
        Assert.assertTrue(arrayBean.myDoubleList.contains(99.9999d));
    }

    @Test
    public void testDoubleSetInjection() {
        Assert.assertEquals(arrayBean.myDoubleSet.size(), 2);
        Assert.assertTrue(arrayBean.myDoubleSet.contains(12.34d));
        Assert.assertTrue(arrayBean.myDoubleSet.contains(99.9999d));
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

        Assert.assertEquals( value[0], Duration.parse("PT15M"));
        Assert.assertEquals( value[1], Duration.parse("PT20M"));

    }

    @Test
    public void testDurationArrayInjection() {

        Assert.assertEquals(arrayBean.myDurations.length, 2);

        Assert.assertEquals(arrayBean.myDurations[0], Duration.parse("PT15M"));
        Assert.assertEquals(arrayBean.myDurations[1], Duration.parse("PT20M"));
    }

    @Test
    public void testDurationListInjection() {

        Assert.assertEquals(arrayBean.myDurationList.size(), 2);

        Assert.assertTrue(arrayBean.myDurationList.contains(Duration.parse("PT15M")));
        Assert.assertTrue(arrayBean.myDurationList.contains(Duration.parse("PT20M")));
    }

    @Test
    public void testDurationSetInjection() {
        Assert.assertEquals(arrayBean.myDurationSet.size(), 2);
        Assert.assertTrue(arrayBean.myDurationSet.contains(Duration.parse("PT20M")));
        Assert.assertTrue(arrayBean.myDurationSet.contains(Duration.parse("PT15M")));
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

        Assert.assertEquals( value[0], LocalTime.parse("10:37"));
        Assert.assertEquals( value[1], LocalTime.parse("11:44"));

    }

    @Test
    public void testLocalTimeArrayInjection() {

        Assert.assertEquals(arrayBean.myLocaltimes.length, 2);

        Assert.assertEquals(arrayBean.myLocaltimes[0], LocalTime.parse("10:37"));
        Assert.assertEquals(arrayBean.myLocaltimes[1], LocalTime.parse("11:44"));
    }

    @Test
    public void testLocalTimeListInjection() {

        Assert.assertEquals(arrayBean.myLocalTimeList.size(), 2);

        Assert.assertTrue(arrayBean.myLocalTimeList.contains(LocalTime.parse("10:37")));
        Assert.assertTrue(arrayBean.myLocalTimeList.contains(LocalTime.parse("11:44")));
    }

    @Test
    public void testLocalTimeSetInjection() {
        Assert.assertEquals(arrayBean.myLocalTimeSet.size(), 2);
        Assert.assertTrue(arrayBean.myLocalTimeSet.contains(LocalTime.parse("10:37")));
        Assert.assertTrue(arrayBean.myLocalTimeSet.contains(LocalTime.parse("11:44")));
    }

    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Test LocalDate////////////////////////////

    @Test
    public void testLocalDateLookupProgrammatically() {
        LocalDate[] value = config.getValue("tck.config.test.javaconfig.converter.localdatevalues",
            LocalDate[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);

        Assert.assertEquals( value[0], LocalDate.parse("2017-12-24"));
        Assert.assertEquals( value[1], LocalDate.parse("2017-11-29"));

    }
    @Test
    public void testLocalDateArrayInjection() {

        Assert.assertEquals(arrayBean.myDates.length, 2);

        Assert.assertEquals(arrayBean.myDates[0], LocalDate.parse("2017-12-24"));
        Assert.assertEquals(arrayBean.myDates[1], LocalDate.parse("2017-11-29"));
    }


    @Test
    public void testLocalDateListInjection() {
        Assert.assertEquals(arrayBean.myLocalDateList.size(), 2);

        Assert.assertTrue(arrayBean.myLocalDateList.contains(LocalDate.parse("2017-12-24")));
        Assert.assertTrue(arrayBean.myLocalDateList.contains(LocalDate.parse("2017-11-29")));
    }

    @Test
    public void testLocalDateSetInjection() {
        Assert.assertEquals(arrayBean.myLocalDateSet.size(), 2);
        Assert.assertTrue(arrayBean.myLocalDateSet.contains(LocalDate.parse("2017-12-24")));
        Assert.assertTrue(arrayBean.myLocalDateSet.contains(LocalDate.parse("2017-11-29")));
    }
    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test LocalDateTime////////////////////////////

    @Test
    public void testLocalDateTimeLookupProgrammatically() {
        LocalDateTime[] value = config.getValue("tck.config.test.javaconfig.converter.localdatetimevalues",
             LocalDateTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);

        Assert.assertEquals( value[0], LocalDateTime.parse("2017-12-24T10:25:30"));
        Assert.assertEquals( value[1], LocalDateTime.parse("2017-12-24T10:25:33"));

    }

    @Test
    public void testLocalDateTimeArrayInjection() {
        Assert.assertEquals(arrayBean.myLocalDateTimes.length, 2);

        Assert.assertEquals(arrayBean.myLocalDateTimes[0], LocalDateTime.parse("2017-12-24T10:25:30"));
        Assert.assertEquals(arrayBean.myLocalDateTimes[1], LocalDateTime.parse("2017-12-24T10:25:33"));
    }

    @Test
    public void testLocalDateTimeListInjection() {
        Assert.assertEquals(arrayBean.myLocalDateTimeList.size(), 2);

        Assert.assertTrue(arrayBean.myLocalDateTimeList.contains(LocalDateTime.parse("2017-12-24T10:25:30")));
        Assert.assertTrue(arrayBean.myLocalDateTimeList.contains(LocalDateTime.parse("2017-12-24T10:25:33")));
    }

    @Test
    public void testLocalDateTimeSetInjection() {
         Assert.assertEquals(arrayBean.myLocalDateTimeSet.size(), 2);

         Assert.assertTrue(arrayBean.myLocalDateTimeSet.contains(LocalDateTime.parse("2017-12-24T10:25:30")));
         Assert.assertTrue(arrayBean.myLocalDateTimeSet.contains(LocalDateTime.parse("2017-12-24T10:25:33")));
    }
    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test OffsetDateTime////////////////////////////

    @Test
    public void testOffsetDateTimeLookupProgrammatically() {
        OffsetDateTime[] value = config.getValue("tck.config.test.javaconfig.converter.offsetdatetimevalues",
                OffsetDateTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);

        Assert.assertEquals( value[0], OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
        Assert.assertEquals( value[1], OffsetDateTime.parse("2007-12-03T10:15:30+02:00"));

    }
    @Test
    public void testOffsetDateTimeArrayInjection() {
        Assert.assertEquals(arrayBean.myOffsetDateTimes.length, 2);
        Assert.assertEquals(arrayBean.myOffsetDateTimes[0], OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
        Assert.assertEquals(arrayBean.myOffsetDateTimes[1], OffsetDateTime.parse("2007-12-03T10:15:30+02:00"));
    }

    @Test
    public void testOffsetDateTimeListInjection() {
        Assert.assertEquals(arrayBean.myOffsetDateTimeList.size(), 2);

        Assert.assertTrue(arrayBean.myOffsetDateTimeList.contains(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
        Assert.assertTrue(arrayBean.myOffsetDateTimeList.contains(OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
    }

    @Test
    public void testOffsetDateTimeSetInjection() {
         Assert.assertEquals(arrayBean.myOffsetDateTimeSet.size(), 2);

        Assert.assertTrue(arrayBean.myOffsetDateTimeSet.contains(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
        Assert.assertTrue(arrayBean.myOffsetDateTimeSet.contains(OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
    }
    //////////////////////////////////////////////////////////////////////

    ////////////////////////Test OffsetTime////////////////////////////
    @Test
    public void testOffsetTimeLookupProgrammatically() {
        OffsetTime[] value = config.getValue("tck.config.test.javaconfig.converter.offsettimevalues",
              OffsetTime[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);

        Assert.assertEquals( value[0], OffsetTime.parse("13:45:30.123456789+02:00"));
        Assert.assertEquals( value[1], OffsetTime.parse("13:45:30.123456789+03:00"));

    }

    @Test
    public void testOffsetTimeArrayInjection() {

        Assert.assertEquals(arrayBean.myOffsetDateTimes.length, 2);
        Assert.assertEquals(arrayBean.myOffsetTimes[0], OffsetTime.parse("13:45:30.123456789+02:00"));
        Assert.assertEquals(arrayBean.myOffsetTimes[1], OffsetTime.parse("13:45:30.123456789+03:00"));
    }

    @Test
    public void testOffsetTimeListInjection() {

        Assert.assertEquals(arrayBean.myOffsetTimeList.size(), 2);

        Assert.assertTrue(arrayBean.myOffsetTimeList.contains(OffsetTime.parse("13:45:30.123456789+02:00")));
        Assert.assertTrue(arrayBean.myOffsetTimeList.contains(OffsetTime.parse("13:45:30.123456789+03:00")));
    }

    @Test
    public void testOffsetTimeSetInjection() {
         Assert.assertEquals(arrayBean.myOffsetTimeSet.size(), 2);

         Assert.assertTrue(arrayBean.myOffsetTimeSet.contains(OffsetTime.parse("13:45:30.123456789+02:00")));
         Assert.assertTrue(arrayBean.myOffsetTimeSet.contains(OffsetTime.parse("13:45:30.123456789+03:00")));
    }
    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test instant////////////////////////////


    @Test
    public void testInstantLookupProgrammatically() {
        Instant[] value = config.getValue("tck.config.test.javaconfig.converter.instantvalues",
               Instant[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        Assert.assertEquals( value[0], Instant.parse("2015-06-02T21:34:33.616Z"));
        Assert.assertEquals( value[1], Instant.parse("2017-06-02T21:34:33.616Z"));
    }

    @Test
    public void testInstantArrayInjection() {
        Assert.assertEquals(arrayBean.myInstants.length, 2);
        Assert.assertEquals(arrayBean.myInstants[0], Instant.parse("2015-06-02T21:34:33.616Z"));
        Assert.assertEquals(arrayBean.myInstants[1], Instant.parse("2017-06-02T21:34:33.616Z"));
    }

    @Test
    public void testInstantListInjection() {

        Assert.assertEquals(arrayBean.myInstantList.size(), 2);

        Assert.assertTrue(arrayBean.myInstantList.contains(Instant.parse("2015-06-02T21:34:33.616Z")));
        Assert.assertTrue(arrayBean.myInstantList.contains(Instant.parse("2017-06-02T21:34:33.616Z")));
    }

    @Test
    public void testInstantSetInjection() {
         Assert.assertEquals(arrayBean.myInstantSet.size(), 2);

         Assert.assertTrue(arrayBean.myInstantSet.contains(Instant.parse("2015-06-02T21:34:33.616Z")));
         Assert.assertTrue(arrayBean.myInstantSet.contains(Instant.parse("2017-06-02T21:34:33.616Z")));
    }
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Test URL[] //////////////////////////

    @Test
    public void testUrlLookupProgrammatically() throws MalformedURLException {
        URL[] value = config.getValue("tck.config.test.javaconfig.converter.urlvalues",
            URL[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals( value[0], new URL("http://microprofile.io"));
        Assert.assertEquals( value[1], new URL("http://openliberty.io"));
        Assert.assertEquals( value[2], new URL("http://microprofile.io"));
    }



    @Test
    public void testUrlArrayInjection() throws MalformedURLException {

        Assert.assertEquals(arrayBean.myUrls.length, 3);
        Assert.assertEquals(arrayBean.myUrls[0], new URL("http://microprofile.io"));
        Assert.assertEquals(arrayBean.myUrls[1], new URL("http://openliberty.io"));
        Assert.assertEquals(arrayBean.myUrls[2], new URL("http://microprofile.io"));
    }

    @Test
    public void testURLListInjection() throws MalformedURLException {

        Assert.assertEquals(arrayBean.myUrlList.size(), 3);

        Assert.assertTrue(arrayBean.myUrlList.contains(new URL("http://openliberty.io")));
        Assert.assertTrue(arrayBean.myUrlList.contains(new URL("http://microprofile.io")));
    }

    @Test
    public void testURLSetInjection() throws MalformedURLException {

        Assert.assertEquals(arrayBean.myUrlSet.size(), 2);
        Assert.assertTrue( arrayBean.myUrlSet.contains(new URL("http://openliberty.io")));
        Assert.assertTrue( arrayBean.myUrlSet.contains(new URL("http://microprofile.io")));
    }
    ///////////////////////////////////////////////////////////////////
    //test custom class array support


    @Test
    public void testCustomTypeArrayLookupProgrammatically() {
        Pizza[] value = config.getValue("tck.config.test.javaconfig.converter.array.pizza",
            Pizza[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);

        Assert.assertEquals( value[0],  (new Pizza("cheese,mushroom", "large")));
        Assert.assertEquals( value[1],  (new Pizza("chicken", "medium")));
        Assert.assertEquals( value[2],  (new Pizza("pepperoni", "small")));
    }

    @Test
    public void testCustomTypeArrayInjection() {

        Assert.assertEquals(arrayBean.pizzas.length, 3);
        //large:cheese,medium:chicken,small:pepperoni
        Assert.assertEquals( arrayBean.pizzas[0],  (new Pizza("cheese,mushroom", "large")));
        Assert.assertEquals( arrayBean.pizzas[1],  (new Pizza("chicken", "medium")));
        Assert.assertEquals( arrayBean.pizzas[2],  (new Pizza("pepperoni", "small")));
    }

    @Test
    public void testCustomTypeListInjection()  {

        Assert.assertEquals(arrayBean.pizzaList.size(), 3);

        Assert.assertTrue( arrayBean.pizzaList.contains(new Pizza("cheese,mushroom", "large")));
        Assert.assertTrue( arrayBean.pizzaList.contains(new Pizza("chicken", "medium")));
        Assert.assertTrue( arrayBean.pizzaList.contains(new Pizza("pepperoni", "small")));
    }

    @Test
    public void testCustomTypeSetInjection()  {

        Assert.assertEquals(arrayBean.pizzaSet.size(), 3);
        Assert.assertTrue( arrayBean.pizzaSet.contains(new Pizza("cheese,mushroom", "large")));
        Assert.assertTrue( arrayBean.pizzaSet.contains(new Pizza("chicken", "medium")));
        Assert.assertTrue( arrayBean.pizzaSet.contains(new Pizza("pepperoni", "small")));
    }

}
