/*
 * Copyright (c) 2016-2018 Contributors to the Eclipse Foundation
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

import static org.eclipse.microprofile.config.tck.base.AbstractTest.addFile;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedHashSet;

import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Test Class type converter handling
 *
 * @author John D. Ament
 */
public class ClassConverterTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        String archiveName = ClassConverterTest.class.getSimpleName();
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, archiveName+".jar")
                .addClasses(ClassConverterBean.class, ClassConverterTest.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

            addFile(testJar, "META-INF/microprofile-config.properties");

            WebArchive war = ShrinkWrap
                .create(WebArchive.class, archiveName+".war")
                .addAsLibrary(testJar);
            return war;
    }

    @Inject
    private Config config;

    @Inject
    private ClassConverterBean classConverterBean;

    @Test
    public void testClassConverterWithLookup() {
        Class<?> testClass = config.getValue("tck.config.test.javaconfig.converter.class", Class.class);
        assertEquals(testClass, ClassConverterTest.class);
        Class<?>[] testClasses = config.getValue("tck.config.test.javaconfig.converter.class.array", Class[].class);
        assertEquals(testClasses.length, 2);
        assertEquals(testClasses, new Class[]{ClassConverterTest.class, String.class});
    }

    @Test
    public void testConverterForClassLoadedInBean() {
        assertEquals(classConverterBean.getTestClass(), ClassConverterTest.class);
        assertEquals(classConverterBean.getTestClasses().length, 2);
        assertEquals(classConverterBean.getTestClasses(), new Class[]{ClassConverterTest.class, String.class});
        assertEquals(classConverterBean.getTestClassSet().size(), 2);
        assertEquals(classConverterBean.getTestClassSet(), new LinkedHashSet<>(Arrays.asList(ClassConverterTest.class, String.class)));
        assertEquals(classConverterBean.getTestClassList().size(), 2);
        assertEquals(classConverterBean.getTestClassList(), Arrays.asList(ClassConverterTest.class, String.class));
    }
}
