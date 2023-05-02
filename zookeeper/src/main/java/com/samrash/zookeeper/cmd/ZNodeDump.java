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

package com.samrash.zookeeper.cmd;

import com.samrash.zookeeper.ZkUtil;
import com.samrash.zookeeper.convenience.ZkQuickConnectionManager;
import com.samrash.zookeeper.convenience.ZkScript;
import com.samrash.zookeeper.path.ZkGenericPath;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class ZNodeDump extends ZkScript
{
  public ZNodeDump(ZkQuickConnectionManager zkQuickConnectionManager)
  {
    super(zkQuickConnectionManager);
  }

  public ZNodeDump()
  {
    this(new ZkQuickConnectionManager());
  }

  public void dump(String pathStr, int depth, boolean detailed)
      throws InterruptedException, KeeperException
  {
    ZkGenericPath path = new ZkGenericPath(pathStr);
    dump(path, depth, detailed);
  }

  public void dump(ZkGenericPath path, int depth, boolean detailed)
      throws InterruptedException, KeeperException
  {
    try {
      print(path.toString(), detailed);
      if (depth > 0) {
        List<String> children = getZk().getChildren(path.toString(), null);
        for (String child : children) {
          dump(path.appendChild(child), depth - 1, detailed);
        }
      }
    }
    catch (KeeperException.NoNodeException e) {
      // Ignore those that were deleted
    }
  }

  private void print(String zNodePath, boolean detailed)
      throws InterruptedException, KeeperException
  {
    if (detailed) {
      printDetailed(zNodePath);
    } else {
      printSimple(zNodePath);
    }
  }

  private void printSimple(String zNodePath)
  {
    System.out.println(zNodePath);
  }

  private void printDetailed(String zNodePath)
      throws InterruptedException, KeeperException
  {
    Stat stat = new Stat();
    byte[] rawData = getZk().getData(zNodePath, null, stat);
    String data =
        (rawData == null) ? "<NULL>" : "'" + ZkUtil.bytesToString(rawData) + "'";
    System.out.print(String.format("%-40s ", zNodePath));
    System.out.print(String.format("Data: %-20s ", data));
    System.out.print(String.format("Data Length: %-5d ", stat.getDataLength()));
    System.out.print(String.format("NumChildren: %-5d ", stat.getNumChildren()));
    System.out.print(String.format("Children Changes: %-20d ", stat.getCversion()));
    System.out.print(String.format("Ephemeral Owner: %-20d ", stat.getEphemeralOwner()));
    System.out.print(String.format("Version: %-20d ", stat.getVersion()));
    System.out.print(String.format("Time Modified: %-20d ", stat.getMtime()));
    System.out.print(String.format("Time Created: %-20d", stat.getCtime()));
    System.out.print("\n");
  }

  @Override
  protected String getName()
  {
    return ZNodeDump.class.getName();
  }

  @Override
  protected Options getSpecificOptions()
  {
    Options options = new Options();
    options.addOption(
        "z",
        "zkpath",
        true,
        "ZooKeeper root path to initiate dump [Required]"
    );
    options.addOption(
        "d",
        "depth",
        true,
        "Max descendant depth to traverse [Default: INTEGER_MAX]"
    );
    options.addOption(
        "t",
        "detailed",
        false,
        "Use this flag to print the contents of each ZNode [Default: off]"
    );
    return options;
  }

  @Override
  protected boolean verifySpecificOptions(CommandLine cmd)
  {
    if (!cmd.hasOption("zkpath")) {
      System.err.println("Error: You must specify a ZooKeeper path.\n");
      return false;
    }
    if (cmd.hasOption("depth")) {
      try {
        if (Integer.parseInt(cmd.getOptionValue("depth")) < 0) {
          System.err.println("Error: depth parameter must be non-negative.\n");
          return false;
        }
      }
      catch (NumberFormatException e) {
        System.err.println("Error: depth parameter is not an integer.\n");
        return false;
      }
    }
    return true;
  }

  @Override
  protected void runScript(CommandLine cmd) throws Exception
  {
    String zkPath = cmd.getOptionValue("zkpath");
    int depth =
        cmd.hasOption("depth")
        ? Integer.parseInt(cmd.getOptionValue("depth"))
        : Integer.MAX_VALUE;
    boolean detailed = cmd.hasOption("detailed");
    dump(zkPath, depth, detailed);
  }

  public static void main(String[] args) throws Exception
  {
    ZkScript script = new ZNodeDump();
    script.runMain(args);
    System.out.println("DONE");
  }
}
