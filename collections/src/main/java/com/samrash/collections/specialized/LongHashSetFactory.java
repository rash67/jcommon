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

public class LongHashSetFactory implements SetFactory<Long, SnapshotableSet<Long>> {
  public static final int DEFAULT_INITIAL_SIZE = 4;
  public static final int DEFAULT_MAX_SIZE = 8000;

  private final int initialSize;
  private final int maxSetSize;

  public LongHashSetFactory(int initialSize, int maxSetSize) {
    this.initialSize = initialSize;
    this.maxSetSize = maxSetSize;
  }

  public LongHashSetFactory(int maxSetSize) {
    this(DEFAULT_INITIAL_SIZE, maxSetSize);
  }

  public static LongHashSetFactory withInitialSize(int initialSize) {
    return new LongHashSetFactory(initialSize, DEFAULT_MAX_SIZE);
  }

  public static LongHashSetFactory withMaxSize(int maxSize) {
    return new LongHashSetFactory(DEFAULT_INITIAL_SIZE, maxSize);
  }

  @Override
  public SnapshotableSet<Long> create() {
    // we don't want more than 20% of maxSetSize allocated
    return new LongHashSet(initialSize, (int)((1.2f)*maxSetSize));
  }
}
