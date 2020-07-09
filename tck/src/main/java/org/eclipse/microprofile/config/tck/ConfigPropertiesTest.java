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

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verify the support {@code ConfigProperties}.
 *
 * @author <a href="mailto:emijiang6@googlemail.com">Emily Jiang</a>
 */
public class ConfigPropertiesTest extends Arquillian {

    private @Inject BeanOne customerBeanOne;
    
    private @Inject @ConfigProperties(prefix="client") BeanOne clientBeanOne;
    private @Inject @ConfigProperties BeanOne beanOne;

    private @Inject BeanTwo beanTwo;
    private @Inject @ConfigProperties(prefix="my") BeanTwo myBeanTwo;
    private @Inject BeanFour myBeanFour;
    private @Inject BeanThree beanThree;

    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "ConfigPropertiesTest.jar")
                .addClasses(ConfigPropertiesTest.class, BeanOne.class, BeanTwo.class, BeanThree.class, Location.class)
                .addAsManifestResource(
                    new StringAsset(
                        "customer.name=Bob\n" +
                        "customer.age=24\n" +
                        "customer.location=2 Hook Road, Winchester, Hampshire, SO21 2JN, UK\n" +
                        "customer.job=Developer\n" + 
                        "customer.new.hobbies=Badminton, Tennis\n" +
                        "client.name=Rob\n" +
                        "client.age=25\n" +
                        "client.location=22 Hook Road, Winchester, Hampshire, SO21 2JN, UK\n" +
                        "client.job=Engineer\n" + 
                        "client.new.hobbies=Football, Tennis\n" +
                        "name=Hob\n" +
                        "age=26\n" +
                        "location=222 Hook Road, Winchester, Hampshire, SO21 2JN, UK\n" +
                        "job=Plumber\n" + 
                        "new.hobbies=Volleyball\n" +
                        "host=localhost\n" +
                        "port=9080\n"+
                        "endpoint=woof\n" + 
                        "my.host=myhost\n" +
                        "my.port=9081\n"+
                        "my.endpoint=poof\n" +
                        "name=Harry\n" +
                        "age=21\n" +
                        "nationality=UK\n" +
                        "other.name=Holly\n" +
                        "other.age=20\n" +
                        "other.nationality=USA\n" 
                        ),
                        "microprofile-config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "ConfigPropertiesTest.war")
                .addAsLibrary(testJar);
        return war;
    }


    @Test
    public void testConfigPropertiesPlainInjection() {
        Assert.assertEquals("Bob", customerBeanOne.getName());
        Assert.assertEquals(24, customerBeanOne.age);
        Assert.assertEquals("Developer", customerBeanOne.job);
        Assert.assertEquals(new String[] {"Badminton", "Tennis"}, customerBeanOne.hobbies);
        Assert.assertEquals(new Location("2 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), customerBeanOne.location);
    }


    @Test
    public void testConfigPropertiesProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanOne cBeanOne = config.getConfigProperties(BeanOne.class);
        Assert.assertEquals("Bob", cBeanOne.getName());
        Assert.assertEquals(24, cBeanOne.age);
        Assert.assertEquals("Developer", cBeanOne.job);
        Assert.assertEquals(new String[] {"Badminton", "Tennis"}, cBeanOne.hobbies);
        Assert.assertEquals(new Location("2 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), cBeanOne.location);
    }


    @Test
    public void testConfigPropertiesWithPrefix() {
        Assert.assertEquals("Rob", clientBeanOne.getName());
        Assert.assertEquals(25, clientBeanOne.age);
        Assert.assertEquals("Engineer", clientBeanOne.job);
        Assert.assertEquals(new String[] {"Football", "Tennis"}, clientBeanOne.hobbies);
        Assert.assertEquals(new Location("22 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), clientBeanOne.location);
        
        //programmatic lookup of the beans
        BeanOne bo= CDI.current().select(BeanOne.class, ConfigProperties.Literal.of("client")).get();
        Assert.assertEquals("Rob", bo.getName());
        Assert.assertEquals(25, bo.age);
        Assert.assertEquals("Engineer", bo.job);
        Assert.assertEquals(new String[] {"Football", "Tennis"}, bo.hobbies);
        Assert.assertEquals(new Location("22 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), bo.location);
    }

    @Test
    public void testConfigPropertiesWithPrefixProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanOne cBeanOne = config.getConfigProperties(BeanOne.class, "client");
        Assert.assertEquals("Rob", cBeanOne.getName());
        Assert.assertEquals(25, cBeanOne.age);
        Assert.assertEquals("Engineer", cBeanOne.job);
        Assert.assertEquals(new String[] {"Football", "Tennis"}, cBeanOne.hobbies);
        Assert.assertEquals(new Location("22 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), cBeanOne.location);
    }

    @Test
    public void testConfigPropertiesWithoutPrefix() {
        Assert.assertEquals("Hob", beanOne.getName());
        Assert.assertEquals(26, beanOne.age);
        Assert.assertEquals("Plumber", beanOne.job);
        Assert.assertEquals(new String[] {"Volleyball"}, beanOne.hobbies);
        Assert.assertEquals(new Location("222 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), beanOne.location);
        
        //programmatic lookup of the beans
        BeanOne bo= CDI.current().select(BeanOne.class, ConfigProperties.Literal.NOPREFIX).get();
        Assert.assertEquals("Hob", bo.getName());
        Assert.assertEquals(26, bo.age);
        Assert.assertEquals("Plumber", bo.job);
        Assert.assertEquals(new String[] {"Volleyball"}, bo.hobbies);
        Assert.assertEquals(new Location("222 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), bo.location);
    }

    @Test
    public void testConfigPropertiesWithoutPrefixProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanOne beanOne = config.getConfigProperties(BeanOne.class, "");
        Assert.assertEquals("Hob", beanOne.getName());
        Assert.assertEquals(26, beanOne.age);
        Assert.assertEquals("Plumber", beanOne.job);
        Assert.assertEquals(new String[] {"Volleyball"}, beanOne.hobbies);
        Assert.assertEquals(new Location("222 Hook Road, Winchester, Hampshire, SO21 2JN, UK"), beanOne.location);
    }

    @Test
    public void testConfigPropertiesNoPrefixOnBean() {
        Assert.assertEquals("localhost", beanTwo.getHost());
        Assert.assertEquals("9080", beanTwo.port);
        Assert.assertEquals("woof", beanTwo.endpoint);
    }

    @Test
    public void testConfigPropertiesNoPrefixOnBeanProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanTwo myBeanTwo = config.getConfigProperties(BeanTwo.class);
        Assert.assertEquals("localhost", myBeanTwo.getHost());
        Assert.assertEquals("9080", myBeanTwo.port);
        Assert.assertEquals("woof", myBeanTwo.endpoint);
    }

    @Test
    public void testConfigPropertiesNoPrefixOnBeanThenSupplyPrefix() {
        Assert.assertEquals("myhost", myBeanTwo.getHost());
        Assert.assertEquals("9081", myBeanTwo.port);
        Assert.assertEquals("poof", myBeanTwo.endpoint);
    }

    @Test
    public void testConfigPropertiesNoPrefixOnBeanThenSupplyPrefixProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanTwo myBeanTwo = config.getConfigProperties(BeanTwo.class, "my");
        Assert.assertEquals("myhost", myBeanTwo.getHost());
        Assert.assertEquals("9081", myBeanTwo.port);
        Assert.assertEquals("poof", myBeanTwo.endpoint);
    }

    @Test
    public void testNoConfigPropertiesAnnotationNoPrefixProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanThree myBeanThree = config.getConfigProperties(BeanThree.class);
        Assert.assertEquals("Harry", myBeanThree.name);
        Assert.assertEquals(21, myBeanThree.age);
        Assert.assertEquals("UK", myBeanThree.getNationality());
        BeanThree anotherThree = config.getConfigProperties(BeanThree.class, null);
        Assert.assertEquals("Harry", anotherThree.name);
        Assert.assertEquals(21, anotherThree.age);
        Assert.assertEquals("UK", anotherThree.getNationality());
    }

    @Test
    public void testNoConfigPropertiesAnnotationWithPrefixProgrammatic() {
        Config config = ConfigProvider.getConfig();
        BeanThree myBeanThree = config.getConfigProperties(BeanThree.class, "other");
        Assert.assertEquals("Holly", myBeanThree.name);
        Assert.assertEquals(20, myBeanThree.age);
        Assert.assertEquals("USA", myBeanThree.getNationality());
    }

    @Test
    public void testNoConfigPropertiesAnnotationInjection() {
        //The fields on beanThree are not resolved to config properties,
        // as the bean class has no ConfigProperties annotation.
        Assert.assertNull( beanThree.name);
        Assert.assertEquals(0, beanThree.age);
        Assert.assertNull(beanThree.getNationality());
    }

    @Test
    public void testConfigPropertiesDefaultOnBean() {
        Assert.assertEquals("mycloud.org", myBeanFour.getHost());
        Assert.assertEquals("9080", myBeanFour.port);
        Assert.assertFalse(myBeanFour.location.isPresent());
    }

    @Test
    public void testConfigPropertiesDefaultOnBeanPL() {
        Config config = ConfigProvider.getConfig();
        BeanFour myBeanFour = config.getConfigProperties(BeanFour.class);
        Assert.assertEquals("mycloud.org", myBeanFour.getHost());
        Assert.assertEquals("9080", myBeanFour.port);
        Assert.assertFalse(myBeanFour.location.isPresent());
    }

    @ConfigProperties(prefix="customer")
    @Dependent
    public static class BeanOne {
        private String name;
        int age;
        public Location location;
        protected String job;
        @ConfigProperty(name="new.hobbies")
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

    @ConfigProperties(prefix="cloud")
    @Dependent
    public static class BeanFour {
        @ConfigProperty(name="a.host", defaultValue="mycloud.org")
        private String host;
        public int port = 9080;
        public String getHost() {
            return host;
        }  
        public Optional<String> location;
    }

}