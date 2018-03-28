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

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verify injection of {@code Optional<T>} fields.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class CdiOptionalInjectionTest extends Arquillian {

    private @Inject OptionalValuesBean optionalValuesBean;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "cdiOptionalInjectionTest.jar")
                .addClasses(CdiOptionalInjectionTest.class, OptionalValuesBean.class)
                .addAsManifestResource(new StringAsset("my.optional.int.property=1234\nmy.optional.string.property=hello"),
                        "microprofile-config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "cdiOptionalInjectionTest.war")
                .addAsLibrary(testJar);
        return war;
    }


    @Test
    public void testOptionalInjection() {
        Assert.assertTrue(optionalValuesBean.getIntProperty().isPresent());
        Assert.assertEquals(optionalValuesBean.getIntProperty().get(), Integer.valueOf(1234));

        Assert.assertFalse(optionalValuesBean.getNotexistingProperty().isPresent());

        Assert.assertTrue(optionalValuesBean.getStringValue().isPresent());
        Assert.assertEquals(optionalValuesBean.getStringValue().get(), "hello");

        Assert.assertFalse(optionalValuesBean.getNotExistingStringProperty().isPresent());
    }
}
