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
package org.eclipse.microprofile.config.tck.broken;

import java.util.NoSuchElementException;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.DeploymentException;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Verify the support {@code ConfigProperties}.
 *
 * @author <a href="mailto:emijiang6@googlemail.com">Emily Jiang</a>
 */
public class ConfigPropertiesMissingPropertyPLTest extends Arquillian {
        
    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "ConfigPropertiesTest.jar")
                .addClasses(ConfigPropertiesMissingPropertyPLTest.class, BeanOne.class)
                .addAsManifestResource(
                    new StringAsset(
                        "customer.name=Bob\n" +
                        "customer.age=24\n" 
                        ),
                        "microprofile-config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "ConfigPropertiesTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Test
    @ShouldThrowException(NoSuchElementException.class)
    public void test() {  
        Config config = ConfigProvider.getConfig();
        BeanOne beanOne = config.getConfigProperties(BeanOne.class); //should throw exception
    }

    @ConfigProperties(prefix="customer.")
    @Dependent
    public static class BeanOne {
        private String name;
        public int age;
        public String nationality; //no corresponding config property customer.nationality exists
       
        /**
         * @return String return the name
         */
        public String getName() {
            return name;
        }
    }
}