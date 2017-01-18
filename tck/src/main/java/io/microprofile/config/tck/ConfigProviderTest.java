/*
 * Copyright (c) 2016-2017 Original Author and authors, IBM Corp and others
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
package io.microprofile.config.tck;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;

import io.microprofile.config.Config;
import io.microprofile.config.ConfigProvider;

import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigProviderTest {

    @Test
    public void testConfigProviderWithDefaultTCCL() {
        ClassLoader oldTccl = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader tempCl = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
            Thread.currentThread().setContextClassLoader(tempCl);
            Config config = ConfigProvider.getConfig();
            Assert.assertNotNull(config);

            Config config2 = ConfigProvider.getConfig(tempCl);
            Assert.assertNotNull(config2);
            Assert.assertEquals(config, config2);
        }
        finally {
            Thread.currentThread().setContextClassLoader(oldTccl);
        }
    }

    @Test
    public void testEnvironmentConfigSource() {
        Map<String, String> env = System.getenv();
        Config config = ConfigProvider.getConfig();
        for (Map.Entry<String, String> envEntry : env.entrySet()) {
            Assert.assertEquals(envEntry.getValue(), config.getString(envEntry.getKey()).get());
        }
    }

    @Test
    public void testPropertyConfigSource() {
        Properties properties = System.getProperties();
        Config config = ConfigProvider.getConfig();

        for (Map.Entry<Object, Object> propEntry : properties.entrySet()) {
            Assert.assertEquals(propEntry.getValue(), config.getString((String) propEntry.getKey()).get());
        }
    }

    @Test
    public void testDynamicValueInPropertyConfigSource() {
        Config config = ConfigProvider.getConfig();
        String configKey = "tck.config.test.systemproperty.dynamic.value";
        String configValue = "myDynamicValue;";

        System.setProperty(configKey, configValue);
        Assert.assertEquals(config.getString(configKey).get(), configValue);
    }

    @Test
    public void testJavaConfigPropertyFilesConfigSource() {
        Config config = ConfigProvider.getConfig();
        Assert.assertEquals(config.getString("tck.config.test.javaconfig.properties.key1").get(), "VALue1");
    }

    @Test
    public void testNonExistingConfigKey() {
        Config config = ConfigProvider.getConfig();
        Assert.assertNull(config.getString("tck.config.test.keydoesnotexist").get());
    }

/* TODO important for Gunnars use case!
    @Test
    public void testRegisterManualConfig() {
        // make sure we get a clean config
        ConfigProvider.releaseConfig(ConfigProvider.getConfig());

        ConfigProvider.registerConfig()
                .ignoreDefaultSources()
                .withSources(new SampleYamlConfigSource(null))
                .build();

        Config config = ConfigProvider.getConfig();
        Assert.assertNotNull(config);
        Assert.assertNotNull(config.getConfigSources());
        Assert.assertEquals(1, config.getConfigSources().length);
        Assert.assertEquals(SampleYamlConfigSource.class, config.getConfigSources()[0].getClass());

        Assert.assertEquals("yamlvalue1", config.getValue("tck.config.test.sampleyaml.key1"));

        Assert.assertNull(config.getValue("tck.config.test.javaconfig.properties.key1"));

        {
            // try it again, Sam
            // this time we should fail as we already have a Config registered for this application
            try {
                ConfigProvider.registerConfig()
                        .ignoreDefaultSources()
                        .withSources(new SampleYamlConfigSource(null))
                        .build();

                Assert.fail("We fail because subsequently registering another Config for the application is not allowed");
            }
            catch (IllegalStateException ise) {
                // all fine
            }
        }

        // clean up the dirt afterwards
        ConfigProvider.releaseConfig(ConfigProvider.getConfig());
    }
*/

    @Test
    public void testConfigProviderRelease() {
        Config config1 = ConfigProvider.getConfig();
        Config config2 = ConfigProvider.getConfig();

        Assert.assertEquals(config2, config1);

        ConfigProvider.releaseConfig(config2);

        Config config3 = ConfigProvider.getConfig();

        Assert.assertNotEquals(config1, config3);
    }
}
