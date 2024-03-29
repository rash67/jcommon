/*
 * Copyright (C) 2014 Facebook, Inc.
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

package com.samrash.tools.io;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Adds a {@link #printfln(String, Object...)} method to {@link java.io.PrintStream} and removes
 * the {@link java.io.IOException} from {@link #write(byte[])}.
 */
public abstract class PrintStreamPlus extends PrintStream
{
  protected PrintStreamPlus(OutputStream out)
  {
    super(out);
  }

  /**
   * Convenience method identical to {#code printf(format, args); println();}.
   */
  public abstract void printfln(String format, Object... args);

  @Override
  public void write(byte[] buffer)
  {
    write(buffer, 0, buffer.length);
  }
}
