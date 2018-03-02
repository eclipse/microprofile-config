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
    private Boolean[] myBooleans;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private boolean[] mybooleans;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private List<Boolean> myBooleanList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.booleanvalues")
    private Set<Boolean> myBooleanSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.stringvalues")
    private String[] myStrings;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.stringvalues")
    private List<String> myStringList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.stringvalues")
    private Set<String> myStringSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private Integer[] myInts;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private int[] myints;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private List<Integer> myIntList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.integervalues")
    private Set<Integer> myIntSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private Long[] myLongs;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private long[] mylongs;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private List<Long> myLongList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.longvalues")
    private Set<Long> myLongSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private Float[] myFloats;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private float[] myfloats;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private List<Float> myFloatList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.floatvalues")
    private Set<Float> myFloatSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private Double[] myDoubles;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private double[] mydoubles;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private List<Double> myDoubleList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.doublevalues")
    private Set<Double> myDoubleSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalues")
    private Duration[] myDurations;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalues")
    private List<Duration> myDurationList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.durationvalues")
    private Set<Duration> myDurationSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalues")
    private LocalTime[] myLocaltimes;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalues")
    private List<LocalTime> myLocalTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localtimevalues")
    private Set<LocalTime> myLocalTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalues")
    private LocalDate[] myDates;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalues")
    private List<LocalDate> myLocalDateList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatevalues")
    private Set<LocalDate> myLocalDateSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalues")
    private LocalDateTime[] myLocalDateTimes;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalues")
    private List<LocalDateTime> myLocalDateTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.localdatetimevalues")
    private Set<LocalDateTime> myLocalDateTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalues")
    private OffsetDateTime[] myOffsetDateTimes;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalues")
    private List<OffsetDateTime> myOffsetDateTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsetdatetimevalues")
    private Set<OffsetDateTime> myOffsetDateTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalues")
    private OffsetTime[] myOffsetTimes;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalues")
    private List<OffsetTime> myOffsetTimeList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.offsettimevalues")
    private Set<OffsetTime> myOffsetTimeSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalues")
    private Instant[] myInstants;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalues")
    private List<Instant> myInstantList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.instantvalues")
    private Set<Instant> myInstantSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private URL[] myUrls;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private List<URL> myUrlList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private Set<URL> myUrlSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private URI[] myUris;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private List<URI> myUriList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.urlvalues")
    private Set<URI> myUriSet;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.array.pizza")
    private Pizza[] pizzas;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.array.pizza")
    private List<Pizza> pizzaList;
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.array.pizza")
    private Set<Pizza> pizzaSet;

    public Boolean[] getMyBooleans() {
        return myBooleans;
    }

    public boolean[] getMybooleans() {
        return mybooleans;
    }

    public List<Boolean> getMyBooleanList() {
        return myBooleanList;
    }

    public Set<Boolean> getMyBooleanSet() {
        return myBooleanSet;
    }

    public String[] getMyStrings() {
        return myStrings;
    }

    public List<String> getMyStringList() {
        return myStringList;
    }

    public Set<String> getMyStringSet() {
        return myStringSet;
    }

    public Integer[] getMyInts() {
        return myInts;
    }

    public int[] getMyints() {
        return myints;
    }

    public List<Integer> getMyIntList() {
        return myIntList;
    }

    public Set<Integer> getMyIntSet() {
        return myIntSet;
    }

    public Long[] getMyLongs() {
        return myLongs;
    }

    public long[] getMylongs() {
        return mylongs;
    }

    public List<Long> getMyLongList() {
        return myLongList;
    }

    public Set<Long> getMyLongSet() {
        return myLongSet;
    }

    public Float[] getMyFloats() {
        return myFloats;
    }

    public float[] getMyfloats() {
        return myfloats;
    }

    public List<Float> getMyFloatList() {
        return myFloatList;
    }

    public Set<Float> getMyFloatSet() {
        return myFloatSet;
    }

    public Double[] getMyDoubles() {
        return myDoubles;
    }

    public double[] getMydoubles() {
        return mydoubles;
    }

    public List<Double> getMyDoubleList() {
        return myDoubleList;
    }

    public Set<Double> getMyDoubleSet() {
        return myDoubleSet;
    }

    public Duration[] getMyDurations() {
        return myDurations;
    }

    public List<Duration> getMyDurationList() {
        return myDurationList;
    }

    public Set<Duration> getMyDurationSet() {
        return myDurationSet;
    }

    public LocalTime[] getMyLocaltimes() {
        return myLocaltimes;
    }

    public List<LocalTime> getMyLocalTimeList() {
        return myLocalTimeList;
    }

    public Set<LocalTime> getMyLocalTimeSet() {
        return myLocalTimeSet;
    }

    public LocalDate[] getMyDates() {
        return myDates;
    }

    public List<LocalDate> getMyLocalDateList() {
        return myLocalDateList;
    }

    public Set<LocalDate> getMyLocalDateSet() {
        return myLocalDateSet;
    }

    public LocalDateTime[] getMyLocalDateTimes() {
        return myLocalDateTimes;
    }

    public List<LocalDateTime> getMyLocalDateTimeList() {
        return myLocalDateTimeList;
    }

    public Set<LocalDateTime> getMyLocalDateTimeSet() {
        return myLocalDateTimeSet;
    }

    public OffsetDateTime[] getMyOffsetDateTimes() {
        return myOffsetDateTimes;
    }

    public List<OffsetDateTime> getMyOffsetDateTimeList() {
        return myOffsetDateTimeList;
    }

    public Set<OffsetDateTime> getMyOffsetDateTimeSet() {
        return myOffsetDateTimeSet;
    }

    public OffsetTime[] getMyOffsetTimes() {
        return myOffsetTimes;
    }

    public List<OffsetTime> getMyOffsetTimeList() {
        return myOffsetTimeList;
    }

    public Set<OffsetTime> getMyOffsetTimeSet() {
        return myOffsetTimeSet;
    }

    public Instant[] getMyInstants() {
        return myInstants;
    }

    public List<Instant> getMyInstantList() {
        return myInstantList;
    }

    public Set<Instant> getMyInstantSet() {
        return myInstantSet;
    }

    public URL[] getMyUrls() {
        return myUrls;
    }

    public List<URL> getMyUrlList() {
        return myUrlList;
    }

    public Set<URL> getMyUrlSet() {
        return myUrlSet;
    }

    public Pizza[] getPizzas() {
        return pizzas;
    }

    public List<Pizza> getPizzaList() {
        return pizzaList;
    }

    public Set<Pizza> getPizzaSet() {
        return pizzaSet;
    }

    public URI[] getMyUris() {
        return myUris;
    }

    public List<URI> getMyUriList() {
        return myUriList;
    }

    public Set<URI> getMyUriSet() {
        return myUriSet;
    }
}
