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
 *
 */
package org.eclipse.microprofile.config.tck.converters.convertToNull;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.converters.Pizza;
import org.eclipse.microprofile.config.tck.converters.PizzaConverter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * This is to test that when a converter returns null for some config value, null will be assigned. This means the
 * property still exists but the value is set to null on purpose.
 */
public class ConvertedNullValueTest extends Arquillian {
    private @Inject Config config;
    private @Inject MyBean myBean;

    @Deployment
    public static Archive deployment() {
        return ShrinkWrap
                .create(WebArchive.class, "ConvertedNullValueTest.war")
                .addClasses(ConvertedNullValueTest.class, Pizza.class, PizzaConverter.class, MyBean.class)
                .addAsResource(
                        new StringAsset(
                                "partial.pizza=cheese"),
                        "META-INF/microprofile-config.properties")
                .addAsServiceProvider(Converter.class, PizzaConverter.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Test
    public void testDefaultValueNotUsed() {
        // The converter returns null as the converter returns null if the property does not contain :
        // This will treat as if the property has been removed. The defaultValue will not be used.
        Assert.assertNull(myBean.getPizza());
    }

    @Test
    public void testGetValue() {
        // The converter returns null as the converter returns null if the property does not contain :
        // Therefore, it will be treated as non-existence.
        Assert.assertThrows(NoSuchElementException.class, () -> config.getValue("partial.pizza", Pizza.class));
    }

    @Test
    public void testGetOptionalValue() {
        // The converter returns null as the converter returns null if the property does not contain :
        // Therefore, it will be treated as non-existence.
        Assert.assertFalse(config.getOptionalValue("partial.pizza", Pizza.class).isPresent());
    }

    @ApplicationScoped
    public static class MyBean {

        private @Inject @ConfigProperty(name = "partial.pizza", defaultValue = "medium:chicken") Optional<Pizza> myPizza;

        public Pizza getPizza() {
            return myPizza.isPresent() ? myPizza.get() : null;
        }
    }

}
