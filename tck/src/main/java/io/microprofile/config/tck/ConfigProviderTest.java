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
package io.microprofile.config.tck;

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

}
