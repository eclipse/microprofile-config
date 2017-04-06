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
 *******************************************************************************/
package org.eclipse.microprofile.config.tck;

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.eclipse.microprofile.config.tck.configsources.CustomConfigSourceProvider;
import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
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
 */
public class AutoDiscoveredConfigSourceTest{

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "customConfigSourceTest.jar")
                .addClass(AutoDiscoveredConfigSourceTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(ConfigSource.class, CustomDbConfigSource.class)
                .addAsServiceProvider(ConfigSourceProvider.class, CustomConfigSourceProvider.class)
                .as(JavaArchive.class);

        addFile(testJar, "META-INF/services/org.eclipse.microprfile.config.spi.ConfigSource");
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "customConfigSourceTest.war")
                .addAsLibrary(testJar);
        return war;
    }
    
    @Test
    public void testConfigSourceProvider() {
        Config config = ConfigProviderResolver.instance().getBuilder().addDefaultSources().addDiscoveredSources().build();
        Assert.assertEquals(config.getValue("tck.config.test.customDbConfig.key1", String.class), "valueFromDb1");
    }
}
