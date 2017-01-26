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
package org.eclipse.microprofile.impl.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Priority;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.impl.config.converters.BooleanConverter;
import org.eclipse.microprofile.impl.config.converters.DoubleConverter;
import org.eclipse.microprofile.impl.config.converters.FloatConverter;
import org.eclipse.microprofile.impl.config.converters.IntegerConverter;
import org.eclipse.microprofile.impl.config.converters.LongConverter;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigImpl implements Config {
    protected final Logger logger = Logger.getLogger(ConfigImpl.class.getName());

    protected final List<ConfigSource> configSources;
    protected final Map<Type, Converter> converters = new HashMap<>();


    public ConfigImpl(List<ConfigSource> configSources, List<Converter<?>> converters) {
        this.configSources = sortDescending(configSources);

        registerDefaultConverter();
        for (Converter<?> converter : converters) {
            addConverter(converter);
        }
    }

    private void registerDefaultConverter() {
        converters.put(Boolean.class, BooleanConverter.INSTANCE);
        converters.put(Double.class, DoubleConverter.INSTANCE);
        converters.put(Float.class, FloatConverter.INSTANCE);
        converters.put(Integer.class, IntegerConverter.INSTANCE);
        converters.put(Long.class, LongConverter.INSTANCE);
    }

    @Override
    public Optional<String> getValue(String key) {
        return Optional.ofNullable(getInternalValue(key));
    }

    protected String getInternalValue(String key) {
        for (ConfigSource configSource : configSources) {
            String value = configSource.getValue(key);

            if (value != null) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "found value {0} for key {1} in ConfigSource {2}.",
                            new Object[]{value, key, configSource.getId()});
                }

                return value;
            }
        }
        return null;
    }

    @Override
    public <T> Optional<T> getValue(String key, final Class<T> asType) {
        String value = getInternalValue(key);
        return Optional.ofNullable(convert(value, asType));

    }

    public <T> T convert(String value, Class<T> asType) {
        Converter<T> converter = getConverter(asType);
        if (value != null) {
            return converter.convert(value);
        }

        return null;
    }

    private <T> Converter getConverter(Class<T> asType) {
        Converter converter = converters.get(asType);
        if (converter == null) {
            throw new UnsupportedOperationException("No Converter registered for class " + asType);
        }
        return converter;
    }

    @Override
    public Iterable<String> getPropertyNames() {
        Set<String> propertyNames = new HashSet<>();
        for (ConfigSource configSource : configSources) {
            propertyNames.addAll(configSource.getProperties().keySet());
        }

        return propertyNames;
    }


    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return configSources;
    }


    protected void addConverter(Converter<?> converter) {
        if (converter == null) {
            return;
        }

        Type targetType = getTypeOfConverter(converter.getClass());
        if (targetType == null ) {
            throw new IllegalStateException("Converter " + converter.getClass() + " must be a ParameterisedType");
        }

        Converter oldConverter = converters.get(targetType);
        if (oldConverter == null || getPriority(converter) > getPriority(oldConverter)) {
            converters.put(targetType, converter);
        }
    }

    private int getPriority(Converter<?> converter) {
        int priority = 100;
        Priority priorityAnnotation = converter.getClass().getAnnotation(Priority.class);
        if (priorityAnnotation != null) {
            priority = priorityAnnotation.value();
        }
        return priority;
    }


    public Map<Type, Converter> getConverters() {
        return converters;
    }


    protected List<ConfigSource> sortDescending(List<ConfigSource> configSources) {
        Collections.sort(configSources, new Comparator<ConfigSource>() {
            @Override
            public int compare(ConfigSource configSource1, ConfigSource configSource2) {
                return (configSource1.getOrdinal() > configSource2.getOrdinal()) ? -1 : 1;
            }
        });
        return configSources;

    }

    private Type getTypeOfConverter(Class clazz) {
        if (clazz.equals(Object.class)) {
            return null;
        }

        Type[] genericInterfaces = clazz.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericInterface;
                if (pt.getRawType().equals(Converter.class)) {
                    Type[] typeArguments = pt.getActualTypeArguments();
                    if (typeArguments.length != 1) {
                        throw new IllegalStateException("Converter " + clazz + " must be a ParameterisedType");
                    }
                    return typeArguments[0];
                }
            }
        }

        return getTypeOfConverter(clazz.getSuperclass());
    }
}