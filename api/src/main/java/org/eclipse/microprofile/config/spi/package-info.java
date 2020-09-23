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
 */

/**
 * This package contains classes which are used to implement the configuration API, and to extend the standard
 * configuration functionality in a portable way.
 * <p>
 * Users and frameworks may provide custom {@link org.eclipse.microprofile.config.spi.ConfigSource} and
 * {@link org.eclipse.microprofile.config.spi.Converter} instances. Configuration instances may be set up and created
 * using the {@link org.eclipse.microprofile.config.spi.ConfigBuilder} API.
 * <p>
 * The package also contains the class {@link org.eclipse.microprofile.config.spi.ConfigProviderResolver}, which is used
 * to implement the specification itself.
 *
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 *
 */
@org.osgi.annotation.versioning.Version("2.0")
package org.eclipse.microprofile.config.spi;
