/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck.converters;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

import static org.testng.Assert.assertThrows;

public class NullConvertersTest {
    @Test
    public void nulls() {
        final Config config = ConfigProvider.getConfig();

        assertThrows(NullPointerException.class, () -> convertNull(config, Boolean.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Byte.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Short.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Integer.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Long.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Float.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Double.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, Character.class));

        assertThrows(NullPointerException.class, () -> convertNull(config, OptionalInt.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, OptionalLong.class));
        assertThrows(NullPointerException.class, () -> convertNull(config, OptionalDouble.class));
    }

    private static <T> void convertNull(Config config, Class<T> converterType) {
        config.getConverter(converterType)
              .map(converter -> converter.convert(null))
              .orElseThrow(NoSuchElementException::new);
    }
}
