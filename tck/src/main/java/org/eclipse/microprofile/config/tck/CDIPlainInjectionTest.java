/*
 * Copyright (c) 2016-2017 Payara Services Ltd. and others
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

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import static org.eclipse.microprofile.config.tck.matchers.AdditionalMatchers.floatCloseTo;
import static org.eclipse.microprofile.config.tck.testsupport.TestSetup.getBeanOfType;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.testsupport.TestSetup;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Test cases for CDI-based API that test retrieving values from the configuration. 
 * The tests depend only on CDI 1.2. 
 * @author Ondrej Mihalyi
 */
public class CDIPlainInjectionTest extends Arquillian {

    @Deployment
    public static Archive deployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CDIPlainInjectionTest.class, SimpleValuesBean.class, DynamicValuesBean.class)
                .addAsServiceProvider(ConfigSource.class, TestConfigSource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void can_inject_simple_values_when_defined() {
        ensure_all_property_values_are_defined();

        SimpleValuesBean bean = getBeanOfType(SimpleValuesBean.class);

        assertThat(bean.stringProperty, is(equalTo("text")));
        assertThat(bean.boolProperty, is(true));
        assertThat(bean.intProperty, is(equalTo(5)));
        assertThat(bean.longProperty, is(equalTo(10L)));
        assertThat(bean.floatProperty, is(floatCloseTo(10.5f, 0.1f)));
        assertThat(bean.doubleProperty, is(closeTo(11.5, 0.1)));
    }

    @Test
    public void can_inject_dynamic_values_via_CDI_provider() {
        clear_all_property_values();

        DynamicValuesBean bean = getBeanOfType(DynamicValuesBean.class);

        //X TODO clarify how Provider<T> should behave for missing values assertThat(bean.getIntProperty(), is(nullValue()));

        ensure_all_property_values_are_defined();

        assertThat(bean.getIntProperty(), is(equalTo(5)));
    }
    
    private void ensure_all_property_values_are_defined() {
        TestSetup.ensurePropertyDefined("my.string.property", "text");
        TestSetup.ensurePropertyDefined("my.boolean.property", "true");
        TestSetup.ensurePropertyDefined("my.int.property", "5");
        TestSetup.ensurePropertyDefined("my.long.property", "10");
        TestSetup.ensurePropertyDefined("my.float.property", "10.5");
        TestSetup.ensurePropertyDefined("my.double.property", "11.5");
    }

    private void clear_all_property_values() {
        TestSetup.ensurePropertyUndefined("my.string.property");
        TestSetup.ensurePropertyUndefined("my.boolean.property");
        TestSetup.ensurePropertyUndefined("my.int.property");
        TestSetup.ensurePropertyUndefined("my.long.property");
        TestSetup.ensurePropertyUndefined("my.float.property");
        TestSetup.ensurePropertyUndefined("my.double.property");
    }

    @Dependent
    public static class SimpleValuesBean {

        @Inject
        @ConfigProperty(name="my.string.property")
        private String stringProperty;

        @Inject
        @ConfigProperty(name="my.boolean.property")
        private Boolean boolProperty;

        @Inject
        @ConfigProperty(name="my.int.property")
        private Integer intProperty;

        @Inject
        @ConfigProperty(name="my.long.property")
        private Long longProperty;

        @Inject
        @ConfigProperty(name="my.float.property")
        private Float floatProperty;

        @Inject
        @ConfigProperty(name="my.double.property")
        private Double doubleProperty;

    }    
    
    @Dependent
    public static class DynamicValuesBean {

        @Inject
        @ConfigProperty(name="my.int.property")
        private Provider<Integer> intPropertyProvider;

        public Integer getIntProperty() {
            return intPropertyProvider.get();
        }

    }
    
    public static class TestConfigSource implements ConfigSource {

        private Map<String, String> properties;

        public TestConfigSource() {
            properties = new HashMap<>();
            properties.put("my.string.property", "text");
            properties.put("my.boolean.property", "true");
            properties.put("my.int.property", "5");
            properties.put("my.long.property", "10");
            properties.put("my.float.property", "10.5");
            properties.put("my.double.property", "11.5");
        }

        @Override
        public Map<String, String> getProperties() {
            return properties;
        }

        @Override
        public String getValue(String propertyName) {
            return properties.get(propertyName);
        }

        @Override
        public String getName() {
            return this.getClass().getName();
        }
    }
}
