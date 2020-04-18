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

import com.samrash.util.digest.LongMurmur3Hash;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestLongMurmur3Hash {

  @Test(groups = "fast")
  public void testConsistency() throws Exception {
    LongMurmur3Hash longMurmur3Hash1 = new LongMurmur3Hash();
    LongMurmur3Hash longMurmur3Hash2 = new LongMurmur3Hash();

    for (long i = 0; i < 1000; i++) {
      Assert.assertEquals(
        longMurmur3Hash1.computeDigest(i),
        longMurmur3Hash2.computeDigest(i),
        String.format("Hashes don't match for %d", i)
      );
    }
  }
}
