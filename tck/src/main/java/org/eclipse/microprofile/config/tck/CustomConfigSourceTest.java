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

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.eclipse.microprofile.config.tck.configsources.CustomConfigSourceProvider;
import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class CustomConfigSourceTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "customConfigSourceTest.jar")
                .addClass(CustomConfigSourceTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(ConfigSource.class, CustomDbConfigSource.class)
                .addAsServiceProvider(ConfigSourceProvider.class, CustomConfigSourceProvider.class)
                .as(JavaArchive.class);

        addFile(testJar, "META-INF/microprofile-config.properties");

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "customConfigSourceTest.war")
                .addAsLibrary(testJar);
        return war;
    }


    @Test
    public void testConfigSourceProvider() {
        Assert.assertEquals(config.getValue("tck.config.test.customDbConfig.key1", String.class), "valueFromDb1");
    }
}
