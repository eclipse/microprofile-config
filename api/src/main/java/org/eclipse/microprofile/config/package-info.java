/*
 *******************************************************************************
 * Copyright (c) 2016 IBM Corp. and others
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
 * <h1>Configuration for Java Microprofile</h1>
 *
 * <h2>Rational</h2>
 *
 * <p>Many project artifacts (e.g. WAR, EAR) should only be created once and then get installed at different customers, stages, etc
 * They need to target those different execution environments without necessity of any repackaging.
 * In other words: depending on the situation they need different configuration.
 *
 * <p>This is easily achievable by having a set of default configuration values inside the project artifact.
 * But be able to overwrite those default values from external.
 *
 * <h2>How it works</h2>
 *
 * <p>A 'Configuration' consists of the information collected from the registered
 * {@link org.eclipse.microprofile.config.spi.ConfigSource ConfigSources}.
 * These `ConfigSource`s get sorted according to their <i>ordinal</i>.
 * That way it is possible to overwrite configuration with lower importance from outside.
 *
 * <p>By default there are 3 ConfigSources:
 *
 * <ul>
 * <li>{@code System.getenv()} (ordinal=400)</li>
 * <li>{@code System.getProperties()} (ordinal=300)</li>
 * <li>all {@code META-INF/microprofile-config.properties} files on the ClassPath.
 *  (ordinal=100, separately configurable via a config_ordinal property inside each file)</li>
 * </ul>
 *
 * <p>That means that one can put the default configuration in a `META-INF/microprofile-config.properties` anywhere on the classpath.
 * And Operations can later simply e.g set a system property to change this default configuration.
 *
 * <p>It is of course also possible to register own {@link org.eclipse.microprofile.config.spi.ConfigSource ConfigSources}.
 * A {@code ConfigSource} could e.g. read configuration values from a database table, a remote server, etc
 *
 *  <h2>Accessing and Using the Configuration</h2>
 *
 *  <p> The configuration of an application is represented by an instance of {@link org.eclipse.microprofile.config.Config}.
 *  The {@link org.eclipse.microprofile.config.Config} can be accessed via the {@link org.eclipse.microprofile.config.ConfigProvider}.
 *
 *  <pre>
 *  Config config = ConfigProvider#getConfig();
 *  String restUrl = config.getValue("myproject.some.endpoint.url", String.class);
 *  </pre>
 *
 *  <p> Of course we also support injection via a JSR-330 DI container:
 *  <pre>
 *  &#064;Inject
 *  &#064;ConfigProperty(name="myproject.some.endpoint.url");
 *  private String restUrl;
 *  </pre>
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @version 1.0
 */
package org.eclipse.microprofile.config;

