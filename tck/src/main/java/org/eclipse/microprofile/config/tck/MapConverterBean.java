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

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.tck.converters.Pizza;

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
import java.util.Map;
import java.util.Set;

@Dependent
public class MapConverterBean {

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.map.string.string")
    private Map<String, String> myStringStringMap;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.map.integer.string")
    private Map<Integer, String> myIntegerStringMap;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.map.string.integer")
    private Map<String, Integer> myStringIntegerMap;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.map.enum.enum")
    private Map<EnumKey, EnumValue> myEnumEnumMap;

    public Map<String, String> getMyStringStringMap() {
        return myStringStringMap;
    }

    public Map<Integer, String> getMyIntegerStringMap() {
        return myIntegerStringMap;
    }

    public Map<String, Integer> getMyStringIntegerMap() {
        return myStringIntegerMap;
    }

    public Map<EnumKey, EnumValue> getMyEnumEnumMap() {
        return myEnumEnumMap;
    }

    public enum EnumKey {
        key1,
        key2;
    }

    public enum EnumValue {
        enum1,
        enum2;
    }
}
