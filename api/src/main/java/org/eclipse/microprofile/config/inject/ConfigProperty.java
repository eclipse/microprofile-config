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
 * Should annotate injection points of type {@code TYPE}, {@code Optional<TYPE>} or {@code javax.inject.Provider<TYPE>},
 * where {@code TYPE} can be {@code String} and all types which have appropriate converters. 
 * <p>
 *
 * <h3>Examples</h3>
 *
 * The first sample injects the configured value of the my.long.property property.
 * The injected value is static and does not change even if the underline
 * property value changes in the {@link org.eclipse.microprofile.config.Config}.
 * If no configured value exists for this property and no {@link #defaultValue()} is provided,
 * a {@code DeplymentException} will be thrown during startup.
 *
 * It is recommended for a static property or used by a bean with RequestScoped.
 * A further recommendation is to use the built in {@code META-INF/microprofile-config.properties} file mechanism with
 * an ordinal &lt;= 100 to provide default values inside an Application.
 * <pre>
 * &#064;Inject
 * &#064;ConfigProperty(name="my.long.property", defaultValue="123")
 * private Long injectedLongValue;
 * </pre>
 *
 * The following code injects an Optional value of my.long.property property.
 * Countrary to natively injecting the configured value this will not lead to a DeploymentException if the configuration is missing.
 * <pre>
 * &#064;Inject
 * &#064;ConfigProperty(name = "my.optional.int.property")
 * private Optional&lt;Integer&gt; intConfigValue;
 * </pre>
 *
 * The next sample injects a Provider for the value of my.long.property property to resolve the property dynamically.
 * Each invocation to {@code Provider#get()} will resolve the latest value from underlying {@link org.eclipse.microprofile.config.Config} again.
 * Instances of {@code Provider<T>} are guaranteed to be Serializable.
 * <pre>
 * &#064;Inject
 * &#064;ConfigProperty(name = "my.long.property" defaultValue="123")
 * private Provider&lt;Long&gt; longConfigValue;
 * </pre>
 *
 *
 * @author Ondrej Mihalyi
 * @author Emily Jiang
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface ConfigProperty {
    /**
     * The key of the config property used to look up the configuration value. 
     * If it is not specified, it will be derived automatically as {@code <class_name>.<injetion_point_name>},
     * where {@code injection_point_name} is the field name or parameter name,
     * {@code class_name} is the fully qualified name of the class being injected to with the first letter decaptialised.
     * If one of the {@code class_name} or {@code injection_point_name} cannot be determined, the value has to be provided.
     * 
     * @return Name (key) of the config property to inject
     */
    @Nonbinding
    String name() default "";

    /**
     * The default value if the configured property value does not exist.
     * If the target Type is not String a proper {@link org.eclipse.microprofile.config.spi.Converter} will get applied.
     * That means that any default value string should follow the formatting rules of the registered Converters.
     * @return the default value as a string
     */
    @Nonbinding
    String defaultValue() default "";
}
