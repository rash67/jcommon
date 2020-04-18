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

import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableDuration;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Keeps a list of gauge counters (see AbstractCompositeCounter)
 * that look like one summarized gauge counter.
 */
public class CompositeGaugeCounter extends AbstractCompositeSum<GaugeCounter>
  implements GaugeCounter {
  private final GaugeCounterFactory gaugeCounterFactory;

  public CompositeGaugeCounter(
    ReadableDuration maxLength,
    ReadableDuration maxChunkLength,
    GaugeCounterFactory gaugeCounterFactory
  ) {
    super(maxLength, maxChunkLength);
    this.gaugeCounterFactory = gaugeCounterFactory;
  }

  public CompositeGaugeCounter(
    ReadableDuration maxLength, GaugeCounterFactory gaugeCounterFactory
  ) {
    super(maxLength);
    this.gaugeCounterFactory = gaugeCounterFactory;
  }

  public CompositeGaugeCounter(ReadableDuration maxLength) {
    this(maxLength, DefaultGaugeCounterFactory.INSTANCE);
  }

  public synchronized CompositeEventCounterIf<GaugeCounter> add(
    long delta, long nsamples, ReadableDateTime start, ReadableDateTime end
  ) {
    GaugeCounter counter = nextCounter(start, end);
    counter.add(delta, nsamples);
    return addEventCounter(counter);
  }

  /**
   * mirrors AbstractCompositeCounter:add()
   */
  @Override
  public void add(long delta, long nsamples) {
    DateTime now = new DateTime();
    GaugeCounter last;

    synchronized (this) {
      if (getMostRecentCounter() == null ||
          !now.isBefore(getMostRecentCounter().getEnd())) {
        addEventCounter(nextCounter(now, now.plus(getMaxChunkLength())));
      }
      last = getMostRecentCounter();
    }
    last.add(delta, nsamples);
  }

  /**
   * mirrors AbstractCompositeCounter:getValue()
   */
  @Override
  public synchronized long getSamples() {
    long nsamples = 0L;

    trimIfNeeded();

    Iterator<GaugeCounter> counterIterator = getEventCounters().iterator();
    boolean first = true;
    while (counterIterator.hasNext()) {
      GaugeCounter counter = counterIterator.next();
      nsamples += counter.getSamples();

      if (first) {
        // if there is at least one counter that is partially expired
        if (getWindowStart().isAfter(counter.getStart())) {
          // adjust for partial expiration
          nsamples -=
            (long) (getExpiredFraction(counter) * counter.getSamples());
        }
        first = false;
      }
    }

    return nsamples;
  }

  public synchronized long getAverage() {
    long nsamples = getSamples();
    if (nsamples == 0) {
      return 0;
    }
    long value = getValue();
    return value / nsamples;
  }

  @Override
  protected GaugeCounter nextCounter(
    ReadableDateTime start, ReadableDateTime end
  ) {
    return gaugeCounterFactory.create(start, end);
  }

  @Override
  public GaugeCounter merge(GaugeCounter counter) {
    // special case to handle merging of 2 composite counters
    if (counter instanceof CompositeGaugeCounter) {
      return internalMerge(
        ((CompositeGaugeCounter) counter).getEventCounters(),
        new CompositeGaugeCounter( getMaxLength(), getMaxChunkLength(), gaugeCounterFactory)
      );
    } else {
      return internalMerge(
        Arrays.asList(counter),
        new CompositeGaugeCounter(getMaxLength(), getMaxChunkLength(), gaugeCounterFactory)
      );
    }
  }
}
