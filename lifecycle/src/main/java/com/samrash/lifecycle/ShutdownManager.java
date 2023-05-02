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

package com.samrash.lifecycle;

/**
 * allows for objects to register shutdown hooks
 * used instead of direct shutdown hooks in order to
 * manage the order they are called
 * <p>
 * The idea here is that on startup, we register the Thread this provides
 * as a hook in case the JVM is killed.  If the fb303.shutdown() is called,
 * this will execute and remove itself from the jvm shutdown hooks
 */
public interface ShutdownManager<T extends Comparable>
{
  public boolean tryAddShutdownHook(Runnable hook);

  /**
   * attempt to add a shutdown hook to be run at shutdown
   *
   * @param stage - stage to add the hook to
   * @param hook  - hook to run
   * @return true if the hook was added, false if we are already shutting
   * down and the hook was not added
   */
  public boolean tryAddShutdownHook(T stage, Runnable hook);

  public void addShutdownHook(Runnable hook);

  /**
   * add a hook to a given stage
   *
   * @param stage - stage to add the hook to
   * @param hook  - hook to run
   * @throws IllegalArgumentException if shutdown has started and we have
   *                                  passed the specified stage.
   */
  public void addShutdownHook(T stage, Runnable hook);

  public void shutdown();

  public Thread getAsThread();
}
