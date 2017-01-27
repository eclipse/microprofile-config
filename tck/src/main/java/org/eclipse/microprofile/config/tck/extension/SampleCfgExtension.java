/*
 * Copyright (c) 2016-2017 Mark Struberg and others
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
 */
package org.eclipse.microprofile.config.tck.extension;


import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;

import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.eclipse.microprofile.config.tck.configsources.SampleYamlConfigSource;
import org.eclipse.microprofile.config.tck.converters.DuckConverter;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class SampleCfgExtension implements Extension {

    public void registerCustomConfig(@Observes ConfigBuilder configBuilder) {
        configBuilder.withSources(new CustomDbConfigSource(), new SampleYamlConfigSource(null))
            .withConverters(new DuckConverter());
    }
}
