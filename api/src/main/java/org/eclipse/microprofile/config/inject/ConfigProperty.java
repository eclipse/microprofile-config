/**********************************************************************
 * Copyright (c) 2016-2017 Contributors to the Eclipse Foundation 
 *
 * See the NOTICES file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * SPDX-License-Identifier: Apache-2.0
 **********************************************************************/

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
 * Can be used to annotate injection points of type {@code TYPE}, {@code Optional<TYPE>} or {@code javax.inject.Provider<TYPE>},
 * where {@code TYPE} can be {@code String} and all types which have appropriate converters. 
 *
 * <h2>Examples</h2>
 *
 * <h3>Injecting Native Values</h3>
 *
 * The first sample injects the configured value of the {@code my.long.property} property.
 * The injected value does not change even if the underline
 * property value changes in the {@link org.eclipse.microprofile.config.Config}.
 * If no configured value exists for this property and no {@link #defaultValue()} is provided,
 * a {@code DeplymentException} will be thrown during startup.
 *
 * <p>Injecting a native value is recommended for a property that does not change at runtime or used by a bean with RequestScoped.
 * <p>A further recommendation is to use the built in {@code META-INF/microprofile-config.properties} file mechanism
 * to provide default values inside an Application.
 * <pre>
 * &#064;Inject
 * &#064;ConfigProperty(name="my.long.property", defaultValue="123")
 * private Long injectedLongValue;
 * </pre>
 *
 * <h3>Injecting Optional Values</h3>
 *
 * The following code injects an Optional value of my.long.property property.
 * Countrary to natively injecting the configured value this will not lead to a DeploymentException if the configured value is missing.
 * <pre>
 * &#064;Inject
 * &#064;ConfigProperty(name = "my.optional.int.property")
 * private Optional&lt;Integer&gt; intConfigValue;
 * </pre>
 *
 * <h3>Injecting Dynamic Values</h3>
 *
 * The next sample injects a Provider for the value of my.long.property property to resolve the property dynamically.
 * Each invocation to {@code Provider#get()} will resolve the latest value from underlying {@link org.eclipse.microprofile.config.Config} again.
 * The existence of configured values will get checked during startup.
 * Instances of {@code Provider<T>} are guaranteed to be Serializable.
 * <pre>
 * &#064;Inject
 * &#064;ConfigProperty(name = "my.long.property" defaultValue="123")
 * private Provider&lt;Long&gt; longConfigValue;
 * </pre>
 *
 * <p>If {@code ConfigProperty} is used with a type where no {@link org.eclipse.microprofile.config.spi.Converter} exists,
 * a deployment error is thrown.
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
     * <p>The default value if the configured property value does not exist.
     *
     * <p>If the target Type is not String a proper {@link org.eclipse.microprofile.config.spi.Converter} will get applied.
     * That means that any default value string should follow the formatting rules of the registered Converters.
     *
     * 
     * @return the default value as a string
     */
    @Nonbinding
    String defaultValue() default "";
}
