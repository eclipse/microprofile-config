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

import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;

/**
 * Verify the support {@code ConfigProperties}.
 *
 * @author <a href="mailto:emijiang6@googlemail.com">Emily Jiang</a>
 */
public class ConfigPropertiesTest extends Arquillian {

    @Deployment
    public static WebArchive deploy() {
        return ShrinkWrap
                .create(WebArchive.class, "ConfigPropertiesTest.war")
                .addClasses(ConfigPropertiesTest.class, BeanOne.class, BeanTwo.class, BeanThree.class, BeanFour.class,
                        Location.class)
                .addAsResource(
                        new StringAsset(
                                "customer.name=Bob\n" +
                                        "customer.age=24\n" +
                                        "customer.location=2 Hook Road, Winchester, Hampshire, SO21 2JN, UK\n" +
                                        "customer.job=Developer\n" +
                                        "customer.new.hobbies=Badminton,Tennis\n" +
                                        "client.name=Rob\n" +
                                        "client.age=25\n" +
                                        "client.location=22 Hook Road, Winchester, Hampshire, SO21 2JN, UK\n" +
                                        "client.job=Engineer\n" +
                                        "client.new.hobbies=Football,Tennis\n" +
                                        "name=Harry\n" +
                                        "age=21\n" +
                                        "nationality=UK\n" +
                                        "location=222 Hook Road, Winchester, Hampshire, SO21 2JN, UK\n" +
                                        "job=Plumber\n" +
                                        "new.hobbies=Volleyball\n" +
                                        "host=localhost\n" +
                                        "port=9080\n" +
                                        "endpoint=woof\n" +
                                        "my.host=myhost\n" +
                                        "my.port=9081\n" +
                                        "my.endpoint=poof\n" +
                                        "other.name=Holly\n" +
                                        "other.age=20\n" +
                                        "other.nationality=USA\n"),
                        "META-INF/microprofile-config.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Inject
    InjectingBean bean;

    @Test
    public void testConfigPropertiesPlainInjection() {
        BeanOne customerBeanOne = bean.getCustomerBeanOne();
        Assert.assertEquals("Bob", customerBeanOne.getName());
        Assert.assertEquals(24, customerBeanOne.age);
        Assert.assertEquals("Developer", customerBeanOne.job);
        Assert.assertEquals(new String[]{"Badminton", "Tennis"}, customerBeanOne.hobbies);
        Assert.assertEquals(new Location("2 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), customerBeanOne.location);
    }

    @Test
    public void testConfigPropertiesWithPrefix() {
        BeanOne clientBeanOne = bean.getClientBeanOne();
        Assert.assertEquals("Rob", clientBeanOne.getName());
        Assert.assertEquals(25, clientBeanOne.age);
        Assert.assertEquals("Engineer", clientBeanOne.job);
        Assert.assertEquals(new String[]{"Football", "Tennis"}, clientBeanOne.hobbies);
        Assert.assertEquals(new Location("22 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), clientBeanOne.location);

        // programmatic lookup of the beans
        BeanOne bo = CDI.current().select(BeanOne.class, ConfigProperties.Literal.of("client")).get();
        Assert.assertEquals("Rob", bo.getName());
        Assert.assertEquals(25, bo.age);
        Assert.assertEquals("Engineer", bo.job);
        Assert.assertEquals(new String[]{"Football", "Tennis"}, bo.hobbies);
        Assert.assertEquals(new Location("22 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), bo.location);
    }

    @Test
    public void testConfigPropertiesWithoutPrefix() {
        BeanOne beanOne = bean.getBeanOne();
        Assert.assertEquals("Harry", beanOne.getName());
        Assert.assertEquals(21, beanOne.age);
        Assert.assertEquals("Plumber", beanOne.job);
        Assert.assertEquals(new String[]{"Volleyball"}, beanOne.hobbies);
        Assert.assertEquals(new Location("222 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), beanOne.location);

        // programmatic lookup of the beans
        BeanOne bo = CDI.current().select(BeanOne.class, ConfigProperties.Literal.of("")).get();
        Assert.assertEquals("Harry", bo.getName());
        Assert.assertEquals(21, bo.age);
        Assert.assertEquals("Plumber", bo.job);
        Assert.assertEquals(new String[]{"Volleyball"}, bo.hobbies);
        Assert.assertEquals(new Location("222 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), bo.location);
    }

    @Test
    public void testConfigPropertiesNoPrefixOnBean() {
        BeanTwo beanTwo = bean.getBeanTwo();
        Assert.assertEquals("localhost", beanTwo.getHost());
        Assert.assertEquals(9080, beanTwo.port);
        Assert.assertEquals("woof", beanTwo.endpoint);
    }

    @Test
    public void testConfigPropertiesNoPrefixOnBeanThenSupplyPrefix() {
        BeanTwo myBeanTwo = bean.getMyBeanTwo();
        Assert.assertEquals("myhost", myBeanTwo.getHost());
        Assert.assertEquals(9081, myBeanTwo.port);
        Assert.assertEquals("poof", myBeanTwo.endpoint);
    }

    @Test
    public void testNoConfigPropertiesAnnotationInjection() {
        // The fields on beanThree are not resolved to config properties,
        // as the bean class has no ConfigProperties annotation.
        BeanThree beanThree = bean.getBeanThree();
        Assert.assertNull(beanThree.name);
        Assert.assertEquals(0, beanThree.age);
        Assert.assertNull(beanThree.getNationality());
    }

    @Test
    public void testConfigPropertiesDefaultOnBean() {
        BeanFour myBeanFour = bean.getMyBeanFour();
        Assert.assertEquals("mycloud.org", myBeanFour.getHost());
        Assert.assertEquals(9080, myBeanFour.port);
        Assert.assertFalse(myBeanFour.location.isPresent());
    }

    @ConfigProperties(prefix = "customer")
    @Dependent
    public static class BeanOne {
        private String name;
        int age;
        public Location location;
        protected String job;
        @ConfigProperty(name = "new.hobbies")
        public String[] hobbies;

        /**
         * @return String return the name
         */
        public String getName() {
            return name;
        }
    }

    @ConfigProperties
    @RequestScoped
    public static class BeanTwo {
        private String host;
        int port;
        String endpoint;
        /**
         * @return String return the host
         */
        public String getHost() {
            return host;
        }
    }

    @Dependent
    public static class BeanThree {
        public String name;
        public int age;
        private String nationality;
        /**
         * @return String return the host
         */
        public String getNationality() {
            return nationality;
        }
    }

    @ConfigProperties(prefix = "cloud")
    @Dependent
    public static class BeanFour {
        @ConfigProperty(name = "a.host", defaultValue = "mycloud.org")
        private String host;
        public int port = 9080;
        public String getHost() {
            return host;
        }
        public Optional<String> location;
    }

    // declare a proper bean with bean defining annotation as an injection target for config-related injection
    @Dependent
    public static class InjectingBean {

        @Inject
        @ConfigProperties
        private BeanOne customerBeanOne;

        @Inject
        @ConfigProperties(prefix = "client")
        private BeanOne clientBeanOne;

        @Inject
        @ConfigProperties(prefix = "")
        private BeanOne beanOne;

        @Inject
        @ConfigProperties
        private BeanTwo beanTwo;

        @Inject
        @ConfigProperties(prefix = "my")
        private BeanTwo myBeanTwo;

        @Inject
        @ConfigProperties
        private BeanFour myBeanFour;

        @Inject
        private BeanThree beanThree;

        public BeanOne getCustomerBeanOne() {
            return customerBeanOne;
        }

        public BeanOne getClientBeanOne() {
            return clientBeanOne;
        }

        public BeanOne getBeanOne() {
            return beanOne;
        }

        public BeanTwo getBeanTwo() {
            return beanTwo;
        }

        public BeanTwo getMyBeanTwo() {
            return myBeanTwo;
        }

        public BeanFour getMyBeanFour() {
            return myBeanFour;
        }

        public BeanThree getBeanThree() {
            return beanThree;
        }
    }
}
