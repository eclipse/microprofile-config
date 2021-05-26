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

import java.util.List;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class ClassConverterBean {
    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.class")
    private Class testClass;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.class.array")
    private Class[] testClasses;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.class.array")
    private Set<Class> testClassSet;

    @Inject
    @ConfigProperty(name = "tck.config.test.javaconfig.converter.class.array")
    private List<Class> testClassList;

    public Class getTestClass() {
        return testClass;
    }

    public Class[] getTestClasses() {
        return testClasses;
    }

    public Set<Class> getTestClassSet() {
        return testClassSet;
    }

    public List<Class> getTestClassList() {
        return testClassList;
    }
}
