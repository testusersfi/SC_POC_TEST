package com.appium.test.runner;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.appium.base.Utils;
import com.appium.manager.ParallelThread;

public class RunnerMultiTestNG {

  @Test
  public void testRunner() throws Exception {
    Utils.RUNNER_MODE = Utils.PROPERTIES.getProperty("RUNNER");
    ParallelThread parallelThread = new ParallelThread();
    List<String> test = new ArrayList<String>();
    /*
     * test.add("MultiLoginTest"); test.add("AddCommentTest");
     */
    parallelThread.runner(Utils.PROPERTIES.getProperty("TEST.PACKAGE") + ".tests");
  }

}
