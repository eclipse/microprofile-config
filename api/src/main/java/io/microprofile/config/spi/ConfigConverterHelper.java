/*
 *******************************************************************************
 * Copyright (c) 2016 IBM Corp. and others
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
 *
 *******************************************************************************/

package io.microprofile.config.spi;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The helper class returns all the built-in converters.
 *
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public final class ConfigConverterHelper {

    private ConfigConverterHelper() {
        // utility ct only
    }

    /**
     * Convert the string to a BigDecimal value or throw NumberFormatException
     * if unable to convert
     * 
     */
    public static Converter<BigDecimal> bigDecimalConverter = BigDecimal::new;
    /**
     * Convert the string to a BigInterger value or throw NumberFormatException
     * if unable to convert
     *
     */
    public static Converter<BigInteger> bigIntegerConverter = BigInteger::new;
    /**
     * Covert the string to a boolean. If the string is "true", "yes" or "on"
     * regardless of the case, the return value will be TRUE. Otherwise, it is
     * FALSE.
     */
    public static Converter<Boolean> booleanConverter = Boolean::valueOf;
    /**
     * Convert the string to a Byte value or throw NumberFormatException if
     * unable to convert
     *
     */
    public static Converter<Byte> byteConverter = Byte::new;
    /**
     * Convert the string to a Double value or throw NumberFormatException if
     * unable to convert
     *
     */
    public static Converter<Double> doubleConverter = Double::new;
    /**
     * Convert the string to a Float value or throw NumberFormatException if
     * unable to convert
     */
    public static Converter<Float> floatConverter = Float::new;
    /**
     * Convert the string to a Interger value
     *
     */
    public static Converter<Integer> integerConverter = Integer::getInteger;
    /**
     * Convert the string to a Long value
     *
     */
    public static Converter<Long> longConverter = Long::getLong;
    /**
       * Convert the string to a Short value or throw NumberFormatException if
     * unable to convert
     *
     */
    public static Converter<Short> shortConverter = Short::new;
    /**
     * Convert the string to a URI or throw ConvertException if unable to
     * convert
     *
     */
    public static Converter<URI> uriConverter = (String value) -> {

        URI uri = null;
        try {
            uri = new URI(value);
        }
        catch (URISyntaxException use) {
            throw new ConvertException(use);
        }
        return uri;
    };
    /**
     * Convert the string to a URL or throw ConvertException if unable to
     * convert
     */
    public static Converter<URL> urlConverter = (String value) -> {

        URL url;
        try {
            url = new URL(value);
        }
        catch (MalformedURLException mfue) {
            throw new ConvertException(mfue);
        }
        return url;
    };


    private static final Map<Type, Converter<?>> CONVERTERS = new HashMap<>();
    static {
        CONVERTERS.put(BigDecimal.class, bigDecimalConverter);
        CONVERTERS.put(BigInteger.class, bigIntegerConverter);
        CONVERTERS.put(Boolean.class, booleanConverter);
        CONVERTERS.put(Byte.class, byteConverter);
        CONVERTERS.put(Double.class, doubleConverter);
        CONVERTERS.put(Float.class, floatConverter);
        CONVERTERS.put(Integer.class, integerConverter);
        CONVERTERS.put(Long.class, longConverter);
        CONVERTERS.put(Short.class, shortConverter);
        CONVERTERS.put(URL.class, urlConverter);
        CONVERTERS.put(URI.class, uriConverter);
    }

    public static Map<Type, Converter<?>> getDefaultConverters() {
        return Collections.unmodifiableMap(CONVERTERS);
    }

}
