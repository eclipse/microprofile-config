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
package org.eclipse.microprofile.config.tck;

import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */

public class ConfigProviderTest extends AbstractTest {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        return allIn("configProviderTest").addClass(ConfigProviderTest.class);
    }

    @Test
    public void testEnvironmentConfigSource() {
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, String> envEntry : env.entrySet()) {
            Assert.assertEquals(envEntry.getValue(), config.getValue(envEntry.getKey(), String.class));
        }
    }

    @Test
    public void testPropertyConfigSource() {
        Properties properties = System.getProperties();

        for (Map.Entry<Object, Object> propEntry : properties.entrySet()) {
            Assert.assertEquals(propEntry.getValue(), config.getValue((String) propEntry.getKey(), String.class));
        }
    }

    @Test
    public void testDynamicValueInPropertyConfigSource() {
        String configKey = "tck.config.test.systemproperty.dynamic.value";
        String configValue = "myDynamicValue;";

        System.setProperty(configKey, configValue);
        Assert.assertEquals(config.getValue(configKey, String.class), configValue);
    }

    @Test
    public void testJavaConfigPropertyFilesConfigSource() {
        Assert.assertEquals(config.getValue("tck.config.test.javaconfig.properties.key1", String.class), "VALue1");
    }

    @Test
    public void testNonExistingConfigKey() {
        Assert.assertFalse(config.getOptionalValue("tck.config.test.keydoesnotexist", String.class).isPresent());
    }

    @Test
    public void testEmptyConfigTreatedAsNotExisting() {
        Assert.assertFalse(config.getOptionalValue("tck.config.test.javaconfig.emptyvalue", String.class).isPresent());
    }

    @Test
    public void testGetConfigSources() {
        Iterable<ConfigSource> configSources = config.getConfigSources();
        Assert.assertNotNull(configSources);

        // check descending sorting
        int prevOrdinal = Integer.MAX_VALUE;
        for (ConfigSource configSource : configSources) {
            Assert.assertTrue(configSource.getOrdinal() <= prevOrdinal);
            prevOrdinal = configSource.getOrdinal();
        }

    }

}
