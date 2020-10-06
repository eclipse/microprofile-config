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
public class ProdProfileTest extends Arquillian {
    @Deployment
    public static WebArchive deployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "ProdProfileTest.war")
                .addClasses(ProdProfileTest.class, ProfilePropertyBean.class)
                .addAsResource(
                        new StringAsset(
                                "mp.config.profile=prod\n" +
                                        "%dev.vehicle.name=bike\n" +
                                        "%prod.vehicle.name=bus\n" +
                                        "%test.vehicle.name=van\n" +
                                        "vehicle.name=car"),
                        "META-INF/microprofile-config.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return war;
    }

    @Test
    public void testConfigProfileWithDev() {
        ProfilePropertyBean bean = CDI.current().select(ProfilePropertyBean.class).get();
        assertThat(bean.getConfigProperty(), is(equalTo("bus")));
        assertThat(ConfigProvider.getConfig().getValue("vehicle.name", String.class), is(equalTo("bus")));
    }

    @Dependent
    public static class ProfilePropertyBean {
        @Inject
        @ConfigProperty(name = "vehicle.name")
        private String vehicleName;
        public String getConfigProperty() {
            return vehicleName;
        }
    }
}
