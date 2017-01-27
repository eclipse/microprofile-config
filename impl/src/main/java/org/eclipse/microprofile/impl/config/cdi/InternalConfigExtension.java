/*
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
 */
package org.eclipse.microprofile.impl.config.cdi;


import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

import org.eclipse.microprofile.config.spi.ConfigExtension;

/**
 * This is needed since some containers don't scan for beans in jars which are in container libs.
 *
 * @author <a href="mailto:struberg@apache.org">Mark Struberg</a>
 */
public class InternalConfigExtension implements Extension {

    public void addConfigBeans(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        abd.addBean(new ConfigBean(bm.getExtension(ConfigExtension.class).getConfig()));
    }
}
