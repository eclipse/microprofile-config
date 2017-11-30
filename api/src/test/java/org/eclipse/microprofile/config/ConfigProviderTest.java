/*
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation
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

package org.eclipse.microprofile.config;

import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class ConfigProviderTest {
    @Test
    public void testUsesProperlyOverriddenProvider() {
        // uses the META-INF version
        Config original = ConfigProvider.getConfig();
        assertNotNull(original);
        // override based on how OSGI would work
        ConfigProviderResolver.setInstance(new FakeConfigProviderResolver());
        Config config = ConfigProvider.getConfig();
        assertNull(config);
    }

    private class FakeConfigProviderResolver extends ConfigProviderResolver {

        @Override
        public Config getConfig() {
            return null;
        }

        @Override
        public Config getConfig(ClassLoader loader) {
            return null;
        }

        @Override
        public ConfigBuilder getBuilder() {
            return null;
        }

        @Override
        public void registerConfig(Config config, ClassLoader classLoader) {

        }

        @Override
        public void releaseConfig(Config config) {

        }
    }
}
