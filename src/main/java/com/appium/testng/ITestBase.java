package com.appium.testng;

import java.lang.reflect.Method;

import org.testng.ITestResult;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public interface ITestBase {

  void beforeSuite() throws Exception;

  void afterSuite() throws Exception;

  void beforeClass(String className) throws Exception;

  void afterClass() throws Exception;

  void beforeMethod(Method name) throws Exception;

  void afterMethod(ITestResult result) throws Exception;

  AppiumDriver<MobileElement> getDriver();

}
