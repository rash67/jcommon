/*
 * Copyright (C) 2014 Facebook, Inc.
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

package com.samrash.tools.example;

import com.facebook.swift.service.ThriftMethod;

@SuppressWarnings("InterfaceNeverImplemented")
@com.facebook.swift.service.ThriftService
public interface CheckpointManager extends AutoCloseable
{

  public String getCheckpoint(String application, String environment, int shard);

  @ThriftMethod
  public void deleteCheckpoint(String application, String environment, int shard);

  @Override
  void close();
}
