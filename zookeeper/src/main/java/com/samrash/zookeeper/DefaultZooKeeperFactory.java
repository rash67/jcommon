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

package com.samrash.zookeeper;

import org.apache.zookeeper.Watcher;

import java.io.IOException;

public class DefaultZooKeeperFactory implements ZooKeeperFactory
{
  private final String zkHostStr;
  private final int timeoutMillis;
  private final int maxRetries;
  private final int retryIntervalMillis;

  public DefaultZooKeeperFactory(
      String zkHostStr,
      int timeoutMillis,
      int maxRetries,
      int retryIntervalMillis
  )
  {
    this.zkHostStr = zkHostStr;
    this.timeoutMillis = timeoutMillis;
    this.maxRetries = maxRetries;
    this.retryIntervalMillis = retryIntervalMillis;
  }

  public DefaultZooKeeperFactory(String zkHostStr, int timeoutMillis)
  {
    this(zkHostStr, timeoutMillis, 3, 1000);
  }

  @Override
  public ZooKeeperIface create(Watcher watcher) throws IOException
  {
    ZooKeeperFactory sinkZooKeeperFactory =
        new BasicZooKeeperFactory(zkHostStr, timeoutMillis);

    ZooKeeperFactory recoveringZooKeeperFactory =
        new RecoveringZooKeeperFactory(
            sinkZooKeeperFactory, maxRetries, retryIntervalMillis
        );

    return recoveringZooKeeperFactory.create(watcher);
  }
}
