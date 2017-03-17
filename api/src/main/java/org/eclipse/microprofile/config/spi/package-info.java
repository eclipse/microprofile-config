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
 * <p>This package contains classes which are used to extens the standard functionality in a portable way.
 * <p>A user can provide own {@link org.eclipse.microprofile.config.spi.ConfigSource ConfigSources} and
 * {@link org.eclipse.microprofile.config.spi.Converter Converters} to extend the information available in the Config.
 *
 * <p>The package also contains the class {@link org.eclipse.microprofile.config.spi.ConfigProviderResolver}
 * which is used to pick up the actual implementation.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @version 1.0
 */
package org.eclipse.microprofile.config.spi;
