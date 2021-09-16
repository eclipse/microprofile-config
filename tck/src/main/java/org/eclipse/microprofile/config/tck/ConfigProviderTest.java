/*
 * Copyright (c) 2016, 2021 Contributors to the Eclipse Foundation
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import jakarta.inject.Inject;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigProviderTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "configProviderTest.war")
                .addPackage(AbstractTest.class.getPackage())
                .addClass(ConfigProviderTest.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        AbstractTest.addFile(war, "META-INF/microprofile-config.properties");

        return war;
    }

    @BeforeClass
    public static void setupCheck() {
        // check that there is at least one property which is unique to the environment and not also a system property
        boolean checkOK = false;
        Map<String, String> env = System.getenv();
        Properties properties = System.getProperties();
        for (Map.Entry<String, String> envEntry : env.entrySet()) {
            String key = envEntry.getKey();
            if (!properties.containsKey(key)) {
                checkOK = true;
                break;
            }
        }
        Assert.assertTrue(checkOK, "Ensure that there is at least one property which is unique to " +
                "the environment variables and not also a system property.");
    }

    @Test
    public void testEnvironmentConfigSource() {
        String value = System.getenv().get("MP_TCK_ENV_DUMMY");
        Assert.assertNotNull(value);
        Assert.assertEquals("dummy", value);
        Assert.assertEquals(value, config.getValue("mp.tck.env.dummy", String.class));
    }

    @Test
    public void testPropertyConfigSource() {
        Assert.assertNotNull(config.getValue("java.version", String.class));
        String value = System.getProperties().getProperty("mp.tck.prop.dummy");
        Assert.assertNotNull(value);
        Assert.assertEquals("dummy", value);
        Assert.assertEquals(value, config.getValue("mp.tck.prop.dummy", String.class));
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

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNonExistingConfigKeyGet() {
        config.getValue("tck.config.test.keydoesnotexist", String.class);
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

    @Test
    public void testInjectedConfigSerializable() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream)) {
            out.writeObject(config);
        } catch (IOException ex) {
            Assert.fail("Injected config should be serializable, but could not serialize it", ex);
        }
        Object readObject = null;
        try (ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))) {
            readObject = in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Assert.fail(
                    "Injected config should be serializable, but could not deserialize a previously serialized instance",
                    ex);
        }
        MatcherAssert.assertThat("Deserialized object", readObject, CoreMatchers.instanceOf(Config.class));
    }

    @Test
    public void testGetPropertyNames() {
        String configKey = "some.arbitrary.key";
        String configValue = "value";
        System.setProperty(configKey, configValue);
        AtomicBoolean foundKey = new AtomicBoolean(false);
        config.getConfigSources().forEach(c -> {
            if (c.getPropertyNames().contains(configKey)) {
                foundKey.set(true);
            }
        });

        Assert.assertTrue(foundKey.get(), "Unable to find property " + configKey);
    }
}
