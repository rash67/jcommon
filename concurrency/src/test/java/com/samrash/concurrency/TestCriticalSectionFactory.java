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
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.CountDownLatch;

public class TestCriticalSectionFactory
{
  private int count;
  private CountDownLatch criticalSectionLatch;
  private CountDownLatch entryLatch;
  private Runnable runnable;
  private CriticalSectionFactory factory;

  @BeforeMethod(alwaysRun = true)
  private void setup()
  {
    count = 0;
    criticalSectionLatch = new CountDownLatch(1);
    entryLatch = new CountDownLatch(1);
    runnable = new Runnable()
    {
      @Override
      public void run()
      {
        try {
          entryLatch.countDown();
          criticalSectionLatch.await();
        }
        catch (InterruptedException e) {
          Assert.fail("test interrupted");
        }

        count++;
      }
    };
    factory = new CriticalSectionFactory();
  }

  @Test(groups = "fast")
  public void testCriticalSectionFactory() throws InterruptedException
  {
    final Runnable criticalSection = factory.wrap(runnable);

    // t1 will stop in the critical section
    Thread t1 = TestUtils.runInThread(criticalSection);
    entryLatch.await();
    // now start t2 and wait until it skips critical section
    Thread t2 = TestUtils.runInThread(criticalSection);
    t2.join();
    // let t1 out of critical section
    criticalSectionLatch.countDown();
    t1.join();

    // only t1 should make it through critical section
    Assert.assertEquals(count, 1, "too many critical section calls");
  }
}
