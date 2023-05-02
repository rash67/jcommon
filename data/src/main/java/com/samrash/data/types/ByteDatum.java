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


import com.samrash.util.serialization.SerDe;
import com.samrash.util.serialization.SerDeException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@SuppressWarnings({"NumericCastThatLosesPrecision"})
public class ByteDatum implements Datum
{
  private final byte value;

  public ByteDatum(byte value)
  {
    this.value = value;
  }

  @Override
  public boolean asBoolean()
  {
    return value != 0;
  }

  @Override
  public byte asByte()
  {
    return value;
  }

  @Override
  public short asShort()
  {
    return value;
  }

  @Override
  public int asInteger()
  {
    return value;
  }

  @Override
  public long asLong()
  {
    return value;
  }

  @Override
  public float asFloat()
  {
    return (float) value;
  }

  @Override
  public double asDouble()
  {
    return (double) value;
  }

  @Override
  public String asString()
  {
    return String.valueOf(value);
  }

  @Override
  public byte[] asBytes()
  {
    return new byte[]{value};
  }

  @Override
  public boolean isNull()
  {
    return false;
  }

  @Override
  public DatumType getType()
  {
    return DatumType.BYTE;
  }

  @Override
  public Object asRaw()
  {
    return value;
  }

  @Override
  public int hashCode()
  {
    return Byte.valueOf(value).hashCode();
  }

  @Override
  public String toString()
  {
    return asString();
  }

  @Override
  public boolean equals(Object obj)
  {
    return obj != null && obj instanceof Datum &&
           value == ((Datum) obj).asByte();
  }

  @Override
  public int compareTo(Datum o)
  {
    if (o == null) {
      return 1;
    }

    return Integer.signum(value - o.asByte());
  }

  public static class SerDeImpl implements SerDe<Datum>
  {
    @Override
    public Datum deserialize(DataInput in) throws SerDeException
    {
      try {
        return new ByteDatum(in.readByte());
      }
      catch (IOException e) {
        throw new SerDeException(e);
      }
    }

    @Override
    public void serialize(Datum value, DataOutput out)
        throws SerDeException
    {
      try {
        out.writeByte(value.asByte());
      }
      catch (IOException e) {
        throw new SerDeException(e);
      }
    }
  }
}
