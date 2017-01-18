/*******************************************************************************
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

package io.microprofile.config.spi;

import io.microprofile.config.Config;
import io.microprofile.config.ConfigProvider.ConfigBuilder;

import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * Service provider for ConfigProviderResolver. The implementation registers
 * itself via {@link java.util.ServiceLoader} mechanism.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 * @author <a href="mailto:rmannibucau@apache.org">Romain Manni-Bucau</a>
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public abstract class ConfigProviderResolver {
	private static final ThreadLocal<ServiceLoader<ConfigProviderResolver>> threadLoader = new ThreadLocal<ServiceLoader<ConfigProviderResolver>>() {
		@Override
		protected ServiceLoader<ConfigProviderResolver> initialValue() {
			return ServiceLoader.load(ConfigProviderResolver.class);
		}
	};

	protected ConfigProviderResolver() {
	}

	private static ConfigProviderResolver instance = null;

	public abstract Config getConfig();

	public abstract Config getConfig(ClassLoader loader);

	public abstract ConfigBuilder getEmptyBuilder();

	public abstract ConfigBuilder getBuilder();

	public abstract void releaseConfig(Config config);

	/**
	 * Creates a ConfigProviderResolver object
	 *
	 * @return
	 */
	public static ConfigProviderResolver instance() {
		if (instance == null) {
			ServiceLoader<ConfigProviderResolver> sl = threadLoader.get();
			for (ConfigProviderResolver cpr : sl) {
				if (instance != null) {
					Logger.getLogger(ConfigProviderResolver.class.getName())
							.warning(
									"Multiple ConfigProviderResolver found. Ignoring "
											+ cpr.getClass().getName());
				} else {
					instance = cpr;
				}
			}
			if (instance == null) {
				throw new IllegalStateException(
						"No ConfigProviderResolver implementation found!");
			}
		}
		return instance;

	}

	/**
	 * Set the instance. It is used by OSGi environment while service loader
	 * pattern is not supported.
	 *
	 * @param resolver
	 *            set the instance.
	 */
	public static void setInstance(ConfigProviderResolver resolver) {
		instance = resolver;
	}
}