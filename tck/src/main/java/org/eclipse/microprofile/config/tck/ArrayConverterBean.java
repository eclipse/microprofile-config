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

import java.net.URI;
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

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.tck.converters.Pizza;

@Dependent
public class ArrayConverterBean {

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private Boolean[] myBooleanArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private boolean[] mybooleanArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private List<Boolean> myBooleanList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private Set<Boolean> myBooleanSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.boolean.true")
    private Boolean[] mySingleBooleanArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.boolean.true")
    private boolean[] mySinglebooleanArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.boolean.true")
    private List<Boolean> mySingleBooleanList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.boolean.true")
    private Set<Boolean> mySingleBooleanSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.stringvalues")
    private String[] myStringArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.stringvalues")
    private List<String> myStringList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.stringvalues")
    private Set<String> myStringSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.key1")
    private String[] mySingleStringArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.key1")
    private List<String> mySingleStringList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.configvalue.key1")
    private Set<String> mySingleStringSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private Integer[] myIntegerArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private int[] myintArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private List<Integer> myIntegerList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private Set<Integer> myIntegerSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalue")
    private Integer[] mySingleIntegerArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalue")
    private int[] mySingleintArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalue")
    private List<Integer> mySingleIntegerList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalue")
    private Set<Integer> mySingleIntegerSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private Long[] myLongArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private long[] mylongArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private List<Long> myLongList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private Set<Long> myLongSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalue")
    private Long[] mySingleLongArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalue")
    private long[] mySinglelong;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalue")
    private List<Long> mySingleLongList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalue")
    private Set<Long> mySingleLongSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private Float[] myFloatArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private float[] myfloatArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private List<Float> myFloatList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private Set<Float> myFloatSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalue")
    private Float[] mySingleFloatArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalue")
    private float[] mySinglefloatArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalue")
    private List<Float> mySingleFloatList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalue")
    private Set<Float> mySingleFloatSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private Double[] myDoubleArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private double[] mydoubleArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private List<Double> myDoubleList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private Set<Double> myDoubleSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalue")
    private Double[] mySingleDoubleArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalue")
    private double[] mySingledoubleArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalue")
    private List<Double> mySingleDoubleList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalue")
    private Set<Double> mySingleDoubleSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalues")
    private Duration[] myDurationArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalues")
    private List<Duration> myDurationList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalues")
    private Set<Duration> myDurationSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalue")
    private Duration[] mySingleDurationArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalue")
    private List<Duration> mySingleDurationList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalue")
    private Set<Duration> mySingleDurationSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalues")
    private LocalTime[] myLocaltimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalues")
    private List<LocalTime> myLocalTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalues")
    private Set<LocalTime> myLocalTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalue")
    private LocalTime[] mySingleLocaltimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalue")
    private List<LocalTime> mySingleLocalTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalue")
    private Set<LocalTime> mySingleLocalTimeSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalues")
    private LocalDate[] myLocalDateArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalues")
    private List<LocalDate> myLocalDateList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalues")
    private Set<LocalDate> myLocalDateSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalue")
    private LocalDate[] mySingleLocalDateArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalue")
    private List<LocalDate> mySingleLocalDateList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalue")
    private Set<LocalDate> mySingleLocalDateSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalues")
    private LocalDateTime[] myLocalDateTimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalues")
    private List<LocalDateTime> myLocalDateTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalues")
    private Set<LocalDateTime> myLocalDateTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalue")
    private LocalDateTime[] mySingleLocalDateTimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalue")
    private List<LocalDateTime> mySingleLocalDateTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalue")
    private Set<LocalDateTime> mySingleLocalDateTimeSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalues")
    private OffsetDateTime[] myOffsetDateTimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalues")
    private List<OffsetDateTime> myOffsetDateTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalues")
    private Set<OffsetDateTime> myOffsetDateTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalue")
    private OffsetDateTime[] mySingleOffsetDateTimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalue")
    private List<OffsetDateTime> mySingleOffsetDateTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalue")
    private Set<OffsetDateTime> mySingleOffsetDateTimeSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalues")
    private OffsetTime[] myOffsetTimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalues")
    private List<OffsetTime> myOffsetTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalues")
    private Set<OffsetTime> myOffsetTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalue")
    private OffsetTime[] mySingleOffsetTimeArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalue")
    private List<OffsetTime> mySingleOffsetTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalue")
    private Set<OffsetTime> mySingleOffsetTimeSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalues")
    private Instant[] myInstantArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalues")
    private List<Instant> myInstantList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalues")
    private Set<Instant> myInstantSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalue")
    private Instant[] mySingleInstantArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalue")
    private List<Instant> mySingleInstantList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalue")
    private Set<Instant> mySingleInstantSet;
 
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private URL[] myUrlArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private List<URL> myUrlList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private Set<URL> myUrlSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalue")
    private URL[] mySingleUrlArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalue")
    private List<URL> mySingleUrlList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalue")
    private Set<URL> mySingleUrlSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private URI[] myUriArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private List<URI> myUriList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private Set<URI> myUriSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalue")
    private URI[] mySingleUriArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalue")
    private List<URI> mySingleUriList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalue")
    private Set<URI> mySingleUriSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.array.pizza")
    private Pizza[] myPizzaArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.array.pizza")
    private List<Pizza> myPizzaList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.array.pizza")
    private Set<Pizza> myPizzaSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.pizza")
    private Pizza[] mySinglePizzaArray;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.pizza")
    private List<Pizza> mySinglePizzaList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.pizza")
    private Set<Pizza> mySinglePizzaSet;

    public Boolean[] getMyBooleanArray() {
        return myBooleanArray;
    }

    public boolean[] getMybooleanArray() {
        return mybooleanArray;
    }

    public List<Boolean> getMyBooleanList() {
        return myBooleanList;
    }

    public Set<Boolean> getMyBooleanSet() {
        return myBooleanSet;
    }

    public Boolean[] getMySingleBooleanArray() {
        return mySingleBooleanArray;
    }

    public boolean[] getMySinglebooleanArray() {
        return mySinglebooleanArray;
    }

    public List<Boolean> getMySingleBooleanList() {
        return mySingleBooleanList;
    }

    public Set<Boolean> getMySingleBooleanSet() {
        return mySingleBooleanSet;
    }

    public String[] getMyStringArray() {
        return myStringArray;
    }

    public List<String> getMyStringList() {
        return myStringList;
    }

    public Set<String> getMyStringSet() {
        return myStringSet;
    }

    public String[] getMySingleStringArray() {
        return mySingleStringArray;
    }

    public List<String> getMySingleStringList() {
        return mySingleStringList;
    }

    public Set<String> getMySingleStringSet() {
        return mySingleStringSet;
    }

    public Integer[] getMyIntegerArray() {
        return myIntegerArray;
    }

    public int[] getMyintArray() {
        return myintArray;
    }

    public List<Integer> getMyIntegerList() {
        return myIntegerList;
    }

    public Set<Integer> getMyIntegerSet() {
        return myIntegerSet;
    }

    public Integer[] getMySingleIntegerArray() {
        return mySingleIntegerArray;
    }

    public int[] getMySingleintArray() {
        return mySingleintArray;
    }

    public List<Integer> getMySingleIntegerList() {
        return mySingleIntegerList;
    }

    public Set<Integer> getMySingleIntegerSet() {
        return mySingleIntegerSet;
    }

    public Long[] getMyLongArray() {
        return myLongArray;
    }

    public long[] getMylongArray() {
        return mylongArray;
    }

    public List<Long> getMyLongList() {
        return myLongList;
    }

    public Set<Long> getMyLongSet() {
        return myLongSet;
    }

    public Long[] getMySingleLongArray() {
        return mySingleLongArray;
    }

    public long[] getMySinglelongArray() {
        return mySinglelong;
    }

    public List<Long> getMySingleLongList() {
        return mySingleLongList;
    }

    public Set<Long> getMySingleLongSet() {
        return mySingleLongSet;
    }

    public Float[] getMyFloatArray() {
        return myFloatArray;
    }

    public float[] getMyfloatArray() {
        return myfloatArray;
    }

    public List<Float> getMyFloatList() {
        return myFloatList;
    }

    public Set<Float> getMyFloatSet() {
        return myFloatSet;
    }

    public Float[] getMySingleFloatArray() {
        return mySingleFloatArray;
    }

    public float[] getMySinglefloatArray() {
        return mySinglefloatArray;
    }

    public List<Float> getMySingleFloatList() {
        return mySingleFloatList;
    }

    public Set<Float> getMySingleFloatSet() {
        return mySingleFloatSet;
    }

    public Double[] getMyDoubleArray() {
        return myDoubleArray;
    }

    public double[] getMydoubleArray() {
        return mydoubleArray;
    }

    public List<Double> getMyDoubleList() {
        return myDoubleList;
    }

    public Set<Double> getMyDoubleSet() {
        return myDoubleSet;
    }

    public Double[] getMySingleDoubleArray() {
        return mySingleDoubleArray;
    }

    public double[] getMySingledoubleArray() {
        return mySingledoubleArray;
    }

    public List<Double> getMySingleDoubleList() {
        return mySingleDoubleList;
    }

    public Set<Double> getMySingleDoubleSet() {
        return mySingleDoubleSet;
    }

    public Duration[] getMyDurationArray() {
        return myDurationArray;
    }

    public List<Duration> getMyDurationList() {
        return myDurationList;
    }

    public Set<Duration> getMyDurationSet() {
        return myDurationSet;
    }

    public Duration[] getMySingleDurationArray() {
        return mySingleDurationArray;
    }

    public List<Duration> getMySingleDurationList() {
        return mySingleDurationList;
    }

    public Set<Duration> getMySingleDurationSet() {
        return mySingleDurationSet;
    }

    public LocalTime[] getMyLocaltimeArray() {
        return myLocaltimeArray;
    }

    public List<LocalTime> getMyLocalTimeList() {
        return myLocalTimeList;
    }

    public Set<LocalTime> getMyLocalTimeSet() {
        return myLocalTimeSet;
    }

    public LocalTime[] getMySingleLocaltimeArray() {
        return mySingleLocaltimeArray;
    }

    public List<LocalTime> getMySingleLocalTimeList() {
        return mySingleLocalTimeList;
    }

    public Set<LocalTime> getMySingleLocalTimeSet() {
        return mySingleLocalTimeSet;
    }

    public LocalDate[] getMyLocalDateArray() {
        return myLocalDateArray;
    }

    public List<LocalDate> getMyLocalDateList() {
        return myLocalDateList;
    }

    public Set<LocalDate> getMyLocalDateSet() {
        return myLocalDateSet;
    }

    public LocalDate[] getMySingleLocalDateArray() {
        return mySingleLocalDateArray;
    }

    public List<LocalDate> getMySingleLocalDateList() {
        return mySingleLocalDateList;
    }

    public Set<LocalDate> getMySingleLocalDateSet() {
        return mySingleLocalDateSet;
    }

    public LocalDateTime[] getMyLocalDateTimeArray() {
        return myLocalDateTimeArray;
    }

    public List<LocalDateTime> getMyLocalDateTimeList() {
        return myLocalDateTimeList;
    }

    public Set<LocalDateTime> getMyLocalDateTimeSet() {
        return myLocalDateTimeSet;
    }

    public LocalDateTime[] getMySingleLocalDateTimeArray() {
        return mySingleLocalDateTimeArray;
    }

    public List<LocalDateTime> getMySingleLocalDateTimeList() {
        return mySingleLocalDateTimeList;
    }

    public Set<LocalDateTime> getMySingleLocalDateTimeSet() {
        return mySingleLocalDateTimeSet;
    }

    public OffsetDateTime[] getMyOffsetDateTimeArray() {
        return myOffsetDateTimeArray;
    }

    public List<OffsetDateTime> getMyOffsetDateTimeList() {
        return myOffsetDateTimeList;
    }

    public Set<OffsetDateTime> getMyOffsetDateTimeSet() {
        return myOffsetDateTimeSet;
    }

    public OffsetDateTime[] getMySingleOffsetDateTimeArray() {
        return mySingleOffsetDateTimeArray;
    }

    public List<OffsetDateTime> getMySingleOffsetDateTimeList() {
        return mySingleOffsetDateTimeList;
    }

    public Set<OffsetDateTime> getMySingleOffsetDateTimeSet() {
        return mySingleOffsetDateTimeSet;
    }

    public OffsetTime[] getMyOffsetTimeArray() {
        return myOffsetTimeArray;
    }

    public List<OffsetTime> getMyOffsetTimeList() {
        return myOffsetTimeList;
    }

    public Set<OffsetTime> getMyOffsetTimeSet() {
        return myOffsetTimeSet;
    }

    public OffsetTime[] getMySingleOffsetTimeArray() {
        return mySingleOffsetTimeArray;
    }

    public List<OffsetTime> getMySingleOffsetTimeList() {
        return mySingleOffsetTimeList;
    }

    public Set<OffsetTime> getMySingleOffsetTimeSet() {
        return mySingleOffsetTimeSet;
    }

    public Instant[] getMyInstantArray() {
        return myInstantArray;
    }

    public List<Instant> getMyInstantList() {
        return myInstantList;
    }

    public Set<Instant> getMyInstantSet() {
        return myInstantSet;
    }

    public Instant[] getMySingleInstantArray() {
        return mySingleInstantArray;
    }

    public List<Instant> getMySingleInstantList() {
        return mySingleInstantList;
    }

    public Set<Instant> getMySingleInstantSet() {
        return mySingleInstantSet;
    }

    public URL[] getMyUrlArray() {
        return myUrlArray;
    }

    public List<URL> getMyUrlList() {
        return myUrlList;
    }

    public Set<URL> getMyUrlSet() {
        return myUrlSet;
    }

    public URL[] getMySingleUrlArray() {
        return mySingleUrlArray;
    }

    public List<URL> getMySingleUrlList() {
        return mySingleUrlList;
    }

    public Set<URL> getMySingleUrlSet() {
        return mySingleUrlSet;
    }

    public URI[] getMyUriArray() {
        return myUriArray;
    }

    public List<URI> getMyUriList() {
        return myUriList;
    }

    public Set<URI> getMyUriSet() {
        return myUriSet;
    }

    public URI[] getMySingleUriArray() {
        return mySingleUriArray;
    }

    public List<URI> getMySingleUriList() {
        return mySingleUriList;
    }

    public Set<URI> getMySingleUriSet() {
        return mySingleUriSet;
    }

    public Pizza[] getMyPizzaArray() {
        return myPizzaArray;
    }

    public List<Pizza> getMyPizzaList() {
        return myPizzaList;
    }

    public Set<Pizza> getMyPizzaSet() {
        return myPizzaSet;
    }

    public Pizza[] getMySinglePizzaArray() {
        return mySinglePizzaArray;
    }

    public List<Pizza> getMySinglePizzaList() {
        return mySinglePizzaList;
    }

    public Set<Pizza> getMySinglePizzaSet() {
        return mySinglePizzaSet;
    }
}
