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
package org.eclipse.microprofile.config.tck.broken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.DeploymentException;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
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
 * Verify that a Convertr exists which can handle the configured string.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class MissingConverterOnInstanceInjectionTest extends Arquillian {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "missingConverterOnInstanceInjectionTest.jar")
                .addClass(ConfigOwner.class)
                .addClass(CustomType.class)
                .addAsManifestResource(new StringAsset("my.customtype.value=xxxxx"), "microprofile-config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "missingConverterOnInstanceInjectionTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Test
    public void test() {
    }


    @RequestScoped
    public static class ConfigOwner {

        @Inject
        @ConfigProperty(name="my.customtype.value")
        private CustomType configValue;
    }

    public static class CustomType {

    }
}
