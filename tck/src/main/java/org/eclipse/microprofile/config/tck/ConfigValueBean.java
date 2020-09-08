/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck;

import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class ConfigValueBean {
    @Inject
    @ConfigProperty(name = "my.prop")
    private ConfigValue configValue;

    @Inject
    @ConfigProperty(name = "my.prop.default", defaultValue = "default")
    private ConfigValue configValueDefault;

    ConfigValue getConfigValue() {
        return configValue;
    }

    ConfigValue getConfigValueDefault() {
        return configValueDefault;
    }
}
