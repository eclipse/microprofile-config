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

package org.eclipse.microprofile.config.tck.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.closeTo;

/**
 *
 * @author Ondrej Mihalyi
 */
public final class AdditionalMatchers {
    
    private AdditionalMatchers() {
        // utility class
    }
    
    public static Matcher<Float> floatCloseTo(float value, float range) {
        return new BaseMatcher<Float>() {
            
            private Matcher<Double> doubleMatcher = null;
            
            @Override
            public boolean matches(Object item) {
                if (item instanceof Float) {
                    return (doubleMatcher = closeTo(value, range)).matches(((Float)item).doubleValue());
                }
                else {
                    return (doubleMatcher = closeTo(value, range)).matches(item);
                }
            }

            @Override
            public void describeTo(Description description) {
                doubleMatcher.describeTo(description);
            }
        };
    }


}
