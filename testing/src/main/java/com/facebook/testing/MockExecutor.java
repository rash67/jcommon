/*
 * Copyright (C) 2012 Facebook, Inc.
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
package com.facebook.testing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MockExecutor implements ScheduledExecutorService {
  private final List<AnnotatedRunnable> runnableList =
    Collections.synchronizedList(new ArrayList<AnnotatedRunnable>());
  private final CountDownLatch latch = new CountDownLatch(1);
  private final IdentityHashMap<ScheduledFuture<?>, ScheduledFuture<?>> 
    outstandingTasks = new IdentityHashMap<ScheduledFuture<?>, ScheduledFuture<?>>();

  private volatile boolean rejectSubmission = false;
  private volatile boolean isShutdown = false;
  private volatile boolean isTerminated = false;

  public List<AnnotatedRunnable> getRunnableList() {
    return runnableList;
  }

  public void setShutdown(boolean shutdown) {
    isShutdown = shutdown;
  }

  public void setTerminated(boolean terminated) {
    isTerminated = terminated;
  }

  public void setRejectSubmission(boolean rejectSubmission) {
    this.rejectSubmission = rejectSubmission;
  }

  public void triggerShutdownLatch() {
    latch.countDown();
  }

  public AnnotatedRunnable removeHead() {
    return runnableList.isEmpty() ? null : runnableList.remove(0);
  }

  public int getNumPendingTasks() {
    return runnableList.size();
  }

  @Override
  public ScheduledFuture<?> schedule(final Runnable command, long delay, TimeUnit unit) {
    if (rejectSubmission) {
      throw new RejectedExecutionException();
    }

    runnableList.add(new AnnotatedRunnable(command, delay, delay, unit));

    return new MockScheduledFuture<Void>(toCallable(command));
  }

  private <T> Callable toCallable(final Runnable runnable, final T result) {
    return new Callable<T>() {
      @Override
      public T call() throws Exception {
        runnable.run();

        return result;
      }
    };

  }

  private Callable<Void> toCallable(Runnable runnable) {
    return toCallable(new AnnotatedRunnable(runnable), (Void) null);
  }

  @Override
  public <V> ScheduledFuture<V> schedule(
    Callable<V> callable, long delay, TimeUnit unit
  ) {
    if (rejectSubmission) {
      throw new RejectedExecutionException();
    }

    MockScheduledFuture<V> future = new MockScheduledFuture<>(callable);
    AnnotatedRunnable runnable = new AnnotatedRunnable(future, delay, delay, unit);
    
    runnable.setFuture(future);
    runnableList.add(runnable);
    outstandingTasks.put(future, future);
    
    return future;
  }

  @Override
  public ScheduledFuture<?> scheduleAtFixedRate(
    Runnable command, long initialDelay, long period, TimeUnit unit
  ) {
    if (rejectSubmission) {
      throw new RejectedExecutionException();
    }

    AnnotatedRunnable runnable =
      new AnnotatedRunnable(command, initialDelay, period, unit);

    runnableList.add(runnable);

    MockScheduledFuture<Void> future = new MockScheduledFuture<Void>(
      Executors.<Void>callable(runnable, (Void) null)
    );

    runnable.setFuture(future);
    outstandingTasks.put(future, future);

    return future;
  }

  @Override
  public ScheduledFuture<?> scheduleWithFixedDelay(
    Runnable command, long initialDelay, long delay, TimeUnit unit
  ) {
    if (rejectSubmission) {
      throw new RejectedExecutionException();
    }
    AnnotatedRunnable runnable =
      new AnnotatedRunnable(command, initialDelay, delay, unit);

    runnableList.add(runnable);

    MockScheduledFuture<Void> future = new MockScheduledFuture<Void>(
      Executors.<Void>callable(runnable, (Void) null)
    );

    runnable.setFuture(future);
    outstandingTasks.put(future, future);

    return future;
  }

  @Override
  public void shutdown() {
    cancelPendingTasks();

    isShutdown = true;
  }

  @Override
  public List<Runnable> shutdownNow() {
    cancelPendingTasks();
    
    return new ArrayList<Runnable>(runnableList);
  }

  private void cancelPendingTasks() {
    for (IdentityHashMap.Entry<ScheduledFuture<?>, ScheduledFuture<?>> entry :
      outstandingTasks.entrySet()
      ) {
      entry.getValue().cancel(false);
    }
  }

  @Override
  public boolean isShutdown() {
    return isShutdown;
  }

  @Override
  public boolean isTerminated() {
    return isTerminated;
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit)
    throws InterruptedException {
    return true;
  }

  @Override
  public <T> Future<T> submit(final Callable<T> task) {
    runnableList.add(new AnnotatedRunnable(new Runnable() {
      @Override
      public void run() {
        try {
          task.call();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }));
    
    return new MockScheduledFuture<T>(task);
  }

  @Override
  public <T> Future<T> submit(Runnable task, T result) {
    runnableList.add(new AnnotatedRunnable(task));

    return new MockScheduledFuture<T>(toCallable(task, result));
  }

  @Override
  public Future<?> submit(Runnable task) {
    runnableList.add(new AnnotatedRunnable(task));

    return new MockScheduledFuture<Void>(toCallable(task));
  }

  @Override
  public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
    throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> List<Future<T>> invokeAll(
    Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit
  ) throws InterruptedException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
    throws InterruptedException, ExecutionException {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T invokeAny(
    Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit
  ) throws InterruptedException, ExecutionException, TimeoutException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void execute(Runnable command) {
    schedule(command, 0, TimeUnit.SECONDS);
  }

  /**
   * runs all tasks in the executor in the current thread
   */
  public void drain() {
    while (getNumPendingTasks() > 0) {
      removeHead().run();
    }
  }

  public void drain(int maxTasks) {
    for (int i = 0; i < maxTasks && getNumPendingTasks() > 0; i++)
      removeHead().run();
  }
}
