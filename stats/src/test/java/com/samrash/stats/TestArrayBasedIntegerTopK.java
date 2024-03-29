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

package com.samrash.stats;

import com.samrash.logging.Logger;
import com.samrash.logging.LoggerImpl;
import com.samrash.stats.topk.ArrayBasedIntegerTopK;
import com.samrash.stats.topk.TopK;

public class TestArrayBasedIntegerTopK extends TestIntegerTopK
{
  private static final Logger LOG = LoggerImpl.getLogger(TestArrayBasedIntegerTopK.class);

  protected TopK<Integer> getInstance(int keySpaceSize, int k)
  {
    return new ArrayBasedIntegerTopK(keySpaceSize, k);
  }

  @Override
  protected Logger getLogger()
  {
    return LOG;
  }
}
