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

import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableDuration;

import java.util.Arrays;

public class CompositeMin extends AbstractCompositeCounter<EventCounter>
    implements EventCounter
{

  public CompositeMin(
      ReadableDuration maxLength, ReadableDuration maxChunkLength
  )
  {
    super(maxLength, maxChunkLength);
  }

  public CompositeMin(ReadableDuration maxLength)
  {
    super(maxLength);
  }

  @Override
  public EventCounter merge(EventCounter counter)
  {
    if (counter instanceof CompositeMin) {
      return internalMerge(
          ((CompositeMin) counter).getEventCounters(),
          new CompositeMin(getMaxLength(), getMaxChunkLength())
      );
    } else {
      return internalMerge(
          Arrays.asList(counter),
          new CompositeMin(getMaxLength(), getMaxChunkLength())
      );
    }
  }

  @Override
  protected EventCounter nextCounter(
      ReadableDateTime start, ReadableDateTime end
  )
  {
    return new MinEventCounter(start, end);
  }

  @Override
  public synchronized long getValue()
  {
    trimIfNeeded();

    long min = Long.MAX_VALUE;

    for (EventCounter eventCounter : getEventCounters()) {
      min = Math.min(min, eventCounter.getValue());
    }

    return min;
  }
}
