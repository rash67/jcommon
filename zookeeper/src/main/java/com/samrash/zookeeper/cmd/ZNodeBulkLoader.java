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

import com.samrash.util.StreamImporter;
import com.samrash.zookeeper.path.ZkGenericPath;
import com.samrash.zookeeper.convenience.ZkQuickConnectionManager;
import com.samrash.zookeeper.convenience.ZkScript;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public class ZNodeBulkLoader extends ZkScript {
  private volatile boolean verbose = false;

  public ZNodeBulkLoader(ZkQuickConnectionManager zkQuickConnectionManager) {
    super(zkQuickConnectionManager);
  }

  public ZNodeBulkLoader() {
    this(new ZkQuickConnectionManager());
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  public void load(File template)
    throws InterruptedException, IOException, KeeperException {
    InputStream in = new FileInputStream(template);
    try {
      load(in);
    } finally {
      in.close();
    }
  }

  public void load(InputStream in)
    throws InterruptedException, IOException, KeeperException {
    load(StreamImporter.importLines(in));
  }

  public void load(List<String> pathStrs)
    throws InterruptedException, KeeperException {
    for (String pathStr : pathStrs) {
      createEntirePath(pathStr);
    }
  }

  private void createEntirePath(String pathStr)
    throws InterruptedException, KeeperException {
    ZkGenericPath path = ZkGenericPath.parse("/", pathStr);
    Iterator<ZkGenericPath> lineageIter = path.lineageIterator();
    while (lineageIter.hasNext()) {
      String currentPathStr = lineageIter.next().toString();
      try {
        getZk().create(
          currentPathStr,
          null,
          ZooDefs.Ids.OPEN_ACL_UNSAFE,
          CreateMode.PERSISTENT
        );
      } catch (KeeperException.NodeExistsException e) {
        // Ignore if it already exist
      }
    }
    if (verbose) {
      System.out.println("Created ZNode: " + pathStr);
    }
  }

  @Override
  protected String getName() {
    return ZNodeBulkLoader.class.getName();
  }

  @Override
  protected Options getSpecificOptions() {
    Options options = new Options();
    options.addOption(
      "f",
      "file-template",
      true,
      "Path to file containing new-line delimited list of ZNodes to create " +
        "(recursive). If this parameter is not specified, expects the values " +
        "to be provided via standard input."
    );
    options.addOption(
      "v",
      "verbose",
      false,
      "Print verbose messages [Default: off]"
    );
    return options;
  }

  @Override
  protected boolean verifySpecificOptions(CommandLine cmd) {
    if (cmd.hasOption("file-template")) {
      File template = new File(cmd.getOptionValue("file-template"));
      if (!template.exists()) {
        System.err.println("Error: invalid file-template path.\n");
        return false;
      }
    }
    return true;
  }

  @Override
  protected void runScript(CommandLine cmd) throws Exception {
    setVerbose(cmd.hasOption("verbose"));
    if (cmd.hasOption("file-template")) {
      File template = new File(cmd.getOptionValue("file-template"));
      load(template);
    } else {
      load(System.in);
    }
  }

  public static void main(String[] args) throws Exception {
    ZkScript script = new ZNodeBulkLoader();
    script.runMain(args);
    System.out.println("DONE");
  }
}
