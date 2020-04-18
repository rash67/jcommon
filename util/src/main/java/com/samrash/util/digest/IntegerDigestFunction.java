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
package com.samrash.util.digest;

public class IntegerDigestFunction implements DigestFunction<Integer> {
  private final MurmurHash hasher = new MurmurHash(MurmurHash.JCOMMON_SEED);

  @Override
  public long computeDigest(Integer input) {
    return hasher.hash(input);
  }
}
