/*
 *******************************************************************************
 * Copyright (c) 2016-2017 Mark Struberg. and others
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
 *******************************************************************************/

/**
 * <p>CDI Support for Microprofile Config
 *
 * <p>Microprofile Config also supports injection via a JSR-330 DI container:
 * <pre>
 *  &#064;Inject
 *  &#064;ConfigProperty(name="myproject.some.endpoint.url");
 *  private String restUrl;
 * </pre>
 *
 * <p>The following types can be injected:
 * <ul>
 *     <li><code>T</code> where T is a Type where a {@link org.eclipse.microprofile.config.spi.Converter} exists.</li>
 *     <li><code>Optional&lt;T&gt;</code> where T is a Type where a {@link org.eclipse.microprofile.config.spi.Converter} exists.</li>
 *     <li><code>Provider&lt;T&gt;</code> where T is a Type where a {@link org.eclipse.microprofile.config.spi.Converter} exists.</li>
 * </ul>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @version 1.0
 */
package org.eclipse.microprofile.config.inject;

