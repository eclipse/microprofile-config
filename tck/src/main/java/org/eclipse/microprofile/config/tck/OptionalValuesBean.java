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
/**
 * Declare a bean for config property injections.
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
import java.util.Optional;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Dependent
public class OptionalValuesBean {
    @Inject
    @ConfigProperty(name="my.optional.int.property")
    private Optional<Integer> intProperty;

    @Inject
    @ConfigProperty(name="my.notexisting.property")
    private Optional<Integer> notexistingProperty;

    @Inject
    @ConfigProperty(name="my.notexisting.string.property")
    private Optional<String> notExistingStringProperty;

    private Optional<String> stringValue;

    @Inject
    public void setStringValue(@ConfigProperty(name="my.optional.string.property") Optional<String> stringValue) {
        this.stringValue = stringValue;
    }

    public Optional<String> getStringValue() {
        return stringValue;
    }

    public Optional<Integer> getIntProperty() {
        return intProperty;
    }

    public Optional<Integer> getNotexistingProperty() {
        return notexistingProperty;
    }

    public Optional<String> getNotExistingStringProperty() {
        return notExistingStringProperty;
    }
}
