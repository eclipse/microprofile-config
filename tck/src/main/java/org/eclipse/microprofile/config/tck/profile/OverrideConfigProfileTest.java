/*
 * Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test cases for Config profile
 *
 * @author Oliver Bertuch
 */
public class OverrideConfigProfileTest extends Arquillian {
    @Deployment
    public static WebArchive deployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "OverrideConfigProfileTest.war")
                .addClasses(OverrideConfigProfileTest.class, ProfilePropertyBean.class)
                .addAsResource(
                        new StringAsset(
                                "mp.config.profile=dev\n" +
                                "%dev." + PROPERTY + "=foo\n" +
                                PROPERTY + "=bar\n"),
                    "META-INF/microprofile-config.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return war;
    }

    private static final String PROPERTY = "mp.tck.prop.dummy";
    private static final String EXPECTED = "dummy";

    /**
     * This test relies on the system property "mp.tck.prop.dummy" being set to "dummy" as described in the TCK
     * README as a requirement for runners. System properties are per the TCK requirements at ordinal 120, so shall
     * override the given properties in the microprofile-config.properties file (ordinal 100) included in the WAR above.
     */
    @Test
    public void testConfigProfileWithDevAndOverride() {
        assertThat(System.getProperty(PROPERTY), is(equalTo(EXPECTED)));

        ProfilePropertyBean bean = CDI.current().select(ProfilePropertyBean.class).get();
        assertThat(bean.getConfigProperty(), is(equalTo(EXPECTED)));

        assertThat(ConfigProvider.getConfig().getValue(PROPERTY, String.class), is(equalTo(EXPECTED)));
    }

    @Dependent
    public static class ProfilePropertyBean {
        @Inject
        @ConfigProperty(name = PROPERTY)
        private String stringProperty;
        public String getConfigProperty() {
            return stringProperty;
        }
    }
}
