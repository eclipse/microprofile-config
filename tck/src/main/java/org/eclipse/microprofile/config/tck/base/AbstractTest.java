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
package org.eclipse.microprofile.config.tck.base;

import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;


/**
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class AbstractTest extends Arquillian {


    public static void addFile(JavaArchive archive, String originalPath) {
        archive.addAsResource(new UrlAsset(Thread.currentThread().getContextClassLoader().getResource("internal/" + originalPath)),
                originalPath);
    }

    public static void addFile(JavaArchive archive, String originalFile, String targetFile) {
        archive.addAsResource(new UrlAsset(Thread.currentThread().getContextClassLoader().getResource(originalFile)),
                targetFile);
    }

}
