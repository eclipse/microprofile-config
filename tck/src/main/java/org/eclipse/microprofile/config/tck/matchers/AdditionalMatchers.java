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
            
            Matcher<Double> doubleMatcher = null;
            
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
