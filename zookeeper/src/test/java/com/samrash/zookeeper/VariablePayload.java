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

package com.samrash.zookeeper;

public class VariablePayload implements Encodable
{
  private String strData;

  public VariablePayload(String strData)
  {
    this.strData = strData;
  }

  public void setPayload(String strData)
  {
    this.strData = strData;
  }

  @Override
  public byte[] encode()
  {
    return ZkUtil.stringToBytes(strData);
  }

  public static String decode(byte[] byteData)
  {
    return ZkUtil.bytesToString(byteData);
  }
}
