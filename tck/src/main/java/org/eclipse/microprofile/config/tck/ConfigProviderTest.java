/**********************************************************************
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation 
 *
 * See the NOTICES file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************************/
package org.eclipse.microprofile.config.tck;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigProviderTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "configProviderTest.jar")
                .addPackage(AbstractTest.class.getPackage())
                .addClass(ConfigProviderTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        AbstractTest.addFile(testJar, "META-INF/microprofile-config.properties");

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "configProviderTest.war")
                .addAsLibrary(testJar);
        return war;
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
            String propValue = (String) propEntry.getValue();
            if (propValue != null && propValue.length() > 0) {
                Assert.assertEquals(propValue, config.getValue((String) propEntry.getKey(), String.class));
            }
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

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNonExistingConfigKeyGet() {
        config.getValue("tck.config.test.keydoesnotexist", String.class);
    }

    @Test
    public void testEmptyConfigTreatedAsNotExisting() {
        Assert.assertFalse(config.getOptionalValue("tck.config.test.javaconfig.emptyvalue", String.class).isPresent());
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testEmptyConfigTreatedAsNotExistingGet() {
        config.getValue("tck.config.test.javaconfig.emptyvalue", String.class);
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
