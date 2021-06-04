/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;

import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

public class CDIPropertyExpressionsTest extends Arquillian {
    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap
                .create(WebArchive.class, "CDIPropertyExpressionsTest.war")
                .addClasses(PropertyExpressionBean.class)
                .addAsServiceProvider(ConfigSource.class, PropertyExpressionConfigSource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    PropertyExpressionBean propertyExpressionBean;

    @Test
    public void expression() {
        assertEquals(propertyExpressionBean.expression, "1234");

        assertEquals(propertyExpressionBean.expressionConfigValue.getValue(), "1234");
        assertEquals(propertyExpressionBean.expressionConfigValue.getRawValue(), "${expression}");
    }

    @Test
    public void expressionNoDefault() {
        assertEquals(propertyExpressionBean.expressionDefault, "${expression}");
    }

    @Test
    public void badExpansion() {
        assertFalse(propertyExpressionBean.badExpansion.isPresent());
        assertFalse(propertyExpressionBean.badExpansionInt.isPresent());
        assertFalse(propertyExpressionBean.badExpansionDouble.isPresent());
        assertFalse(propertyExpressionBean.badExpansionLong.isPresent());

        assertNotNull(propertyExpressionBean.badExpansionConfigValue);
        assertEquals(propertyExpressionBean.badExpansionConfigValue.getName(), "expression");
        assertNull(propertyExpressionBean.badExpansionConfigValue.getValue());
        assertNull(propertyExpressionBean.badExpansionConfigValue.getSourceName());
        assertEquals(propertyExpressionBean.badExpansionConfigValue.getSourceOrdinal(), 0);
    }

    @Dependent
    public static class PropertyExpressionBean {
        @Inject
        @ConfigProperty(name = "my.prop")
        String expression;
        @Inject
        @ConfigProperty(name = "my.prop")
        ConfigValue expressionConfigValue;
        @Inject
        @ConfigProperty(name = "another.prop", defaultValue = "${expression}")
        String expressionDefault;
        @ConfigProperty(name = "bad.property.expression.prop")
        Optional<String> badExpansion;
        @Inject
        @ConfigProperty(name = "bad.property.expression.prop")
        OptionalInt badExpansionInt;
        @Inject
        @ConfigProperty(name = "bad.property.expression.prop")
        OptionalDouble badExpansionDouble;
        @Inject
        @ConfigProperty(name = "bad.property.expression.prop")
        OptionalLong badExpansionLong;
        @ConfigProperty(name = "bad.property.expression.prop")
        ConfigValue badExpansionConfigValue;
    }

    public static class PropertyExpressionConfigSource implements ConfigSource {
        private Map<String, String> properties = new HashMap<>();

        public PropertyExpressionConfigSource() {
            properties.put("my.prop", "${expression}");
            properties.put("expression", "1234");
            properties.put("bad.property.expression.prop", "${missing.prop}");
        }

        @Override
        public Set<String> getPropertyNames() {
            return properties.keySet();
        }

        @Override
        public String getValue(final String propertyName) {
            return properties.get(propertyName);
        }

        @Override
        public String getName() {
            return this.getClass().getName();
        }
    }
}
