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

/*
 * modified by Sam Rash (rash67) 2023
 */

package com.samrash.concurrency.linearization;

import java.util.concurrent.TimeUnit;

/**
 * similar to a write lock, will block other ConcurrentPoints and
 * LinearizationPoints.
 * <p>
 * * NOTE: use start/complete in a try/finally block the same as Lock
 */
public interface LinearizationPoint
{
  void start();

  void complete();

  void waitForStart() throws InterruptedException;

  boolean waitForStart(long timeout, TimeUnit unit) throws InterruptedException;

  void waitForCompletion() throws InterruptedException;

  boolean waitForCompletion(long timeout, TimeUnit unit) throws InterruptedException;
}
