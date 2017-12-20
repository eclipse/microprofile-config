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
 *
 */

package org.eclipse.microprofile.config.tck.converters;

/**
 * Class, which is converted using a Lambda based converter.
 *
 * @author <a href="mailto:anatole@apache.org">Anatole Tresch</a>
 */
public class Donald {

    private String name;
    private boolean bool;

    private Donald(String name, boolean bool) {
        this.name = name;
        this.bool = bool;
    }

    /**
     * Ensure constructor cannot be auto-detected/auto-constructed.
     * @param name the name, not null
     * @return a new instance, never null.
     */
    public static Donald iLikeDonald(String name){
        return new Donald(name, true);
    }

    public String getName(){
        return name;
    }

}
