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

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class BlockingValueProducer<V, E extends Exception> implements Callable<V> {
  private final V value;
  private final E ex;
  private final CountDownLatch latch;
  private AtomicInteger calledCount = new AtomicInteger(0);
  private AtomicInteger completedCount = new AtomicInteger(0);

  BlockingValueProducer(V value, boolean blocked, E ex) {
    this.value = value;
    this.ex = ex;

    if (blocked) {
      latch = new CountDownLatch(1);
    } else {
      latch = new CountDownLatch(0);
    }
  }

  BlockingValueProducer(V value, boolean blocked) {
    this(value, blocked, null);
  }

  BlockingValueProducer(V value) {
    this(value, false, null);
  }

  @Override
  public V call() throws E {
    try {
      latch.await();
    } catch (InterruptedException e) {
      // Ignore
    }
    calledCount.incrementAndGet();

    if (ex != null) {
      throw ex;
    }

    completedCount.incrementAndGet();

    return value;
  }

  public int getCalledCount() {
    return calledCount.get();
  }

  public int getCompletedCount() {
    return completedCount.get();
  }

  public void signal() {
    latch.countDown();
  }
}
