/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.microprofile.config.inject;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Binds the injection point with a configured value.
 * Should annotate injection points of type String and all types with appropriate converters. 
 * <p>
 * The injection point can be of type {@code Optional<TYPE>}, where {@code TYPE} is one of supported types. 
 * In that case, the optional value will be defined if the property exists in the coniguration. 
 * Otherwise the value will not be present.
 * <p>
 * Example:
 * <pre>
 * <code>
 * 
 *    {@code @Inject}
 *    {@code @ConfigProperty("my.long.property")}
 *    Long injectedLongValue;  // injects value of my.long.property property
 *
 *    {@code @Inject}
 *    {@code @ConfigProperty("my.long.property")}
 *    java.util.Optional{@code <Long>} injectedOptionalLongValue;  // injects value of my.long.property property exists, or empty Optional if not
 *
 *    {@code @Inject}
 *    {@code @ConfigProperty("my.long.property")}
 *    javax.inject.Provider{@code <Long>} longValueInjector;  // injects a provider for the value of my.long.property property to resolve the property dynamically
 * </code>
 * </pre>
 * @author Ondrej Mihalyi
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface ConfigProperty {
    /**
     * @return Name (key) of the config property to inject
     */
    @Nonbinding
    String value();
}
