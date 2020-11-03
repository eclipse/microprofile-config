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

import javax.enterprise.inject.spi.DeploymentException;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;
import org.eclipse.microprofile.config.tck.converters.Pizza;
import org.eclipse.microprofile.config.tck.converters.PizzaConverter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * This is to test that when a converter returns null for some config value, an exception should be thrown and the
 * defaultValue should not be used.
 */
public class ConvertedNullValueBrokenInjectionTest extends Arquillian {
    private @Inject Config config;
    private @Inject ConvertedNullValueBrokenInjectionBean myBean;

    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static Archive deployment() {
        return ShrinkWrap.create(WebArchive.class, "ConvertedNullValueBrokenInjectionTest.war")
                .addClasses(ConvertedNullValueBrokenInjectionTest.class, Pizza.class, PizzaConverter.class,
                        ConvertedNullValueBrokenInjectionBean.class)
                .addAsResource(
                        new StringAsset(
                                "partial.pizza=cheese"),
                        "META-INF/microprofile-config.properties")
                .addAsServiceProvider(Converter.class, PizzaConverter.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    }

    @Test
    public void test() {
    }

}
