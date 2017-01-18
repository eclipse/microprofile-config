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
/**
 * Thrown to indicate that converting to a specified type is unsuccessful.
 * @author <a href="mailto:emijiang@uk.ibm.com">Emily Jiang</a>
 */

package io.microprofile.config.spi;

public final class ConvertException extends Exception
{

  /**
   * The exception was thrown when unable to convert to the specified type. 
   */
  private static final long serialVersionUID = 1L;

  public ConvertException(String message)
  {
    super(message);
  }

  public ConvertException(Throwable throwable)
  {
    super(throwable);
  }

  public ConvertException(String message, Throwable throwable)
  {
    super(message, throwable);
  }
}
