/*
 * Copyright (c) 2016-2017 Ondrej Mihalyi, Payara Services Ltd. and others
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
                return (doubleMatcher = closeTo(value, range)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                doubleMatcher.describeTo(description);
            }
        };
    }


}
