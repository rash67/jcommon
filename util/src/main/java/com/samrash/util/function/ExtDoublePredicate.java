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

import java.util.Objects;
import java.util.function.DoublePredicate;

public interface ExtDoublePredicate<E extends Throwable>
{
  boolean test(double value) throws E;

  default ExtDoublePredicate<E> and(ExtDoublePredicate<E> other)
  {
    Objects.requireNonNull(other);
    return (value) -> test(value) && other.test(value);
  }

  default ExtDoublePredicate<E> negate()
  {
    return (value) -> !test(value);
  }

  default ExtDoublePredicate<E> or(ExtDoublePredicate<E> other)
  {
    Objects.requireNonNull(other);
    return (value) -> test(value) || other.test(value);
  }

  static DoublePredicate quiet(ExtDoublePredicate<?> doublePredicate)
  {
    return (value) -> ExtBooleanSupplier.quiet(() -> doublePredicate.test(value)).getAsBoolean();
  }
}
