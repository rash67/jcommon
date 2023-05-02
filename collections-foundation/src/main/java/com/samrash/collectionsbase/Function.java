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

package com.samrash.collectionsbase;

/**
 * full function capability: take an input, product an output, and throw checked exceptions or even
 * Error types if necessary
 *
 * @param <K>
 * @param <V>
 * @param <E>
 */
public interface Function<K, V, E extends Throwable>
{
  V execute(K input) throws E;
}
