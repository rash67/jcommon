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

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WrappedScheduledFuture<V> implements ScheduledFuture<V>
{
  private final ScheduledFuture<V> delegate;

  public WrappedScheduledFuture(ScheduledFuture<V> delegate)
  {
    this.delegate = delegate;
  }

  @Override
  public long getDelay(TimeUnit unit)
  {
    return delegate.getDelay(unit);
  }

  @Override
  public int compareTo(Delayed o)
  {
    return delegate.compareTo(o);
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    return delegate.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled()
  {
    return delegate.isCancelled();
  }

  @Override
  public boolean isDone()
  {
    return delegate.isDone();
  }

  @Override
  public V get() throws InterruptedException, ExecutionException
  {
    return delegate.get();
  }

  @Override
  public V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException
  {
    return delegate.get(timeout, unit);
  }
}
