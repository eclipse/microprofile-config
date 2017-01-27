/*
 * Copyright (c) 2009-2017 Mark Struberg and others
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
 */
package org.eclipse.microprofile.impl.config.configsource;

import java.util.Map;

/**
 * ConfigSource which uses {@link System#getProperties()}
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class SystemPropertyConfigSource extends BaseConfigSource {
    public SystemPropertyConfigSource() {
        initOrdinal(400);
    }

    @Override
    public Map<String, String> getProperties() {
        return (Map) System.getProperties();
    }

    @Override
    public String getValue(String key) {
        return System.getProperty(key);
    }

    @Override
    public String getId() {
        return "system-properties";
    }
}
