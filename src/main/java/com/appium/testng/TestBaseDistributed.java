package com.appium.testng;

import java.io.File;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.appium.base.JSonParser;
import com.appium.base.Utils;
import com.appium.manager.AppiumParallelTest;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class TestBaseDistributed extends AppiumParallelTest {

  // protected AppiumDriver<MobileElement> driver;

  public TestBaseDistributed() throws Exception {

  }

  JSonParser jSonParser = new JSonParser();

  @BeforeSuite
  public void beforeSuite() throws Exception {

  }

  @AfterSuite
  public void afterSuite() throws Exception {

  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod(Method name) throws Exception {
    driver = startAppiumServerInParallel(name.getName(), iosNative(), androidNative());
    driver.manage().timeouts().implicitlyWait(
        Integer.parseInt(Utils.PROPERTIES.getProperty("IMPLICIT_WAIT_SECONDS")), TimeUnit.SECONDS);
    startLogResults(name.getName());
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult result) throws Exception {
    endLogTestResults(result);
    getDriver().quit();
  }

  public AppiumDriver<MobileElement> getDriver() {
    return driver;
  }

  @BeforeClass()
  public void beforeClass() throws Exception {
    // startAppiumServer(className);
  }

  @AfterClass()
  public void afterClass() throws Exception {
    // killAppiumServer();
  }

  public String getUserName() {
    String[] crds = Thread.currentThread().getName().toString().split("_");
    System.out.println(crds[1]);
    JSONObject user = jSonParser.getUserData(Integer.parseInt(crds[1]));
    return user.get("userName").toString();
  }

  public String getPassword() {
    String[] crds = Thread.currentThread().getName().toString().split("_");
    JSONObject user = jSonParser.getUserData(Integer.parseInt(crds[1]));
    return user.get("password").toString();
  }

  public synchronized DesiredCapabilities androidNative() {
    System.out.println("Setting Android Desired Capabilities:");
    DesiredCapabilities androidCapabilities = new DesiredCapabilities();
    androidCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
    androidCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "5.X");
    androidCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
        Utils.PROPERTIES.getProperty("APP_ACTIVITY"));
    androidCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
        Utils.PROPERTIES.getProperty("APP_PACKAGE"));
    androidCapabilities.setCapability("browserName", "");
    checkSelendroid(androidCapabilities);
    androidCapabilities.setCapability(MobileCapabilityType.APP,
        new File(Utils.PROPERTIES.getProperty("ANDROID_APP_PATH")).getAbsolutePath());
    androidCapabilities.setCapability("chromedriverExecutable",
        Utils.PROPERTIES.getProperty("CHROME_DRIVER_EXECUTABLE"));
    androidCapabilities.setCapability("recreateChromeDriverSessions", true);
    androidCapabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT, true);
    androidCapabilities.setCapability(MobileCapabilityType.UDID, device_udid);
    return androidCapabilities;
  }

  public synchronized DesiredCapabilities iosNative() {
    DesiredCapabilities iOSCapabilities = new DesiredCapabilities();
    System.out.println("Setting iOS Desired Capabilities:");
    iOSCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.0");
    iOSCapabilities.setCapability(MobileCapabilityType.APP,
        new File(Utils.PROPERTIES.getProperty("IOS_APP_PATH")).getAbsolutePath());
    iOSCapabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID,
        Utils.PROPERTIES.getProperty("BUNDLE_ID"));
    iOSCapabilities.setCapability(IOSMobileCapabilityType.AUTO_ACCEPT_ALERTS, false);
    iOSCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
    iOSCapabilities.setCapability(MobileCapabilityType.UDID, device_udid);
    return iOSCapabilities;
  }
}
