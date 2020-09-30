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
 *
 */
package org.eclipse.microprofile.config.tck;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * This is to test that when a converter returns null for some config value, null will be assigned. This means the
 * property still exists but the value is set to null on purpose.
 */
public class ConvertedNullValueTest extends Arquillian {
    private @Inject Config config;
    private @Inject MyBean myBean;

    @Deployment
    public static Archive deployment() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "ConvertedNullValueTest.jar")
                .addClasses(ConvertedNullValueTest.class, Pizza.class, PizzaConverter.class, MyBean.class)
                .addAsManifestResource(
                        new StringAsset(
                                "partial.pizza=cheese"),
                        "microprofile-config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsServiceProvider(Converter.class, PizzaConverter.class)
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "ConvertedNullValueTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Test
    public void testDefaultValue() {
        // This should return null as the converter returns null if the property does not contain :
        Assert.assertNull(myBean.getPizza());

    }

    public void testGetValue() {
        // This should return null as the converter returns null if the property does not contain :
        Assert.assertNull(config.getValue("partial.pizza", Pizza.class));
    }

    @ApplicationScoped
    public static class MyBean {

        private @Inject @ConfigProperty(name = "partial.pizza", defaultValue = "chicken:medium") Pizza myPizza;

        public Pizza getPizza() {
            return myPizza;
        }
    }

}
