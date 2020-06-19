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
package org.eclipse.microprofile.config.tck;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class ConfigValueTest extends Arquillian {
    @Deployment
    public static Archive deployment() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "ConfigValueTest.jar")
            .addClasses(ConfigValueBean.class)
            .addAsServiceProvider(ConfigSource.class, ConfigValueConfigSource.class)
            .addAsServiceProvider(ConfigSource.class, ConfigValueLowerConfigSource.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .as(JavaArchive.class);

        return ShrinkWrap
            .create(WebArchive.class, "ConfigValueTest.war")
            .addAsLibrary(testJar);
    }

    @Test
    void configValue() {
        ConfigValue configValue = ConfigProvider.getConfig().getConfigValue("my.prop");
        assertNotNull(configValue);
        assertEquals("my.prop", configValue.getName());
        assertEquals("1234", configValue.getValue());
        assertEquals("ConfigValueConfigSource", configValue.getSourceName());
        assertEquals(1000, configValue.getSourceOrdinal());
    }

    @Test
    public void configValueEmpty() {
        ConfigValue configValue = ConfigProvider.getConfig().getConfigValue("not.found");
        assertNotNull(configValue);
        assertEquals("not.found", configValue.getName());
        assertNull(configValue.getValue());
        assertNull(configValue.getSourceName());
        assertEquals(0, configValue.getSourceOrdinal());
    }

    @Inject
    private ConfigValueBean configValueBean;

    @Test
    public void configValueInjection() {
        final ConfigValue configValue = configValueBean.getConfigValue();
        assertNotNull(configValue);
        assertEquals("my.prop", configValue.getName());
        assertEquals("1234", configValue.getValue());
        assertEquals(ConfigValueConfigSource.class.getSimpleName(), configValue.getSourceName());
        assertEquals(1000, configValue.getSourceOrdinal());

        final ConfigValue configValueDefault = configValueBean.getConfigValueDefault();
        assertNotNull(configValueDefault);
        assertEquals("my.prop", configValue.getName());
        assertEquals("default", configValueDefault.getValue());
        assertNull(configValueDefault.getSourceName());
    }

    public static class ConfigValueConfigSource implements ConfigSource {
        private Map<String, String> properties;

        public ConfigValueConfigSource() {
            properties = new HashMap<>();
            properties.put("my.prop", "1234");
        }

        @Override
        public Set<String> getPropertyNames() {
            return properties.keySet();
        }

        @Override
        public String getValue(final String propertyName) {
            return properties.get(propertyName);
        }

        @Override
        public String getName() {
            return this.getClass().getSimpleName();
        }

        @Override
        public int getOrdinal() {
            return 1000;
        }
    }

    public static class ConfigValueLowerConfigSource implements ConfigSource {
        private Map<String, String> properties;

        public ConfigValueLowerConfigSource() {
            properties = new HashMap<>();
            properties.put("my.prop", "5678");
        }

        @Override
        public Set<String> getPropertyNames() {
            return properties.keySet();
        }

        @Override
        public String getValue(final String propertyName) {
            return properties.get(propertyName);
        }

        @Override
        public String getName() {
            return this.getClass().getSimpleName();
        }

        @Override
        public int getOrdinal() {
            return 900;
        }
    }

    @Dependent
    public static class ConfigValueBean {
        @Inject
        @ConfigProperty(name = "my.prop")
        private ConfigValue configValue;

        @Inject
        @ConfigProperty(name = "my.prop.default", defaultValue = "default")
        private ConfigValue configValueDefault;

        ConfigValue getConfigValue() {
            return configValue;
        }

        ConfigValue getConfigValueDefault() {
            return configValueDefault;
        }
    }
}
