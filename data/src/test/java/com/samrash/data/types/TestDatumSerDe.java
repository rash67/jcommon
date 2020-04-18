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
package com.samrash.data.types;

import com.samrash.util.serialization.SerDeException;
import com.samrash.util.serialization.SerDeUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

public class TestDatumSerDe {

  private DatumSerDe serDe;
  private BooleanDatum trueDatum;
  private BooleanDatum falseDatum;
  private LongDatum longDatum;
  private ShortDatum shortDatum;
  private IntegerDatum integerDatum;
  private FloatDatum floatDatum;
  private DoubleDatum doubleDatum;
  private StringDatum stringDatum;
  private ByteDatum byteDatum;
  private ListDatum listDatum;

  @BeforeMethod(alwaysRun = true)
  public void setUp() throws Exception {
    serDe = new DatumSerDe();
    trueDatum = new BooleanDatum(true);
    falseDatum = new BooleanDatum(false);
    byteDatum = new ByteDatum((byte) 99);
    shortDatum = new ShortDatum((short) 100);
    integerDatum = new IntegerDatum(100000);
    longDatum = new LongDatum(300000000L);
    floatDatum = new FloatDatum(32.7f);
    doubleDatum = new DoubleDatum(10.12312415211431);
    stringDatum = new StringDatum("Fuu");
    listDatum = new ListDatum(
      Arrays.asList(
        integerDatum,
        longDatum,
        stringDatum
      )
    );
  }

  @Test(groups = "fast")
  public void testSanity() throws Exception {
    testValue(trueDatum);
    testValue(falseDatum);
    testValue(byteDatum);
    testValue(shortDatum);
    testValue(integerDatum);
    testValue(longDatum);
    testValue(floatDatum);
    testValue(doubleDatum);
    testValue(stringDatum);
    testValue(listDatum);
  }

  private void testValue(Datum datum) throws SerDeException {
    Datum processedValue = SerDeUtils.deserializeFromBytes(
      SerDeUtils.serializeToBytes(datum, serDe), serDe
    );
    Assert.assertEquals(processedValue, datum);
  }
}
