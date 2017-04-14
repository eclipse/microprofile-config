/**********************************************************************
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation 
 *
 * See the NOTICES file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************************/
package org.eclipse.microprofile.config.tck.broken;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.spi.DeploymentException;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * Verify that injectng a native value which is not configured will lead to a deployment error.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class MissingValueOnInstanceInjectionTest extends Arquillian {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive deploy() {
        JavaArchive testJar = ShrinkWrap
                .create(JavaArchive.class, "missingValueOnInstanceInjectionTest.jar")
                .addClass(MissingValueOwner.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .as(JavaArchive.class);

        WebArchive war = ShrinkWrap
                .create(WebArchive.class, "missingValueOnInstanceInjectionTest.war")
                .addAsLibrary(testJar);
        return war;
    }

    @Test
    public void test() {
    }


    @RequestScoped
    public static class MissingValueOwner {

        @Inject
        @ConfigProperty(name="not.existing.value")
        private Long configValue;
    }
}
