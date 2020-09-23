/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package org.eclipse.microprofile.config.tck.converters.implicit;

/**
 * Part of the implicit Converter test.
 *
 * Sample class which has a parse(CharSequence) and Constructor(String) method. The method parse(CharSequence) should be
 * used instead of constructor(String).
 *
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */
public class ConvTestSequenceParseBeforeConstructor {
    private String val;

    public static ConvTestSequenceParseBeforeConstructor parse(CharSequence val) {
        ConvTestSequenceParseBeforeConstructor o = new ConvTestSequenceParseBeforeConstructor();
        o.val = val.toString();
        return o;
    }

    ConvTestSequenceParseBeforeConstructor() {
    }

    public ConvTestSequenceParseBeforeConstructor(String val) {
        this.val = "constructor";
    }

    public String getVal() {
        return val;
    }
}
