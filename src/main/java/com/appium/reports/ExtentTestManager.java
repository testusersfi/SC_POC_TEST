package com.appium.reports;

import java.util.HashMap;
import java.util.Map;

import com.relevantcodes.extentreports.ExtentTest;

public class ExtentTestManager {
  // new
  static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();

  public static synchronized ExtentTest getTest() {
    return extentTestMap.get((int) (long) (Thread.currentThread().getId()));
  }

  public static synchronized void endTest() {
    ExtentManager.getInstance().getReporter()
        .endTest(extentTestMap.get((int) (long) (Thread.currentThread().getId())));
  }

  public static synchronized void endTest(ExtentTest test) {
    ExtentManager.getInstance().getReporter().endTest(test);
  }

  public static synchronized ExtentTest startTest(String testName) {
    return startTest(testName, "");
  }

  public static synchronized ExtentTest startTest(String testName, String desc) {
    ExtentTest test = ExtentManager.getInstance().getReporter().startTest(testName, desc);
    extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);

    return test;
  }
}
