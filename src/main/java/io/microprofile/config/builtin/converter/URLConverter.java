/*******************************************************************************
 * Copyright 2016 Microprofile.io
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


package io.microprofile.config.builtin.converter;

import io.microprofile.config.spi.ConvertException;
import io.microprofile.config.spi.Converter;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * Convert the string to a Double or throw ConvertException if unable to convert
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 *
 */
public class URLConverter implements Converter<URL>{

	public URL convert(String value) throws ConvertException {	
		URL url = null;
		try {
			url = new URL(value);
		} catch (MalformedURLException mfue ) {
			throw new ConvertException(mfue);
		}
		return url;
	}

}
