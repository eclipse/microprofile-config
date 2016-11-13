/*******************************************************************************
 * Copyright (c) 2016 IBM Corporation and others.
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
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Emily Jiang - newly created class
 *******************************************************************************/


package io.microprofile.config;
/**
 * This is the central class to access a {@link Config}.
 * @author Emily
 *
 */
public interface ConfigProvider {

	/**
	 * Create a builder so that custom config resources can be added.
	 * @return an instance {@link ConfigBuilder}
	 */
	ConfigBuilder builder();
	/**
	 * Get the {@link Config} after aggregating on multiple {@link ConfigSource}s
	 * @return the {@link Config} containing all config properties
	 */
	Config getConfig();
}
