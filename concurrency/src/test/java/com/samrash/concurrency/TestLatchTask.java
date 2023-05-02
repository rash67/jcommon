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

package com.samrash.concurrency;

import com.samrash.testing.TestUtils;
import com.samrash.testing.ThreadHelper;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestLatchTask
{
  private LatchTask task;
  private ThreadHelper threadHelper;

  @BeforeMethod(alwaysRun = true)
  public void setUp() throws Exception
  {
    task = new LatchTask();
    threadHelper = new ThreadHelper();
  }

  @Test(groups = "fast")
  public void testSanity() throws Exception
  {
    final AtomicBoolean completed = new AtomicBoolean(false);
    Thread thread = threadHelper.doInThread(
        new Runnable()
        {
          @Override
          public void run()
          {
            try {
              task.await();
              completed.set(true);
            }
            catch (InterruptedException e) {
              Assert.fail("exception", e);
              throw new RuntimeException(e);
            }
          }
        }
    );

    // imperfect way to test for lock, but has to suffice
    TestUtils.waitUntilThreadBlocks(thread);
    // trigger latch
    task.run();
    // wait for thread to complete
    thread.join();
    Assert.assertTrue(completed.get(), "blocked task did not complete normally");
  }
}
