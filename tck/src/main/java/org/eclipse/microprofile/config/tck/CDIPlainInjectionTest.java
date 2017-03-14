/*
 * Copyright (c) 2016-2017 Payara Services Ltd. and others
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

package org.eclipse.microprofile.config.tck;

import io.microprofile.config.inject.ConfigProperty;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.eclipse.microprofile.config.ConfigValue;
import static org.eclipse.microprofile.config.tck.testsupport.TestSetup.ensure_property_defined;
import static org.eclipse.microprofile.config.tck.testsupport.TestSetup.ensure_property_undefined;
import static org.eclipse.microprofile.config.tck.testsupport.TestSetup.get_bean_of_type;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test cases for CDI-based API that test retrieving values from the configuration. 
 * The tests depend only on CDI 1.2. 
 * @author Ondrej Mihalyi
 */
@RunWith(Arquillian.class)
public class CDIPlainInjectionTest {

    @Deployment
    public static Archive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void can_inject_dynamic_values_via_ConfigValue() {
        clear_all_property_values();

        DynamicValuesBean bean = get_bean_of_type(DynamicValuesBean.class);
        ConfigValue<Integer> intPropertyValue = bean.getIntPropertyValue();

        assertThat(intPropertyValue.isValueAvailable(), is(false));

        ensure_all_property_values_are_defined();

        assertThat(intPropertyValue.isValueAvailable(), is(true));
        assertThat(intPropertyValue.getValue(), is(equalTo(5)));
    }

    private void ensure_all_property_values_are_defined() {
        ensure_property_defined("my.string.property", "text");
        ensure_property_defined("my.boolean.property", "true");
        ensure_property_defined("my.int.property", "5");
        ensure_property_defined("my.long.property", "10");
        ensure_property_defined("my.float.property", "10.5");
        ensure_property_defined("my.double.property", "11.5");
        ensure_property_defined("my.date.property", "2017-01-30");
        ensure_property_defined("my.localdate.property", "2016-01-30");
        ensure_property_defined("my.datetime.property", "2015-01-30T10:00");
    }

    private void clear_all_property_values() {
        ensure_property_undefined("my.string.property");
        ensure_property_undefined("my.boolean.property");
        ensure_property_undefined("my.int.property");
        ensure_property_undefined("my.long.property");
        ensure_property_undefined("my.float.property");
        ensure_property_undefined("my.double.property");
        ensure_property_undefined("my.date.property");
        ensure_property_undefined("my.localdate.property");
        ensure_property_undefined("my.datetime.property");
    }

    @Dependent
    public static class DynamicValuesBean {

        @Inject
        @ConfigProperty("my.int.property")
        private ConfigValue<Integer> intPropertyValue;

        public ConfigValue<Integer> getIntPropertyValue() {
            return intPropertyValue;
        }
        
    }

}
