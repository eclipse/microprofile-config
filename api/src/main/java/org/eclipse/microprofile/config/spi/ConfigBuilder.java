/*
 * Copyright (c) 2017 Mark Struberg and others
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
package org.eclipse.microprofile.config.spi;

import org.eclipse.microprofile.config.Config;

/**
 * Builder for manually creating an instance of a {@code Config}.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public abstract class ConfigBuilder {
    /**
     * Trigger this method to prevent picking up the default ConfigSources for environment,
     * system properties and META-INF/microprofile-config.properties.
     */
    public abstract ConfigBuilder ignoreDefaultSources();

    public abstract ConfigBuilder withSources(ConfigSource... sources);

    public abstract ConfigBuilder withConverters(Converter<?>... filters);

    /**
     * Add ConfigSources based on the given resource, e.g. "META-INF/myconfig.properties"
     * @param resourceUrl the resource to look for
     */
    public abstract ConfigBuilder withResources(String resourceUrl);

    /**
     * This method should only get called by the {@link ConfigExtension}
     * @return the final Config
     */
    protected abstract Config build();
}
