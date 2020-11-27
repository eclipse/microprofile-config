/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck.profile;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Test cases for Config profile
 *
 * @author Emily Jiang
 */
public class ConfigPropertyFileProfileTest extends Arquillian {
    @Deployment
    public static WebArchive deployment() {

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "ConfigPropertyFileProfileTest.war")
                .addClasses(ConfigPropertyFileProfileTest.class, ProfilePropertyBean.class)
                .addAsResource(
                        new StringAsset(
                                "mp.config.profile=dev\n" +
                                        "vehicle.name=car\n" +
                                        "vehicle.colour=red"),
                        "META-INF/microprofile-config.properties")
                .addAsResource(new StringAsset(
                        "vehicle.name=bike\n" +
                                "vehicle.owner=Bob"),
                        "META-INF/microprofile-config-dev.properties")
                .addAsResource(new StringAsset(
                        "vehicle.name=bike\n" +
                                "vehicle.age=5"),
                        "META-INF/microprofile-config-prod.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return war;
    }

    /**
     * Check both the file microprofile-config.properties and microprofile-config-dev.properties are loaded as config
     * sources but the file microprofile-config-prod.properties is ignored.
     */
    @Test
    public void testConfigProfileWithDev() {
        ProfilePropertyBean bean = CDI.current().select(ProfilePropertyBean.class).get();
        assertThat(bean.getName(), is(equalTo("bike")));
        assertThat(bean.getColour(), is(equalTo("red")));
        assertThat(bean.getOwner(), is(equalTo("Bob")));
        assertEquals(bean.getVehicleAge(), 10);
        assertThat(ConfigProvider.getConfig().getValue("vehicle.name", String.class), is(equalTo("bike")));
        assertThat(ConfigProvider.getConfig().getValue("vehicle.colour", String.class), is(equalTo("red")));
        assertThat(ConfigProvider.getConfig().getValue("vehicle.owner", String.class), is(equalTo("Bob")));
        assertFalse(ConfigProvider.getConfig().getOptionalValue("vehicle.age", Integer.class).isPresent());
    }

    @Dependent
    public static class ProfilePropertyBean {
        @Inject
        @ConfigProperty(name = "vehicle.name")
        private String name;

        @Inject
        @ConfigProperty(name = "vehicle.age", defaultValue = "10")
        private int age;

        @Inject
        @ConfigProperty(name = "vehicle.colour", defaultValue = "black")
        private String colour;

        @Inject
        @ConfigProperty(name = "vehicle.owner", defaultValue = "Jane")
        private String owner;

        public String getName() {
            return name;
        }

        public int getVehicleAge() {
            return age;
        }

        public String getColour() {
            return colour;
        }

        public String getOwner() {
            return owner;
        }
    }

}
