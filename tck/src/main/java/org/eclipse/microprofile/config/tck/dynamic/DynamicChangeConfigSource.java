/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.microprofile.config.tck.dynamic;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.eclipse.microprofile.config.spi.ConfigSource;


/**
 * @author <a href="mailto:struberg@yahoo.de">Mark Struberg</a>
 */
public class DynamicChangeConfigSource implements ConfigSource, Closeable {
    public static final String TEST_ATTRIBUTE = "tck.config.test.javaconfig.dynymic.testattrib";

    public static boolean callbackListenerSet = false;

    private Consumer<Set<String>> reportAttributeChange;
    private AtomicInteger i = new AtomicInteger(0);
    private Map<String, String> properties = new HashMap<>();
    private Thread worker;

    public DynamicChangeConfigSource() {
    }

    @Override
    public void close() throws IOException {
        i.set(10_001);
    }

    @Override
    public Map<String, String> getProperties() {
        properties.put(TEST_ATTRIBUTE, Integer.toString(i.get()));
        return properties;
    }

    @Override
    public String getValue(String propertyName) {
        return getProperties().get(propertyName);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void setOnAttributeChange(Consumer<Set<String>> reportAttributeChange) {
        this.reportAttributeChange = reportAttributeChange;

        callbackListenerSet = true;

        // start a new backgroundthread.
        worker = new Thread() {
            @Override
            public void run() {
                while (i.incrementAndGet() < 10_000) {
                    reportAttributeChange.accept(Collections.singleton(TEST_ATTRIBUTE));
                    try {
                        Thread.sleep(10L);
                    }
                    catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
        worker.start();

    }

}
