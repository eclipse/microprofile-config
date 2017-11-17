/*
 *******************************************************************************
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
 *******************************************************************************/
package org.eclipse.microprofile.config.tck;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.eclipse.microprofile.config.tck.converters.Pizza;
import org.eclipse.microprofile.config.tck.converters.PizzaConverter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verify the method addDiscoveredSources() on ConfigBuilder.
 *
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class AutoDiscoveredConfigSourceTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "customConfigSourceTest.jar")
                .addClasses(AutoDiscoveredConfigSourceTest.class, CustomDbConfigSource.class, Pizza.class, PizzaConverter.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(ConfigSource.class, CustomDbConfigSource.class)
                .addAsServiceProvider(Converter.class, PizzaConverter.class)
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "customConfigSourceTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Test
    public void testAutoDiscoveredConfigureSources() {
        Config config = ConfigProviderResolver.instance().getBuilder().addDefaultSources().addDiscoveredSources().build();
        Assert.assertEquals(config.getValue("tck.config.test.customDbConfig.key1", String.class), "valueFromDb1");
    }
    @Test
    public void testAutoDiscoveredConverterManuallyAdded() {

        Config config = ConfigProviderResolver.instance().getBuilder().addDefaultSources().addDiscoveredSources().addDiscoveredConverters().build();
        Pizza dVaule = config.getValue("tck.config.test.customDbConfig.key3", Pizza.class);
        Assert.assertEquals(dVaule.getSize(), "big");
        Assert.assertEquals(dVaule.getFlavor(), "cheese");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAutoDiscoveredConverterNotAddedAutomatically() {
        Config config = ConfigProviderResolver.instance().getBuilder().addDefaultSources().addDiscoveredSources().build();
        Pizza dVaule = config.getValue("tck.config.test.customDbConfig.key3", Pizza.class);
        Assert.fail("The auto discovered converter should not be added automatically.");
    }
}
