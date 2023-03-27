/*
 * Copyright (c) 2017-2021 Contributors to the Eclipse Foundation
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

package org.eclipse.microprofile.config.tck.util;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.testng.Assert;

/**
 * @author <a href="mailto:emijiang6@googlemail.com">Emily Jiang</a>
 * @author <a href="mailto:josephcass149@gmail.com">Joseph Cass</a>
 */
public final class AdditionalAssertions {

    private AdditionalAssertions() {
        // utility class
    }

    /**
     * Use URI.equals() since URL.equals() performs DNS resolution which is undesired. See #549
     *
     * @throws URISyntaxException
     *             if either URL is not formatted correctly
     */
    public static boolean urlEquals(URL expected, URL actual) throws URISyntaxException {
        return (expected.toURI().equals(actual.toURI()));
    }

    public static void assertURLArrayEquals(URL[] value, URL[] expectedValue) throws MalformedURLException {
        assertURLListEquals(Arrays.asList(value), Arrays.asList(expectedValue));
    }

    public static void assertURLListEquals(List<URL> value, List<URL> expectedValue) throws MalformedURLException {

        Assert.assertTrue(IntStream.range(0, expectedValue.size()).allMatch(i -> {
            try {
                return urlEquals(expectedValue.get(i), value.get(i));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return false;
        }));
    }

    public static void assertURLSetEquals(Set<URL> valueSet, Set<URL> expectedURLSet)
            throws MalformedURLException, URISyntaxException {

        Assert.assertTrue(valueSet.size() == expectedURLSet.size());

        Iterator<URL> it = valueSet.iterator();
        boolean isEquals = true;
        while (it.hasNext()) {
            boolean found = false;
            URL url = it.next();
            for (URL thisURL : expectedURLSet) {
                if (urlEquals(thisURL, url)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                isEquals = false;
                break;
            }
        }
        Assert.assertTrue(isEquals);
    }

}