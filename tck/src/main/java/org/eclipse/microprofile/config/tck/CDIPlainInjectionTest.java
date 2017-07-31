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
 */

package org.eclipse.microprofile.config.tck;

import static org.eclipse.microprofile.config.tck.matchers.AdditionalMatchers.floatCloseTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.inject.Provider;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.tck.matchers.AdditionalMatchers;
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

    private static final String DEFAULT_PROPERTY_BEAN_KEY = "org.eclipse.microprofile.config.tck.CDIPlainInjectionTest.defaultPropertyBean.configProperty";

    @Deployment
    public static Archive deployment() {
        // this is needed since there's a requirement to validate on start up
        System.setProperty(DEFAULT_PROPERTY_BEAN_KEY, "pathConfigValue");
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(CDIPlainInjectionTest.class, SimpleValuesBean.class, DynamicValuesBean.class,
                        AdditionalMatchers.class, TestConfigSource.class, DefaultPropertyBean.class)
                .addAsServiceProvider(ConfigSource.class, TestConfigSource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void can_inject_simple_values_when_defined() {
        ensure_all_property_values_are_defined();

        SimpleValuesBean bean = getBeanOfType(SimpleValuesBean.class);

        assertThat(bean.stringProperty, is(equalTo("text")));
        assertThat(bean.booleanProperty, is(true));
        assertThat(bean.intProperty, is(equalTo(5)));
        assertThat(bean.longProperty, is(equalTo(10L)));
        assertThat(bean.floatProperty, is(floatCloseTo(10.5f, 0.1f)));
        assertThat(bean.doubleProperty, is(closeTo(11.5, 0.1)));

        assertThat(bean.booleanObjProperty, is(true));
        assertThat(bean.integerProperty, is(equalTo(5)));
        assertThat(bean.longObjProperty, is(equalTo(10L)));
        assertThat(bean.floatObjProperty, is(floatCloseTo(10.5f, 0.1f)));
        assertThat(bean.doubleObjProperty, is(closeTo(11.5, 0.1)));

        assertThat(bean.doublePropertyWithDefaultValue, is(closeTo(3.1415, 0.1)));
    }

    @Test
    public void can_inject_dynamic_values_via_CDI_provider() {
        clear_all_property_values();

        DynamicValuesBean bean = getBeanOfType(DynamicValuesBean.class);

        //X TODO clarify how Provider<T> should behave for missing values assertThat(bean.getIntProperty(), is(nullValue()));

        ensure_all_property_values_are_defined();

        assertThat(bean.getIntProperty(), is(equalTo(5)));
    }

    @Test
    public void can_inject_default_property_path() {
        clear_all_property_values();

        ensure_all_property_values_are_defined();

        DefaultPropertyBean bean = getBeanOfType(DefaultPropertyBean.class);

        assertThat(bean.getConfigProperty(), is(equalTo("pathConfigValue")));
    }

    private void ensure_all_property_values_are_defined() {
        System.setProperty("my.string.property", "text");
        System.setProperty("my.boolean.property", "true");
        System.setProperty("my.int.property", "5");
        System.setProperty("my.long.property", "10");
        System.setProperty("my.float.property", "10.5");
        System.setProperty("my.double.property", "11.5");
        System.setProperty(DEFAULT_PROPERTY_BEAN_KEY, "pathConfigValue");
    }

    private void clear_all_property_values() {
        System.getProperties().remove("my.string.property");
        System.getProperties().remove("my.boolean.property");
        System.getProperties().remove("my.int.property");
        System.getProperties().remove("my.long.property");
        System.getProperties().remove("my.float.property");
        System.getProperties().remove("my.double.property");
        System.getProperties().remove(DEFAULT_PROPERTY_BEAN_KEY);
    }

    private <T> T getBeanOfType(Class<T> beanClass) {
        return CDI.current().select(beanClass).get();
    }

    @Dependent
    public static class SimpleValuesBean {

        @Inject
        @ConfigProperty(name="my.string.property")
        private String stringProperty;

        @Inject
        @ConfigProperty(name="my.boolean.property")
        private Boolean booleanObjProperty;

        @Inject
        @ConfigProperty(name="my.boolean.property")
        private boolean booleanProperty;

        @Inject
        @ConfigProperty(name="my.int.property")
        private Integer integerProperty;

        @Inject
        @ConfigProperty(name="my.int.property")
        private int intProperty;

        @Inject
        @ConfigProperty(name="my.long.property")
        private Long longObjProperty;

        @Inject
        @ConfigProperty(name="my.long.property")
        private long longProperty;

        @Inject
        @ConfigProperty(name="my.float.property")
        private Float floatObjProperty;

        @Inject
        @ConfigProperty(name="my.float.property")
        private float floatProperty;

        @Inject
        @ConfigProperty(name="my.double.property")
        private Double doubleObjProperty;

        @Inject
        @ConfigProperty(name="my.double.property")
        private double doubleProperty;

        // the property is not configured in any ConfigSource but its defaultValue will
        // be used to set the field.
        @Inject
        @ConfigProperty(name="my.not.configured.double.property", defaultValue = "3.1415")
        private Double doublePropertyWithDefaultValue;

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

    @Dependent
    public static class DefaultPropertyBean {
        @Inject
        @ConfigProperty
        private String configProperty;

        public String getConfigProperty() {
            return configProperty;
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
