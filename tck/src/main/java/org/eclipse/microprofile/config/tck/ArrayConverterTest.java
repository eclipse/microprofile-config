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

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
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
                .addClass(ArrayConverterTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

            addFile(testJar, "META-INF/microprofile-config.properties");

            WebArchive war = ShrinkWrap
                .create(WebArchive.class, "arrayConverterTest.war")
                .addAsLibrary(testJar);
            return war;
    }


    private @Inject Config config;
    

  
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

    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues") Boolean[] myBooleans;
    @Test
    public void testBooleanArrayInjection() {
       
        Assert.assertEquals(myBooleans.length, 3);
        Assert.assertEquals( myBooleans[0].booleanValue(), true);
        Assert.assertEquals( myBooleans[1].booleanValue(), false);
        Assert.assertEquals( myBooleans[2].booleanValue(), true);
    }
    
  //test bool[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues") boolean[] mybooleans;
    @Test
    public void testbooleanArrayInjection() {
       
        Assert.assertEquals(mybooleans.length, 3);
        
        Assert.assertEquals( mybooleans[0], true);
        Assert.assertEquals( mybooleans[1], false);
        Assert.assertEquals( mybooleans[2], true);
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues") List<Boolean> myBooleanList;
    @Test
    public void testbooleanListInjection() {
       
        Assert.assertEquals(myBooleanList.size(), 3);
        Assert.assertTrue( myBooleanList.contains(true));
        Assert.assertTrue( myBooleanList.contains(false));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.booleanvalues") Set<Boolean> myBooleanSet;
    @Test
    public void testbooleanSetInjection() {
       
        Assert.assertEquals(myBooleanSet.size(), 2);
        Assert.assertTrue( myBooleanSet.contains(true));
        Assert.assertTrue( myBooleanSet.contains(false));
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

    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.stringvalues") String[] myStrings;
    @Test
    public void testStringArrayInjection() {
       
        Assert.assertEquals(myStrings.length, 4);
        Assert.assertEquals( myStrings[0], "microservice");
        Assert.assertEquals( myStrings[1], "microprofile");
        Assert.assertEquals( myStrings[2], "m,f");
        Assert.assertEquals( myStrings[3], "microservice");
    }
    
  
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.stringvalues") List<String> myStringList;
    
    @Test
    public void testStringListInjection() {
       
        Assert.assertEquals(myStringList.size(), 4);
        Assert.assertTrue( myStringList.contains("microservice"));
        Assert.assertTrue( myStringList.contains("microprofile"));
        Assert.assertTrue( myStringList.contains("m,f"));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.stringvalues") Set<String> myStringSet;
    
    @Test
    public void testStringSetInjection() {
        Assert.assertEquals(myStringSet.size(), 3);
        Assert.assertTrue( myStringSet.contains("microservice"));
        Assert.assertTrue( myStringSet.contains("microprofile"));
        Assert.assertTrue( myStringSet.contains("m,f"));
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

    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues") Integer[] myInts;
    
    @Test
    public void testIntArrayInjection() {
       
        Assert.assertEquals(myInts.length, 2);
        Assert.assertEquals( myInts[0].intValue(), 1234);
        Assert.assertEquals( myInts[1].intValue(), 9999);
    }
    
  //test int[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues") int[] myints;
    @Test
    public void testintArrayInjection() {
        Assert.assertEquals(myints.length, 2);
        Assert.assertEquals( myints[0], 1234);
        Assert.assertEquals( myints[1], 9999);
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues") List<Integer> myIntList;
    @Test
    public void testIntListInjection() {
       
        Assert.assertEquals(myIntList.size(), 2);
        Assert.assertTrue( myIntList.contains(1234));
        Assert.assertTrue( myIntList.contains(9999));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.integervalues") Set<Integer> myIntSet;
    @Test
    public void testIntSetInjection() {
        Assert.assertEquals(myIntSet.size(), 2);
        Assert.assertTrue( myIntSet.contains(1234));
        Assert.assertTrue( myIntSet.contains(9999));
    }
    ///////////////////////////////////////////////////////////////////
    //////////////////Test Long[] long[]///////////////////////////////
    //test Long[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues") Long[] myLongs;
    
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
       
        Assert.assertEquals(myLongs.length, 2);
        Assert.assertEquals( myLongs[0].longValue(), 1234567890L);
        Assert.assertEquals( myLongs[1].longValue(), 1999999999L);
    }
    
  //test long[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues") long[] mylongs;
    @Test
    public void testlongArrayInjection() {
       
        Assert.assertEquals(mylongs.length, 2);
        Assert.assertEquals(mylongs[0], 1234567890L);
        Assert.assertEquals(mylongs[1], 1999999999L);
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues") List<Long> myLongList;
    
    @Test
    public void testLongListInjection() {
       
        Assert.assertEquals(myLongList.size(), 2);
        Assert.assertTrue( myLongList.contains(1234567890L));
        Assert.assertTrue( myLongList.contains(1999999999L));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.longvalues") Set<Long> myLongSet;
    @Test
    public void testLongSetInjection() {
        Assert.assertEquals(myLongSet.size(), 2);
        Assert.assertTrue(myLongSet.contains(1234567890L));
        Assert.assertTrue(myLongSet.contains(1999999999L));
    }
    
   ///////////////////////////////////Test Float[] float[]/////////////////////
    
    //test Float[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues") Float[] myFloats;
    
    @Test
    public void testFloatLookupProgrammatically() {
        Float[] value = config.getValue("tck.config.test.javaconfig.converter.floatvalues",
            Float[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        
        Assert.assertEquals( value[0].floatValue(), 12.34f);
        Assert.assertEquals( value[1].floatValue(), 99.99f);
       
    }

    @Test
    public void testFloatArrayInjection() {
       
        Assert.assertEquals(myFloats.length, 2);
        
        Assert.assertEquals(myFloats[0].floatValue(), 12.34f);
        Assert.assertEquals(myFloats[1].floatValue(), 99.99f);
    }
    
  //test float[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues") float[] myfloats;
    @Test
    public void testfloatArrayInjection() {
       
        Assert.assertEquals(myfloats.length, 2);
        
        Assert.assertEquals(myfloats[0], 12.34f);
        Assert.assertEquals(myfloats[1], 99.99f);
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues") List<Float> myFloatList;
    @Test
    public void testFloatListInjection() {
       
        Assert.assertEquals(myFloatList.size(), 2);
        Assert.assertTrue( myFloatList.contains(12.34f));
        Assert.assertTrue( myFloatList.contains(99.99f));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.floatvalues") Set<Float> myFloatSet;
    
    @Test
    public void testFloatSetInjection() {
        Assert.assertEquals(myFloatSet.size(), 2);
        Assert.assertTrue( myFloatSet.contains(12.34f));
        Assert.assertTrue( myFloatSet.contains(99.99f));
    }
    
    //////////////////////////////////////////////////////////////////////
    
    ///////////////////////////////////Test Double[] double[]/////////////
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues") Double[] myDoubles;
    
    @Test
    public void testDoubleLookupProgrammatically() {
        Double[] value = config.getValue("tck.config.test.javaconfig.converter.doublevalues",
            Double[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 2);
        
        Assert.assertEquals( value[0].floatValue(), 12.34d);
        Assert.assertEquals( value[1].floatValue(), 99.9999d);
       
    }
    @Test
    public void testDoubleArrayInjection() {
       
        Assert.assertEquals(myDoubles.length, 2);
        
        Assert.assertEquals(myDoubles[0].floatValue(), 12.34d);
        Assert.assertEquals(myDoubles[1].floatValue(), 99.9999d);
    }
    
    //test double[] support
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues") double[] mydoubles;  
    
    @Test
    public void testdoubleArrayInjection() {
       
        Assert.assertEquals(mydoubles.length, 2);
        
        Assert.assertEquals(mydoubles[0], 12.34d);
        Assert.assertEquals(mydoubles[1], 99.9999d);
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues") List<Double> myDoubleList;
    @Test
    public void testDoubleListInjection() {
       
        Assert.assertEquals(myDoubleList.size(), 2);
        
        Assert.assertTrue(myDoubleList.contains(12.34d));
        Assert.assertTrue(myDoubleList.contains(99.9999d));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.doublevalues") Set<Double> myDoubleSet;
    
    @Test
    public void testDoubleSetInjection() {
        Assert.assertEquals(myDoubleSet.size(), 2);
        Assert.assertTrue(myDoubleSet.contains(12.34d));
        Assert.assertTrue(myDoubleSet.contains(99.9999d));
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
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.durationvalues") Duration[] myDurations;
    
    @Test
    public void testDurationArrayInjection() {
       
        Assert.assertEquals(myDurations.length, 2);
        
        Assert.assertEquals(myDurations[0], Duration.parse("PT15M"));
        Assert.assertEquals(myDurations[1], Duration.parse("PT20M"));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.durationvalues") List<Duration> myDurationList;
    @Test
    public void testDurationListInjection() {
       
        Assert.assertEquals(myDurationList.size(), 2);
        
        Assert.assertTrue(myDurationList.contains(Duration.parse("PT15M")));
        Assert.assertTrue(myDurationList.contains(Duration.parse("PT20M")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.durationvalues") Set<Duration> myDurationSet;
    
    @Test
    public void testDurationSetInjection() {
        Assert.assertEquals(myDurationSet.size(), 2);
        Assert.assertTrue(myDurationSet.contains(Duration.parse("PT20M")));
        Assert.assertTrue(myDurationSet.contains(Duration.parse("PT15M")));
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
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localtimevalues") LocalTime[] myLocaltimes;
    
    @Test
    public void testLocalTimeArrayInjection() {
       
        Assert.assertEquals(myLocaltimes.length, 2);
        
        Assert.assertEquals(myLocaltimes[0], LocalTime.parse("10:37"));
        Assert.assertEquals(myLocaltimes[1], LocalTime.parse("11:44"));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localtimevalues") List<LocalTime> myLocalTimeList;
    @Test
    public void testLocalTimeListInjection() {
       
        Assert.assertEquals(myLocalTimeList.size(), 2);
        
        Assert.assertTrue(myLocalTimeList.contains(LocalTime.parse("10:37")));
        Assert.assertTrue(myLocalTimeList.contains(LocalTime.parse("11:44")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localtimevalues") Set<LocalTime> myLocalTimeSet;
    
    @Test
    public void testLocalTimeSetInjection() {
        Assert.assertEquals(myLocalTimeSet.size(), 2);
        Assert.assertTrue(myLocalTimeSet.contains(LocalTime.parse("10:37")));
        Assert.assertTrue(myLocalTimeSet.contains(LocalTime.parse("11:44")));
    }
    
    //////////////////////////////////////////////////////////////////////
    ///////////////////////////Test LocalDate////////////////////////////
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatevalues") LocalDate[] myDates;
    
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
       
        Assert.assertEquals(myDates.length, 2);
        
        Assert.assertEquals( myDates[0], LocalDate.parse("2017-12-24"));
        Assert.assertEquals( myDates[1], LocalDate.parse("2017-11-29"));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatevalues") List<LocalDate> myLocalDateList;
    @Test
    public void testLocalDateListInjection() {
       
        Assert.assertEquals(myLocalDateList.size(), 2);
        
        Assert.assertTrue(myLocalDateList.contains(LocalDate.parse("2017-12-24")));
        Assert.assertTrue(myLocalDateList.contains(LocalDate.parse("2017-11-29")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatevalues") Set<LocalDate> myLocalDateSet;
    
    @Test
    public void testLocalDateSetInjection() {
        Assert.assertEquals(myLocalDateSet.size(), 2);
        Assert.assertTrue(myLocalDateSet.contains(LocalDate.parse("2017-12-24")));
        Assert.assertTrue(myLocalDateSet.contains(LocalDate.parse("2017-11-29")));
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
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatetimevalues") LocalDateTime[] myLocalDateTimes;
    
    @Test
    public void testLocalDateTimeArrayInjection() {
       
        Assert.assertEquals(myLocalDateTimes.length, 2);
        
        Assert.assertEquals( myLocalDateTimes[0], LocalDateTime.parse("2017-12-24T10:25:30"));
        Assert.assertEquals( myLocalDateTimes[1], LocalDateTime.parse("2017-12-24T10:25:33"));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatetimevalues") List<LocalDateTime> myLocalDateTimeList;
    @Test
    public void testLocalDateTimeListInjection() {
       
        Assert.assertEquals(myLocalDateTimeList.size(), 2);
        
        Assert.assertTrue(myLocalDateTimeList.contains(LocalDateTime.parse("2017-12-24T10:25:30")));
        Assert.assertTrue(myLocalDateTimeList.contains(LocalDateTime.parse("2017-12-24T10:25:33")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.localdatetimevalues") Set<LocalDateTime> myLocalDateTimeSet;
    
    @Test
    public void testLocalDateTimeSetInjection() {
         Assert.assertEquals(myLocalDateTimeSet.size(), 2);
         
         Assert.assertTrue(myLocalDateTimeSet.contains(LocalDateTime.parse("2017-12-24T10:25:30")));
         Assert.assertTrue(myLocalDateTimeSet.contains(LocalDateTime.parse("2017-12-24T10:25:33")));
    }
    //////////////////////////////////////////////////////////////////////
    ////////////////////////Test OffsetDateTime////////////////////////////

    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsetdatetimevalues") OffsetDateTime[] myOffsetDateTimes;
    
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
       
        Assert.assertEquals(myOffsetDateTimes.length, 2);
        Assert.assertEquals( myOffsetDateTimes[0], OffsetDateTime.parse("2007-12-03T10:15:30+01:00"));
        Assert.assertEquals( myOffsetDateTimes[1], OffsetDateTime.parse("2007-12-03T10:15:30+02:00"));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsetdatetimevalues") List<OffsetDateTime> myOffsetDateTimeList;
    @Test
    public void testOffsetDateTimeListInjection() {
       
        Assert.assertEquals(myOffsetDateTimeList.size(), 2);
        
        Assert.assertTrue(myOffsetDateTimeList.contains(OffsetDateTime.parse("2007-12-03T10:15:30+01:00")));
        Assert.assertTrue(myOffsetDateTimeList.contains(OffsetDateTime.parse("2007-12-03T10:15:30+02:00")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsetdatetimevalues") Set<OffsetDateTime> myOffsetDateTimeSet;
    
    @Test
    public void testOffsetDateTimeSetInjection() {
         Assert.assertEquals(myOffsetDateTimeSet.size(), 2);
         
         Assert.assertTrue(myOffsetDateTimeSet.contains(LocalDateTime.parse("2017-12-24T10:25:30")));
         Assert.assertTrue(myOffsetDateTimeSet.contains(LocalDateTime.parse("2017-12-24T10:25:33")));
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
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsettimevalues") OffsetTime[] myOffsetTimes;
    
    @Test
    public void testOffsetTimeArrayInjection() {
       
        Assert.assertEquals(myOffsetDateTimes.length, 2);
        Assert.assertEquals( myOffsetTimes[0], OffsetTime.parse("13:45:30.123456789+02:00"));
        Assert.assertEquals( myOffsetTimes[1], OffsetTime.parse("13:45:30.123456789+03:00"));
    }
    
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsettimevalues") List<OffsetTime> myOffsetTimeList;
    @Test
    public void testOffsetTimeListInjection() {
       
        Assert.assertEquals(myOffsetTimeList.size(), 2);
        
        Assert.assertTrue(myOffsetTimeList.contains(OffsetTime.parse("13:45:30.123456789+02:00")));
        Assert.assertTrue(myOffsetTimeList.contains(OffsetTime.parse("13:45:30.123456789+03:00")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.offsettimevalues") Set<OffsetTime> myOffsetTimeSet;
    
    @Test
    public void testOffsetTimeSetInjection() {
         Assert.assertEquals(myOffsetTimeSet.size(), 2);
         
         Assert.assertTrue(myOffsetTimeSet.contains(OffsetTime.parse("13:45:30.123456789+02:00")));
         Assert.assertTrue(myOffsetTimeSet.contains(OffsetTime.parse("13:45:30.123456789+03:00")));
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
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.instantvalues") Instant[] myInstants;
    
    @Test
    public void testInstantArrayInjection() {
       
        Assert.assertEquals(myInstants.length, 2);
        Assert.assertEquals( myInstants[0], Instant.parse("2015-06-02T21:34:33.616Z"));
        Assert.assertEquals( myInstants[1], Instant.parse("2017-06-02T21:34:33.616Z"));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.instantvalues") List<Instant> myInstantList;
    @Test
    public void testInstantListInjection() {
       
        Assert.assertEquals(myInstantList.size(), 2);
        
        Assert.assertTrue(myInstantList.contains(Instant.parse("2015-06-02T21:34:33.616Z")));
        Assert.assertTrue(myInstantList.contains(Instant.parse("2017-06-02T21:34:33.616Z")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.instantvalues") Set<Instant> myInstantSet;
    
    @Test
    public void testInstantSetInjection() {
         Assert.assertEquals(myInstantSet.size(), 2);
         
         Assert.assertTrue(myInstantSet.contains(Instant.parse("2015-06-02T21:34:33.616Z")));
         Assert.assertTrue(myInstantSet.contains(Instant.parse("2017-06-02T21:34:33.616Z")));
    }
    //////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Test URL[] //////////////////////////
    
    @Test
    public void testUrlLookupProgrammatically() {
        URL[] value = config.getValue("tck.config.test.javaconfig.converter.urlvalues",
            URL[].class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.length, 3);
        Assert.assertEquals( value[0], "http://microprofile.io");
        Assert.assertEquals( value[1], "http://openliberty.io");
        Assert.assertEquals( value[2], "http://microprofile.io");
    }

    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.urlvalues") URL[] myUrls;
    
    @Test
    public void testUrlArrayInjection() throws MalformedURLException {
       
        Assert.assertEquals(myUrls.length, 3);
        Assert.assertEquals( myUrls[0], new URL("http://microprofile.io"));
        Assert.assertEquals( myUrls[1], new URL("http://openliberty.io"));
        Assert.assertEquals( myUrls[2], new URL("http://microprofile.io"));
    }
    
  
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.urlvalues") List<URL> myUrlList;
    
    @Test
    public void testURLListInjection() throws MalformedURLException {
       
        Assert.assertEquals(myUrlList.size(), 3);
        
        Assert.assertTrue( myUrlList.contains(new URL("http://openliberty.io")));
        Assert.assertTrue( myUrlList.contains(new URL("http://microprofile.io")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.urlvalues") Set<URL> myUrlSet;
    
    @Test
    public void testURLSetInjection() throws MalformedURLException {
        
        Assert.assertEquals(myUrlSet.size(), 2);
        Assert.assertTrue( myUrlSet.contains(new URL("http://openliberty.io")));
        Assert.assertTrue( myUrlSet.contains(new URL("http://microprofile.io")));
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

    private @Inject @ConfigProperty(name ="tck.config.test.javaconfig.converter.array.pizza") Pizza[] pizzas; 
    @Test
    public void testCustomTypeArrayInjection() {
       
        Assert.assertEquals(pizzas.length, 3);
        //large:cheese,medium:chicken,small:pepperoni
        Assert.assertEquals( pizzas[0],  (new Pizza("cheese,mushroom", "large")));
        Assert.assertEquals( pizzas[1],  (new Pizza("chicken", "medium")));
        Assert.assertEquals( pizzas[2],  (new Pizza("pepperoni", "small")));
    }
    private @Inject @ConfigProperty(name ="tck.config.test.javaconfig.converter.array.pizza") List<Pizza> pizzaList; 
    @Test
    public void testCustomTypeListInjection()  {
       
        Assert.assertEquals(pizzaList.size(), 3);
        
        Assert.assertTrue( pizzaList.contains(new Pizza("cheese,mushroom", "large")));
        Assert.assertTrue( pizzaList.contains(new Pizza("chicken", "medium")));
        Assert.assertTrue( pizzaList.contains(new Pizza("pepperoni", "small")));
    }
    private @Inject @ConfigProperty(name="tck.config.test.javaconfig.converter.array.pizza") Set<Pizza> pizzaSet;
    
    @Test
    public void testCustomTypeSetInjection()  {
       
        Assert.assertEquals(pizzaSet.size(), 3);
        Assert.assertTrue( pizzaSet.contains(new Pizza("cheese,mushroom", "large")));
        Assert.assertTrue( pizzaSet.contains(new Pizza("chicken", "medium")));
        Assert.assertTrue( pizzaSet.contains(new Pizza("pepperoni", "small")));
    }
    
}
    