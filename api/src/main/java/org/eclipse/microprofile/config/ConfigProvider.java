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
 *
 *******************************************************************************/

package org.eclipse.microprofile.config;

import javax.enterprise.inject.spi.CDI;

import org.eclipse.microprofile.config.spi.ConfigExtension;


/**
 * <p>
 * This is the central class to access a {@link Config}.
 * </p>
 *
 *
 * Example usage:
 *
 * <pre>
 * String restUrl = ConfigProvider.getConfig().getValue(
 *         &quot;myproject.some.remote.service.url&quot;);
 * Integer port = ConfigProvider.getConfig().getValue(
 *         &quot;myproject.some.remote.service.port&quot;, Integer.class);
 * </pre>
 *
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public final class ConfigProvider {
    /**
     * Provide a {@link Config} based on all {@link org.eclipse.microprofile.config.spi.ConfigSource ConfigSources}
     *
     * <p>There is exactly a single Config instance per Application</p>
     * of the current Thread Context ClassLoader (TCCL)
     *
     * @return the current {@link Config}
     */
    public static Config getConfig() {
        return CDI.current().getBeanManager().getExtension(ConfigExtension.class).getConfig();
    }
}
