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
package org.eclipse.microprofile.config.tck;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.ConfigValue;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class ConfigValueTest {

    private Config config = ConfigProvider.getConfig();

    @Test
    public void testget() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.key1").get(), "value1");
    }


    @Test
    public void testIntegerConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.integer").as(Integer.class).get(), Integer.valueOf(1234));
    }

    @Test
    public void testLongConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.long").as(Long.class).get(), Long.valueOf(1234567890123456L));
    }

    @Test
    public void testFloatConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.float").as(Float.class).get(), Float.valueOf(12.34f));
    }

    @Test
    public void testDoubleonverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.double").as(Double.class).get(), Double.valueOf(12.34567890123456));
    }

    @Test
    public void testBooleanConverter() {
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.true").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.true_uppercase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.true_mixedcase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.false").as(Boolean.class).get(), Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.one").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.zero").as(Boolean.class).get(), Boolean.FALSE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.seventeen").as(Boolean.class).get(), Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.yes").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.yes_uppercase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.yes_mixedcase").as(Boolean.class).get(), Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.y").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.y_uppercase").as(Boolean.class).get(), Boolean.TRUE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.ja").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.ja_uppercase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.ja_mixedcase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.no_mixedcase").as(Boolean.class).get(), Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.j").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.j_uppercase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.n_uppercase").as(Boolean.class).get(), Boolean.FALSE);

        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.oui").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.oui_uppercase").as(Boolean.class).get(), Boolean.TRUE);
        Assert.assertEquals(config.access("tck.config.test.javaconfig.configvalue.boolean.oui_mixedcase").as(Boolean.class).get(), Boolean.TRUE);
    }

    @Test
    public void testCacheFor() throws Exception {
        String key = "tck.config.cachefor.key";
        System.setProperty(key, "firstvalue");
        ConfigValue<String> val = config.access(key).cacheFor(30, TimeUnit.MILLISECONDS);
        Assert.assertEquals(val.get(), "firstvalue");

        // immediately change the value
        System.setProperty(key, "secondvalue");

        // we should still see the first value, because it is cached!
        Assert.assertEquals(val.get(), "firstvalue");

        // but now let's wait a bit
        Thread.sleep(40);
        Assert.assertEquals(val.get(), "secondvalue");
    }

    @Test
    public void testWithVariable() throws Exception {
        ConfigValue<String> val = config.access("tck.config.test.javaconfig.configvalue.withvariable.key").evaluateVariables(true);
        Assert.assertEquals(val.get(), "This key needs the perfect value!");
    }

    @Test
    public void testNotExistingVariable() {
        Assert.assertFalse(config.access("this.key.does.not.exist").getOptional().isPresent());
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNotExistingVariableGet() {
        config.access("this.key.does.not.exist").get();
    }

    @Test
    public void testOnValueChange() {
        String key = "some.sysprop.key";
        System.setProperty(key, "A");
        final List<String> changes = new ArrayList<>();

        ConfigValue.ConfigChanged listener = new ConfigValue.ConfigChanged() {
            @Override
            public <T> void onValueChange(String key, T oldValue, T newValue) {
                changes.add(key + ":" + oldValue + "->" + newValue);
            }
        };

        ConfigValue<String> val = config.access(key).onChange(listener);

        Assert.assertEquals("A", val.get());

        // now set a new value
        System.setProperty(key, "B");
        Assert.assertEquals("B", val.get());

        // now let's check the changes:
        Assert.assertEquals(2, changes.size());
        Assert.assertEquals(key + ":null->A", changes.get(0));
        Assert.assertEquals(key + ":A->B", changes.get(1));
    }

    @Test
    public void testStringValueList() {
        List<String> valueList = config.access("tck.config.test.javaconfig.valuelist").getValueList();

        Assert.assertNotNull(valueList);
        Assert.assertEquals(valueList.size(), 6);
        Assert.assertEquals(valueList.get(0), "some");
        Assert.assertEquals(valueList.get(1), "value");
        Assert.assertEquals(valueList.get(2), "with");
        Assert.assertEquals(valueList.get(3), "es\\caping");
        Assert.assertEquals(valueList.get(4), "and,comma");
        Assert.assertEquals(valueList.get(5), "also");
    }
}
