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
package org.eclipse.microprofile.config.tck.base;

import org.eclipse.microprofile.config.tck.configsources.CustomDbConfigSource;
import org.eclipse.microprofile.config.tck.converters.DuckConverter;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class AbstractTest extends Arquillian {

    protected static JavaArchive allIn(String testName) {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, testName)
                .addPackage(CustomDbConfigSource.class.getPackage())
                .addPackage(DuckConverter.class.getPackage())
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        return testJar;
    }

}
