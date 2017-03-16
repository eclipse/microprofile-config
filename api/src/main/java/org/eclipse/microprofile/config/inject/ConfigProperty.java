/*
 * Copyright (c) 2016-2017 Payara Services Ltd., IBM Corp. and others
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

package org.eclipse.microprofile.config.inject;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Binds the injection point with a configured value.
 * Should annotate injection points of type {@code TYPE} or {@code ConfigValue<TYPE>}, 
 * where {@code TYPE} can be {@code String} and all types which have appropriate converters. 
 * <p>
 * Example:
 * <pre>
 * <code>
 *    {@literal @Inject}
 *    {@literal @ConfigProperty(name="my.long.property" defaultValue="123")}
 *    Long injectedLongValue;  
 *    Injects value of my.long.property property. The injected value is static and does not change even if the underline 
 *    property value changes. It is recommended for a static property or used by a bean with RequestScoped.
 *             
 * 
 *   {@literal @Inject}
 *   {@literal @ConfigProperty(name = "my.long.property" defaultValue="123")}
 *   {@literal Provider<Long>} longConfigValue;  
 *   // injects a Provider for the value of my.long.property property to resolve the property dynamically
 * </code>
 * </pre>
 * @author Ondrej Mihalyi
 * @author Emily Jiang
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface ConfigProperty {
    /**
     * The key of the config property used to look up the configuration value. 
     * If it is not specified, it will be derived automatically as {@code <class_name>.<injetion_point_name>}, 
     * where {@code injection_point_name} is the field name, 
     * {@code class_name} is the simple name of the class being injected to with the first letter decaptialised. 
     * If one of the {@code class_name} or {@code injection_point_name} cannot be determined, the value has to be provided.
     * 
     * @return Name (key) of the config property to inject
     */
    @Nonbinding
    String name() default "";
    /**
     * The default value if the property does not exist
     * @return the default value as a string
     */
    @Nonbinding
    String defaultValue() default "";
}
