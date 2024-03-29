/*
 * Copyright(C) 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samrash.util;

import org.apache.commons.lang.StringEscapeUtils;

public class StringUtils
{
  private StringUtils()
  {
    throw new AssertionError();
  }

  public static String stripQuotes(String input)
  {
    if (input.startsWith("'") || input.startsWith("\"")) {
      return StringEscapeUtils.unescapeJava(input.substring(1, input.length() - 1));
    } else {
      return input;
    }
  }
}
