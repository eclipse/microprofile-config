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
import javax.inject.Qualifier;

/**
 * Marks an interface, for which an instance that provides configured values should be provided by the container.
 * <p>
 * The interface can only contain Java Bean getters that return either a valid configuration type 
 * (either String or a type with appropriate converter).
 * <p>
 * Methods in the annotated interface need to be annotated with {@link ConfigProperty} to define the configuration key. 
 * If a configuration property with that key exists, the particular method will return its value. If the property does not exist,
 * an exception of type {@link java.util.NoSuchElementException} is thrown instead.
 * <p>
 * The methods can also return Optional. If they do, the return value is the same as in 
 * {@link io.microprofile.config.Config#getValue(String, Class)}, where Class is the generic type in the Optional.
 * <p>
 * Example:
 * <pre>
 * <code>
 *    {@code @ConfigRegistry}
 *    public interface MyConfig {
 * 
 *        {@code @ConfigProperty("my.long.property")}
 *        Long getLongValue();  
 *              // returns the value of my.long.property property or throws an exception if it does not exist
 *
 *        {@code @ConfigProperty("my.long.property")}
 *        java.util.Optional{@code <Long>} getOptionalLongValue();  
 *              // returns the optional value of my.long.property property
 *    }
 * </code>
 * </pre>
 * ...and usage:
 * <pre>
 * <code>
 *        {@code @Inject}
 *        MyConfig myConfig;
 * </code>
 * </pre>
 * @author Ondrej Mihalyi
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface ConfigRegistry {
}
