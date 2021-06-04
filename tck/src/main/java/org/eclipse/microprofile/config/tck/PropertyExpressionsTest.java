/*
 * Copyright (c) 2016-2020 Contributors to the Eclipse Foundation
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

import static java.util.stream.Collectors.toList;
import static org.eclipse.microprofile.config.Config.PROPERTY_EXPRESSIONS_ENABLED;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class PropertyExpressionsTest extends Arquillian {

    @Deployment
    public static WebArchive deployment() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "PropertyExpressionsTest.jar")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        return ShrinkWrap
                .create(WebArchive.class, "PropertyExpressionsTest.war")
                .addAsLibrary(testJar);
    }

    private List<Config> builtConfigs = new ArrayList<>();

    @AfterMethod
    public void tearDown() {
        for (Config config : builtConfigs) {
            ConfigProviderResolver.instance().releaseConfig(config);
        }
        builtConfigs.clear();
    }

    @Test
    public void simpleExpression() {
        Config config = buildConfig("my.prop", "1234", "expression", "${my.prop}");

        assertEquals("1234", config.getValue("expression", String.class));
    }

    @Test
    public void multipleExpressions() {
        Config config = buildConfig("my.prop", "1234", "expression", "${my.prop}${my.prop}");

        assertEquals("12341234", config.getValue("expression", String.class));
    }

    @Test
    public void composedExpressions() {
        Config config = buildConfig("my.prop", "1234", "expression", "${${compose}}", "compose", "my.prop");

        assertEquals("1234", config.getValue("expression", String.class));
    }

    @Test
    public void defaultExpression() {
        Config config = buildConfig("expression", "${my.prop:1234}");

        assertEquals("1234", config.getValue("expression", String.class));
    }

    @Test
    public void defaultExpressionEmpty() {
        Config config = buildConfig("expression", "12${my.prop:}34");

        assertEquals("1234", config.getValue("expression", String.class));
    }

    @Test
    public void defaultExpressionComposed() {
        Config config = buildConfig("expression", "${my.prop:${compose}}", "compose", "1234");

        assertEquals("1234", config.getValue("expression", String.class));
    }

    @Test
    public void defaultExpressionComposedEmpty() {
        Config config = buildConfig("expression", "${my.prop:${compose:}}", "my.prop", "1234");

        assertEquals("1234", config.getValue("expression", String.class));
    }

    @Test
    public void noExpression() {
        Config config = buildConfig("expression", "${my.prop}");

        assertThrows(NoSuchElementException.class, () -> config.getValue("expression", String.class));
    }

    @Test
    void noExpressionButOptional() {
        Config config = buildConfig("expression", "${my.prop}");

        assertEquals(Optional.empty(), config.getOptionalValue("expression", String.class));
    }

    @Test
    void noExpressionButConfigValue() {
        Config config = buildConfig("expression", "${my.prop}");

        ConfigValue configValue = config.getConfigValue("expression");
        assertNotNull(configValue);
        assertEquals(configValue.getName(), "expression");
        assertNull(configValue.getValue());
        assertNull(configValue.getSourceName());
        assertEquals(configValue.getSourceOrdinal(), 0);
    }

    @Test
    public void noExpressionComposed() {
        Config config = buildConfig("expression", "${my.prop${compose}}");

        assertThrows(NoSuchElementException.class, () -> config.getValue("expression", String.class));
    }

    @Test
    void noExpressionComposedButOptional() {
        Config config = buildConfig("expression", "${my.prop${compose}}");

        assertEquals(Optional.empty(), config.getOptionalValue("expression", String.class));
    }

    @Test
    void noExpressionComposedButConfigValue() {
        Config config = buildConfig("expression", "${my.prop${compose}}");

        ConfigValue configValue = config.getConfigValue("expression");
        assertNotNull(configValue);
        assertEquals(configValue.getName(), "expression");
        assertNull(configValue.getValue());
        assertNull(configValue.getSourceName());
        assertEquals(configValue.getSourceOrdinal(), 0);
    }

    @Test
    public void multipleExpansions() {
        Config config = buildConfig("my.prop", "1234", "my.prop.two", "${my.prop}", "my.prop.three",
                "${my.prop.two}", "my.prop.four", "${my.prop.three}");

        assertEquals("1234", config.getValue("my.prop", String.class));
        assertEquals("1234", config.getValue("my.prop.two", String.class));
        assertEquals("1234", config.getValue("my.prop.three", String.class));
        assertEquals("1234", config.getValue("my.prop.four", String.class));
    }

    @Test
    public void infiniteExpansion() {
        Config config = buildConfig("my.prop", "${my.prop}");

        assertThrows(IllegalArgumentException.class, () -> config.getValue("my.prop", String.class));
    }

    @Test
    public void withoutExpansion() {
        Config config =
                buildConfig("my.prop", "1234", "expression", "${my.prop}", PROPERTY_EXPRESSIONS_ENABLED, "false");

        assertEquals("${my.prop}", config.getValue("expression", String.class));
    }

    @Test
    public void escape() {
        assertEquals("${my.prop}", buildConfig("expression", "\\${my.prop}").getValue("expression", String.class));
    }

    @Test
    void arrayEscapes() {
        Config config = buildConfig("list", "cat,dog,${mouse},sea\\,turtle", "mouse", "mouse");

        final List<String> list = config.getValues("list", String.class);
        assertEquals(4, list.size());
        assertEquals(list, Stream.of("cat", "dog", "mouse", "sea,turtle").collect(toList()));
    }

    @Test
    void escapeBraces() {
        Config config = buildConfig("my.prop", "${value:111{111}");
        assertEquals("111{111", config.getValue("my.prop", String.class));
    }

    @Test
    void expressionMissing() {
        Config config = buildConfig("my.prop", "${expression}", "my.prop.partial", "${expression}partial");
        assertThrows(Exception.class, () -> config.getValue("my.prop", String.class));
        assertThrows(Exception.class, () -> config.getValue("my.prop.partial", String.class));
    }

    private Config buildConfig(String... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("keyValues array must be a multiple of 2");
        }

        Map<String, String> properties = new HashMap<>();
        for (int i = 0; i < keyValues.length; i += 2) {
            properties.put(keyValues[i], keyValues[i + 1]);
        }

        Config result = ConfigProviderResolver.instance().getBuilder()
                .withSources(new ConfigSource() {
                    @Override
                    public Set<String> getPropertyNames() {
                        return properties.keySet();
                    }

                    @Override
                    public String getValue(String propertyName) {
                        return properties.get(propertyName);
                    }

                    @Override
                    public String getName() {
                        return "test";
                    }
                })
                .build();

        builtConfigs.add(result);
        return result;
    }
}
