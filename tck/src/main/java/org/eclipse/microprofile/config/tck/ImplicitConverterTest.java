/*
 * Copyright (c) 2016-2018 Contributors to the Eclipse Foundation
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

import java.time.YearMonth;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestSequenceOfBeforeValueOf;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestSequenceParseBeforeConstructor;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestSequenceValueOfBeforeParse;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWCharSequenceParse;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringCt;
import org.eclipse.microprofile.config.tck.converters.implicit.ConvTestTypeWStringOf;
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
 * @author <a href="mailto:jmesnil@redhat.com">Jeff Mesnil</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * 
 */
public class ImplicitConverterTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "implicitConverterTest.jar")
            .addPackage(ConvTestTypeWStringCt.class.getPackage())
            .addClasses(ParseConverterInjection.class, ImplicitConverterTest.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .as(JavaArchive.class);

        addFile(testJar, "META-INF/microprofile-config.properties");

        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "implicitConverterTest.war")
            .addAsLibrary(testJar);
        return war;
    }


    private @Inject Config config;
    private @Inject ParseConverterInjection parserConverterInjection;
    

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
    public void testImplicitConverterStringOf() {
        ConvTestTypeWStringOf value = config.getValue("tck.config.test.javaconfig.converter.implicit.stringOf",
            ConvTestTypeWStringOf.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringOf");
    }
    
    @Test
    public void testImplicitConverterCharSequenceParse() {
        ConvTestTypeWCharSequenceParse value = config.getValue("tck.config.test.javaconfig.converter.implicit.charSequenceParse", 
             ConvTestTypeWCharSequenceParse.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "charSequenceParse");
    }
    @Test
    public void testImplicitConverterCharSequenceParseJavaTime() {
        YearMonth value = config.getValue("tck.config.test.javaconfig.converter.implicit.charSequenceParse.yearmonth", 
             YearMonth.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value, YearMonth.parse("2017-12"));
    }
    
    @Test
    public void testImplicitConverterCharSequenceParseJavaTimeInjection() {
        Assert.assertNotNull(parserConverterInjection.getYearMonth());
        Assert.assertEquals(parserConverterInjection.getYearMonth(), YearMonth.parse("2017-12"));
    }

    @Test
    public void testImplicitConverterEnumValueOf() {
        SomeEnumToConvert value = config.getValue("tck.config.test.javaconfig.converter.implicit.enumValueOf",
            SomeEnumToConvert.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value, SomeEnumToConvert.BAZ);
        Assert.assertEquals(value.name(), "BAZ");
    }

    @Test
    public void testImplicitConverterSquenceOfBeforeValueOf() {
        ConvTestSequenceOfBeforeValueOf value = config.getValue
        ("tck.config.test.javaconfig.converter.implicit.sequence.ofBeforeValueOf",
        ConvTestSequenceOfBeforeValueOf.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "ofBeforeValueOf");
    }

    @Test
    public void testImplicitConverterSquenceValueOfBeforeParse() {
        ConvTestSequenceValueOfBeforeParse value = config.getValue
        ("tck.config.test.javaconfig.converter.implicit.sequence.valueOfBeforeParse",
        ConvTestSequenceValueOfBeforeParse.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "valueOfBeforeParse");
    }
    @Test
    public void testImplicitConverterSquenceParseBeforeConstructor() {
        ConvTestSequenceParseBeforeConstructor value = config.getValue
        ("tck.config.test.javaconfig.converter.implicit.sequence.parseBeforeConstructor",
        ConvTestSequenceParseBeforeConstructor.class);
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "parseBeforeConstructor");
    }

    @Test
    public void testGetImplicitConverterStringCtConverter() {
        ConvTestTypeWStringCt value = config.getConverter(ConvTestTypeWStringCt.class).get().convert("stringCt");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringCt");
    }

    @Test
    public void testGetImplicitConverterStringValueOfConverter() {
        ConvTestTypeWStringValueOf value = config.getConverter(ConvTestTypeWStringValueOf.class).get()
            .convert("stringValueOf");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringValueOf");
    }
    @Test
    public void testGetImplicitConverterStringOfConverter() {
        ConvTestTypeWStringOf value = config.getConverter(ConvTestTypeWStringOf.class).get().convert("stringOf");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "stringOf");
    }
    
    @Test
    public void testGetImplicitConverterCharSequenceParseConverter() {
        ConvTestTypeWCharSequenceParse value = config.getConverter(ConvTestTypeWCharSequenceParse.class).get()
            .convert("charSequenceParse");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "charSequenceParse");
    }
    @Test
    public void testGetImplicitConverterCharSequenceParseJavaTimeConverter() {
        YearMonth value = config.getConverter(YearMonth.class).get().convert("2017-12");
        Assert.assertNotNull(value);
        Assert.assertEquals(value, YearMonth.parse("2017-12"));
    }
    
    @Test
    public void testGetImplicitConverterEnumValueOfConverter() {
        SomeEnumToConvert value = config.getConverter(SomeEnumToConvert.class).get().convert("BAZ");
        Assert.assertNotNull(value);
        Assert.assertEquals(value, SomeEnumToConvert.BAZ);
        Assert.assertEquals(value.name(), "BAZ");
    }

    @Test
    public void testGetImplicitConverterSquenceOfBeforeValueOfConverter() {
        ConvTestSequenceOfBeforeValueOf value = config.getConverter(ConvTestSequenceOfBeforeValueOf.class).get()
            .convert("ofBeforeValueOf");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "ofBeforeValueOf");
    }

    @Test
    public void testGetImplicitConverterSquenceValueOfBeforeParseConverter() {
        ConvTestSequenceValueOfBeforeParse value = config.getConverter(ConvTestSequenceValueOfBeforeParse.class).get()
            .convert("valueOfBeforeParse");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "valueOfBeforeParse");
    }
    @Test
    public void testGetImplicitConverterSquenceParseBeforeConstructorConverter() {
        ConvTestSequenceParseBeforeConstructor value = config.getConverter(ConvTestSequenceParseBeforeConstructor.class).get()
            .convert("parseBeforeConstructor");
        Assert.assertNotNull(value);
        Assert.assertEquals(value.getVal(), "parseBeforeConstructor");
    }
}
