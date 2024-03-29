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

package com.samrash.util.function;

import com.samrash.util.ExtRunnable;

import java.util.Objects;
import java.util.function.LongConsumer;

public interface ExtLongConsumer<E extends Throwable>
{
  void accept(long value) throws E;

  default ExtLongConsumer<E> andThen(ExtLongConsumer<E> after)
  {
    Objects.requireNonNull(after);
    return (value) -> {
      accept(value);
      after.accept(value);
    };
  }

  static LongConsumer quiet(ExtLongConsumer<?> longConsumer)
  {
    return (value) -> ExtRunnable.quiet(() -> longConsumer.accept(value)).run();
  }
}
