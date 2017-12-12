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

package org.eclipse.microprofile.config.tck;

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringCt;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringValueOf;
import org.eclipse.microprofile.config.tck.converters.implicit.SomeEnumToConvert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test the implicit converter handling.
 *
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class ImplicitConverterTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "implicitConverterTest.jar")
            .addPackage(ConvTestTypeWStringCt.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .as(JavaArchive.class);

        addFile(testJar, "META-INF/microprofile-config.properties");

        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "implicitConverterTest.war")
            .addAsLibrary(testJar);
        return war;
    }


    private @Inject Config config;


    @Test
    public void testImplicitConverterStringCt() {
        ConvTestTypeWStringCt value = config.getValue("tck.config.test.javaconfig.converter.implicit.stringCt",
            ConvTestTypeWStringCt.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringCt");
    }

    @Test
    public void testImplicitConverterStringValueOf() {
        ConvTestTypeWStringValueOf value = config.getValue("tck.config.test.javaconfig.converter.implicit.stringValueOf",
            ConvTestTypeWStringValueOf.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringValueOf");
    }

    @Test
    public void testImplicitConverterEnumValueOf() {
        SomeEnumToConvert value = config.getValue("tck.config.test.javaconfig.converter.implicit.enumValueOf",
            SomeEnumToConvert.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value, SomeEnumToConvert.BAZ);
        Assert.assertEquals(value.name(), "BAZ");
    }

}
