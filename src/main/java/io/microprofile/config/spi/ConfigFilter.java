/*******************************************************************************
 * Copyright 2016 Microprofile.io
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

import io.microprofile.config.ConfigProvider;

/**
 * <p>A filter which can be added to the
 * {@link ConfigProvider.ConfigBuilder}.
 * The filter can be used to decrypt config values or prepare
 * values for logging.</p>
 *
 * <p>Registering a {@code ConfigFilter} can either be done via the
 * {@code java.util.ServiceLoader} pattern or by manually adding it via
 * {@link ConfigProvider.ConfigBuilder#addConfigFilter(ConfigFilter)}.</p>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public interface ConfigFilter
{

    /**
     * Filter the given configuration value for usage in logs.
     * This might be used to mask out passwords, etc.
     * @param key
     * @param value
     * @return the filtered value or the original input String if no filter shall be applied
     */
    String filterValueForLog(String key, String value);
}
