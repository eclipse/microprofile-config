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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.impl.config.configsource.PropertyFileConfigSourceProvider;
import org.eclipse.microprofile.impl.config.configsource.SystemEnvConfigSource;
import org.eclipse.microprofile.impl.config.configsource.SystemPropertyConfigSource;
import static java.util.Arrays.asList;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigBuilderImpl extends ConfigBuilder {
    private final List<ConfigSource> sources = new ArrayList<>();
    private final List<Converter<?>> converters = new ArrayList<>();
    private boolean ignoreDefaultSources = false;

    @Override
    public ConfigBuilder ignoreDefaultSources() {
        this.ignoreDefaultSources = true;
        return this;
    }

    @Override
    public ConfigBuilder withSources(final ConfigSource... sources) {
        this.sources.addAll(asList(sources));
        return this;
    }

    @Override
    public ConfigBuilder withConverters(Converter<?>... converters) {
        this.converters.addAll(asList(converters));
        return this;
    }


    @Override
    protected Config build() {
        List<ConfigSource> configSources = new ArrayList<>();

        ClassLoader forClassLoader = getClassLoader();
        if (!ignoreDefaultSources) {
            configSources.addAll(getBuiltInConfigSources(forClassLoader));
        }
        configSources.addAll(sources);

        return new ConfigImpl(configSources, converters);
    }

    @Override
    public ConfigBuilder withResources(String resourceUrl) {
        throw new UnsupportedOperationException("TODO, not yet supported");
    }

    protected ClassLoader getClassLoader() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        return cl;
    }

    protected Collection<? extends ConfigSource> getBuiltInConfigSources(ClassLoader forClassLoader) {
        List<ConfigSource> configSources = new ArrayList<>();

        configSources.add(new SystemEnvConfigSource());
        configSources.add(new SystemPropertyConfigSource());
        configSources.addAll(new PropertyFileConfigSourceProvider("META-INF/java-config.properties", true, forClassLoader).getConfigSources());

        return configSources;
    }

}
