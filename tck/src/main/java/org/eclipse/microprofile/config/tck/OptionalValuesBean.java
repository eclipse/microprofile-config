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

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Declare a bean for config property injections.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
@Dependent
public class OptionalValuesBean {
    @Inject
    @ConfigProperty(name = "my.optional.int.property")
    private Optional<Integer> intProperty;

    @Inject
    @ConfigProperty(name = "my.notexisting.property")
    private Optional<Integer> notexistingProperty;

    @Inject
    @ConfigProperty(name = "my.notexisting.string.property")
    private Optional<String> notExistingStringProperty;

    private Optional<String> stringValue;

    @Inject
    @ConfigProperty(name = "my.optional.int.property")
    private OptionalInt optionalIntProperty;
    @Inject
    @ConfigProperty(name = "my.notexisting.property")
    private OptionalInt optionalNotExistingIntProperty;

    @Inject
    @ConfigProperty(name = "my.optional.long.property")
    private OptionalLong optionalLongProperty;
    @Inject
    @ConfigProperty(name = "my.notexisting.property")
    private OptionalLong optionalNotExistingLongProperty;

    @Inject
    @ConfigProperty(name = "my.optional.double.property")
    private OptionalDouble optionalDoubleProperty;
    @Inject
    @ConfigProperty(name = "my.notexisting.property")
    private OptionalDouble optionalNotExistingDoubleProperty;

    @Inject
    public void setStringValue(@ConfigProperty(name = "my.optional.string.property") Optional<String> stringValue) {
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

    public OptionalInt getOptionalIntProperty() {
        return optionalIntProperty;
    }

    public OptionalInt getOptionalNotExistingIntProperty() {
        return optionalNotExistingIntProperty;
    }

    public OptionalLong getOptionalLongProperty() {
        return optionalLongProperty;
    }

    public OptionalLong getOptionalNotExistingLongProperty() {
        return optionalNotExistingLongProperty;
    }

    public OptionalDouble getOptionalDoubleProperty() {
        return optionalDoubleProperty;
    }

    public OptionalDouble getOptionalNotExistingDoubleProperty() {
        return optionalNotExistingDoubleProperty;
    }
}
