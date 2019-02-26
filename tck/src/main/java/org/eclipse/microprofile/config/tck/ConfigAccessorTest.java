/*
 * Copyright (c) 2016-2019 Contributors to the Eclipse Foundation
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

import java.time.Duration;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigAccessor;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.base.AbstractTest;
import org.eclipse.microprofile.config.tck.configsources.ConfigurableConfigSource;
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
public class ConfigAccessorTest extends Arquillian {

    private @Inject Config config;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
            .create(JavaArchive.class, "configValueTest.jar")
            .addPackage(AbstractTest.class.getPackage())
            .addClass(ConfigAccessorTest.class)
            .addClass(ConfigurableConfigSource.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsServiceProvider(ConfigSource.class, ConfigurableConfigSource.class)
            .as(JavaArchive.class);

        AbstractTest.addFile(testJar, "META-INF/microprofile-config.properties");

        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "configValueTest.war")
            .addAsLibrary(testJar);
        return war;
    }


    @Test
    public void testGetValue() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.key1", String.class).build().getValue(), "value1");
    }

    @Test
    public void testGetValueWithDefault() {
        ConfigAccessor<Integer> cfga = config.access("tck.config.test.javaconfig.configvalue.withdefault.notexisting", Integer.class)
                  .withDefault(Integer.valueOf(1234)).build();

        Assert.assertEquals(cfga.getValue(), Integer.valueOf(1234));
    }

    @Test
    public void testGetValueWithStringDefault() {
        ConfigAccessor<Integer> cfga = config.access("tck.config.test.javaconfig.configvalue.withdefault.notexisting", Integer.class)
                .withStringDefault("1234").build();

        Assert.assertEquals(cfga.getValue(), Integer.valueOf(1234));
    }

    /**
     * Checks whether variable substitution works.
     * The following situation is configured in javaconfig.properties:
     * <pre>
     * tck.config.variable.baseHost = some.host.name
     * tck.config.variable.firstEndpoint = http://${tck.config.variable.baseHost}/endpointOne
     * tck.config.variable.secondEndpoint = http://${tck.config.variable.baseHost}/endpointTwo
     * </pre>
     */
    @Test
    public void testVariableReplacement() {
        Assert.assertEquals(config.access("tck.config.variable.firstEndpoint", String.class).build().getValue(),
                "http://some.host.name/endpointOne");

        Assert.assertEquals(config.access("tck.config.variable.secondEndpoint", String.class).build().getValue(),
                "http://some.host.name/endpointTwo");

        // variables in Config.getValue and getOptionalValue do not get evaluated otoh
        Assert.assertEquals(config.getValue("tck.config.variable.firstEndpoint", String.class),
                "http://${tck.config.variable.baseHost}/endpointOne");

        Assert.assertEquals(config.getOptionalValue("tck.config.variable.firstEndpoint", String.class).get(),
                "http://${tck.config.variable.baseHost}/endpointOne");
    }

    @Test
    public void testIntegerConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.integer", Integer.class).build().getValue(),
            Integer.valueOf(1234));
    }

    @Test
    public void testLongConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.long", Long.class).build().getValue(),
            Long.valueOf(1234567890123456L));
    }

    @Test
    public void testFloatConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.float", Float.class).build().getValue(),
            Float.valueOf(12.34f));
    }

    @Test
    public void testDoubleonverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.double", Double.class).build().getValue(),
            Double.valueOf(12.34567890123456));
    }

    @Test
    public void testBooleanConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.true", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.true_uppercase", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.true_mixedcase", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.false", Boolean.class).build().getValue(),
            Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.one", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.zero",Boolean.class).build().getValue(),
            Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.seventeen", Boolean.class).build().getValue(),
            Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.yes", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.yes_uppercase", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.yes_mixedcase", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.y", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.y_uppercase", Boolean.class).build().getValue(),
            Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.no", Boolean.class).build().getValue(),
            Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.no_mixedcase", Boolean.class).build().getValue(),
            Boolean.FALSE);

    }

    @Test
    public void testCacheFor() throws Exception {
        String key = "tck.config.test.javaconfig.cachefor.key";
        System.setProperty(key, "firstvalue");
        ConfigAccessor<String> val = config.access(key, String.class).cacheFor(Duration.ofMillis(30)).build();
        Assert.assertEquals(val.getValue(), "firstvalue");

        // immediately change the value
        System.setProperty(key, "secondvalue");

        // we should still see the first value, because it is cached!
        Assert.assertEquals(val.getValue(), "firstvalue");

        // but now let's wait a bit
        Thread.sleep(60);
        Assert.assertEquals(val.getValue(), "secondvalue");
    }

    @Test
    public void testOnAttributeChange() {
        String key = "tck.config.test.onattributechange.key";
        ConfigurableConfigSource.configure(config, key, "firstvalue");

        ConfigAccessor<String> val = config.access(key, String.class).cacheFor(Duration.ofMillis(30)).build();
        Assert.assertEquals(val.getValue(), "firstvalue");

        // immediately change the value on the ConfigurableConfigSource that will notify the Config of the change
        ConfigurableConfigSource.configure(config, key, "secondvalue");

        // we should see the new value right now as the ConfigurableConfigSource has notified that its attribute has changed
        Assert.assertEquals(val.getValue(), "secondvalue");
    }

    @Test
    public void testDefaultValue() {
        String key = "tck.config.test.javaconfig.somerandom.default.key";

        ConfigAccessor<String> val = config.access(key, String.class).build();
        Assert.assertNull(val.getDefaultValue());

        ConfigAccessor<String> val2 = config.access(key, String.class).withDefault("abc").build();
        Assert.assertEquals(val2.getDefaultValue(), "abc");
        Assert.assertEquals(val2.getValue(), "abc");

        ConfigAccessor<Integer> vali = config.access(key, Integer.class).withDefault(123).build();
        Assert.assertEquals(vali.getDefaultValue(), Integer.valueOf(123));
        Assert.assertEquals(vali.getValue(), Integer.valueOf(123));

        ConfigAccessor<Integer> vali2 = config.access(key, Integer.class).withStringDefault("123").build();
        Assert.assertEquals(vali2.getDefaultValue(), Integer.valueOf(123));
        Assert.assertEquals(vali2.getValue(), Integer.valueOf(123));

        System.setProperty(key, "666");
        Assert.assertEquals(vali2.getDefaultValue(), Integer.valueOf(123));
        Assert.assertEquals(vali2.getValue(), Integer.valueOf(666));


    }
}
