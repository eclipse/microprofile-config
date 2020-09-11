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
package org.eclipse.microprofile.config.tck.EmptyValue;

import javax.enterprise.inject.spi.DeploymentException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import javax.inject.Inject;

public class EmptyValuesTest extends Arquillian {

    private static final String EMPTY_PROPERTY = "my.empty.property";
    private static final String PROP_FILE_EMPTY_PROPERTY = "my.empty.property.in.config.file";
    public static final StringAsset EMPTY_STRING_ASSET = new StringAsset(PROP_FILE_EMPTY_PROPERTY + "=");

    @Deployment
    @ShouldThrowException(DeploymentException.class)
    public static Archive deployment() {
        JavaArchive jar = ShrinkWrap.create(JavaArchive.class, "emptyValues.jar")
                .addClasses(EmptyValuesTest.class, EmptyValuesBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(EMPTY_STRING_ASSET, "microprofile-config.properties");

        return ShrinkWrap.create(WebArchive.class)
                .addAsLibrary(jar)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    private EmptyValuesBean emptyValuesBean;

    @Test
    public void test() {
    }
}
