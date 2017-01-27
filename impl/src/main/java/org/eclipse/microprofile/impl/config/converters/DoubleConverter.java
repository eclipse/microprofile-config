/*
 * Copyright (c) 2009-2017 Mark Struberg and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
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
package org.eclipse.microprofile.impl.config.converters;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.spi.Converter;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
@Priority(1)
public class DoubleConverter implements Converter<Double> {

    public static final DoubleConverter INSTANCE = new DoubleConverter();

    @Override
    public Double convert(String value) {
        return value != null ? Double.valueOf(value) : null;
    }
}
