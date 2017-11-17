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

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWCharSequenceCt;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWCharSequenceParse;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWCharSequenceValueOf;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringCt;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringParse;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringValueOf;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;

/**
 * Test the implicit converter handling.
 *
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class ImplicitConverterTest {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "implicitConverterTest.jar")
            .addPackage(ConvTestTypeWCharSequenceCt.class.getPackage())
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .as(JavaArchive.class);

        addFile(testJar, "META-INF/javaconfig.properties");

        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "customConfigSourceTest.war")
            .addAsLibrary(testJar);
        return war;
    }


    private @Inject Config config;

    @Test
    public void testImplicitConverterCharSequenceCt() {
        ConvTestTypeWCharSequenceCt value = config.getValue("tck.config.test.javaconfig.converter.implicit.charSequenceCt",
            ConvTestTypeWCharSequenceCt.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "charSequenceCt");
    }

    @Test
    public void testImplicitConverterCharSequenceParse() {
        ConvTestTypeWCharSequenceParse value = config.getValue("tck.config.test.javaconfig.converter.implicit.charSequenceParse",
            ConvTestTypeWCharSequenceParse.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "charSequenceParse");
    }

    @Test
    public void testImplicitConverterCharSequenceValueOf() {
        ConvTestTypeWCharSequenceValueOf value = config.getValue("tck.config.test.javaconfig.converter.implicit.charSequenceValueOf",
            ConvTestTypeWCharSequenceValueOf.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "charSequenceValueOf");
    }
    @Test
    public void testImplicitConverterStringCt() {
        ConvTestTypeWStringCt value = config.getValue("tck.config.test.javaconfig.converter.implicit.stringCt",
            ConvTestTypeWStringCt.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringCt");
    }

    @Test
    public void testImplicitConverterStringParse() {
        ConvTestTypeWStringParse value = config.getValue("tck.config.test.javaconfig.converter.implicit.stringParse",
            ConvTestTypeWStringParse.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringParse");
    }

    @Test
    public void testImplicitConverterStringValueOf() {
        ConvTestTypeWStringValueOf value = config.getValue("tck.config.test.javaconfig.converter.implicit.stringValueOf",
            ConvTestTypeWStringValueOf.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringValueOf");
    }
}
