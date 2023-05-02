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

package com.samrash.logging;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestLoggerImpl
{
  private static final Logger LOG = LoggerImpl.getClassLogger();

  @Test(groups = "fast")
  public void testMagicSanity() throws Exception
  {
    LOG.info("logger created from static scope");

    Assert.assertEquals(LOG.getName(), getClass().getName());
  }

  @Test(groups = "fast")
  public void testLocalLoggerGetsClass() throws Exception
  {
    Logger privateLogger = LoggerImpl.getClassLogger();

    privateLogger.info("private logger");

    Assert.assertEquals(privateLogger.getName(), getClass().getName());
  }

  @Test(groups = "fast")
  public void testMessageWithPercentChar() throws Exception
  {
    // A random message with % in it
    String message = "A: 50%, B: 80%";
    Logger privateLogger = LoggerImpl.getClassLogger();

    // this should not cause an UnknownFormatConversionException
    privateLogger.info(message);
  }
}
