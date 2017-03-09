/*
 *******************************************************************************
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
 *
 *
 *******************************************************************************/
package org.eclipse.microprofile.config;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Accessor to a configured value.
 * It follows a builder pattern.
 *
 * Accessing the configured value is finally done via {@link #get()} or {@link #getOptional()}
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:gpetracek@apache.org">Gerhard Petracek</a>
 * @author <a href="mailto:rsmeral@jboss.org">Ron Smeral</a>
 */
public interface ConfigValue<T> {

    /**
     * Sets the type of the configuration entry to the given class and returns this builder.
     * The default type of a ConfigValue is {@code String}.
     *
     * This will use the registered {@link org.eclipse.microprofile.config.spi.Converter} for the given type.
     **
     * @param clazz The target type
     * @param <N> The target type
     * @return This builder as a typed ConfigValue
     */
    <N> ConfigValue<N> as(Class<N> clazz);

    /**
     * Specify that a resolved value will get cached for a certain amount of time.
     * After the time expires the next {@link #get()} will again resolve the value
     * from the underlying {@link Config}.
     *
     * @param value the amount of the TimeUnit to wait
     * @param timeUnit the TimeUnit for the value
     *
     * @return This builder
     */
    ConfigValue<T> cacheFor(long value, TimeUnit timeUnit);

    /**
     * Whether to evaluate variables in configured values.
     * A variable starts with '${' and ends with '}', e.g.
     * <pre>
     * mycompany.some.url=${myserver.host}/some/path
     * myserver.host=http://localhost:8081
     * </pre>
     * If 'evaluateVariables' is enabled, the result for the above key
     * {@code "mycompany.some.url"} would be:
     * {@code "http://localhost:8081/some/path"}
     * @param evaluateVariables whether to evaluate variables in values or not
     * @return This builder
     */
    ConfigValue<T> evaluateVariables(boolean evaluateVariables);


    /**
     * The specified lambda will get called on any config value changes.
     *
     * @return This builder
     */
    ConfigValue<T> onChange(ConfigChanged valueChange);

    /**
     * Returns the converted resolved filtered value.
     * @throws java.util.NoSuchElementException if there is no value present
     * @return the resolved value
     */
    T get();

    /**
     * Returns the converted resolved filtered value as an Optional.
     *
     * @return an {@code Optional} for the given type
     *
     * @see #get()
     */
    Optional<T> getOptional();

    /**
     * Resolves the value and split it on each comma (',') character.
     * If a comma is contained in the values it must get escaped with a preceding backslash (&quot;\,&quot;).
     * Any backslash needs to get escaped via double-backslash (&quot;\\&quot;).
     *
     * @return the list of configured comma separated values or an empty Iterable if no
     */
    List<T> getValueList();

    /**
     * Returns the key given in {@link Config#access(String)}.
     * @return the original key
     */
    String getKey();

    /**
     * Returns the actual key which led to successful resolution and corresponds to the resolved value.
     * Otherwise the resolved key should always be equal to the original key.
     * This method is provided for cases, when arameterized resolution is
     * requested but the value for such appended key is not found and some of the fallback keys is used.
     *
     * This should be called only after calling {@link #get()} otherwise the value is undefined (but likely
     * null).
     */
    String getResolvedKey();


    /**
     * TODO feedback from gunnar: could be interesting to have this functionality also as Config#onChange(ConfigChanged)
     * Callback which can be used with {@link #onChange(ConfigChanged)}
     */
    interface ConfigChanged {
        <T> void onValueChange(String key, T oldValue, T newValue);
    }
}
