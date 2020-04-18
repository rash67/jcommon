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
package com.samrash.collections.specialized;

import com.samrash.collections.SetFactory;

import java.util.Set;

public class SnapshotableSetImplFactory<T> implements SetFactory<T, SnapshotableSet<T>> {

  private final SetFactory<T, Set<T>> factory;

  public SnapshotableSetImplFactory(SetFactory<T, Set<T>> factory) {
    this.factory = factory;
  }

  @Override
  public SnapshotableSet<T> create() {
    return new SnapshotableSetImpl<T>(factory.create(), this);
  }
}
