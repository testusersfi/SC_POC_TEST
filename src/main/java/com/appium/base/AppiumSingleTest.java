package com.appium.base;

import static org.testng.AssertJUnit.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;

public class AppiumSingleTest {
  protected AppiumDriver<MobileElement> driver = null;
  protected static AppiumDriverLocalService service;

  public static String APPIUM_SERVER_ADDRESS = "127.0.0.1";

  public AppiumDriver<MobileElement> initializeDriver() throws IOException {

    if (Utils.PROPERTIES.getProperty("platform").equals("android")) {
      androidSetup();
    } else if (Utils.PROPERTIES.getProperty("platform").equals("ios")) {
      iosSetup();
    } else {
      Utils.log("ERROR: The platform specified is invalid.");
      Utils.log(
          "Try providing the platform name from ant commandline args (e.g. ant -Dplatform=ios) or local.properties or config.properties");
    }
    driver.manage().timeouts().implicitlyWait(
        Integer.parseInt(Utils.PROPERTIES.getProperty("IMPLICIT_WAIT_SECONDS")), TimeUnit.SECONDS);
    return driver;
  }

  public void androidSetup() throws MalformedURLException {
    DesiredCapabilities capabilities = DesiredCapabilities.android();
    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM);
    capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
    capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION,
        Utils.PROPERTIES.getProperty("ANDROID_PLATFORM_VERSION"));
    capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
        Utils.PROPERTIES.getProperty("ANDROID_APP_PACKAGE"));
    capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_PACKAGE,
        Utils.PROPERTIES.getProperty("ANDROID_APP_PACKAGE"));
    capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
        Utils.PROPERTIES.getProperty("ANDROID_APP_ACTIVITY"));

    //capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 200);

   // capabilities.setCapability("chromedriverExecutable",
   //     Utils.PROPERTIES.getProperty("CHROME_DRIVER_EXECUTABLE"));
   // capabilities.setCapability("browserName", "");
    capabilities.setCapability("autoAcceptAlerts", true);
    capabilities.setCapability("recreateChromeDriverSessions", true);
    capabilities.setCapability(MobileCapabilityType.TAKES_SCREENSHOT, true);
    capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Browser");


    File classpathRoot = new File(System.getProperty("user.dir"));
    File app = new File(Utils.PROPERTIES.getProperty("ANDROID_APP_PATH"));
    capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
    Utils.log("----APK Absolute Path: " + app.getAbsolutePath());
    
    
    driver =
        new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
  }

  public void iosSetup() throws MalformedURLException {
    DesiredCapabilities capabilities = new DesiredCapabilities();

    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
    capabilities.setCapability("platformVersion", "9.3");
    capabilities.setCapability("deviceName", "iPhone 6");
    File classpathRoot = new File(System.getProperty("user.dir"));
    File app = new File(classpathRoot, Utils.PROPERTIES.getProperty("IOS_APP_PATH"));
    capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
    Utils.log("----IPA Absolute Path: " + app.getAbsolutePath());
    driver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
  }

  private static File findCustomNode() {
    Platform current = Platform.getCurrent();
    if (current.is(Platform.WINDOWS)) {
      return new File(String.valueOf(Utils.PROPERTIES.get("path.to.custom.node.win")));
    } else {
      return new File(String.valueOf(Utils.PROPERTIES.get("path.to.custom.node.mac")));
    }
  }

  private static File findCustomAppiumJs() {
    Platform current = Platform.getCurrent();
    if (current.is(Platform.WINDOWS)) {
      return new File(String.valueOf(Utils.PROPERTIES.get("path.to.custom.appium.js.win")));
    } else {
      return new File(String.valueOf(Utils.PROPERTIES.get("path.to.custom.appium.js.mac")));
    }
  }

public static void startAppiumServer() throws InterruptedException {
	    File node = findCustomNode();
	    File appiumjs = findCustomAppiumJs();
	//String Appium_Node_Path= "C:/Program Files (x86)/Appium/node.exe";
	//String Appium_JS_Path="C:/Program Files (x86)/Appium/node_modules/appium/bin/appium.js";
    //String Appium_JS_Path="C:/Program Files (x86)/Appium/node_modules/appium/lib/server/main.js";
    File logdir = Utils.getChildDir("log");
    File appiumlog = new File(logdir, "appiumServerLog.txt");
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("autoAcceptAlerts", true);
    service = AppiumDriverLocalService
        .buildService(new AppiumServiceBuilder().usingDriverExecutable(node).withAppiumJS(appiumjs)
            .withIPAddress(APPIUM_SERVER_ADDRESS).usingPort(4723).withLogFile(appiumlog));
    Utils.log("Service URL: " + service.getUrl().toString());
    Thread.sleep(12000);
    service.start();
    Utils.log("Service running: " + service.isRunning());
    Utils.log("Service URL:" + service.getUrl());
    assertEquals(true, service.isRunning());
  }
  
/*  public static void startAppiumServer() throws InterruptedException {
	  try
      {
         Utils.log("Setting Appium Server");
         CommandLine command = new CommandLine("cmd");
         command.addArgument("/c");
         command.addArgument("C://Appium//node.exe");
         command.addArgument("C://Appium//node_modules//appium//bin//appium.js");
         command.addArgument("--address");
         command.addArgument("127.0.0.1");
         command.addArgument("--port");
         command.addArgument("4723");
         command.addArgument("--no-reset");
         command.addArgument("--log");
         File dir = new File(".");
         command.addArgument(dir.getCanonicalPath() + "//appiumLogs.txt");
         DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
         DefaultExecutor executor = new DefaultExecutor();
         executor.setExitValue(1);
         executor.execute(command, resultHandler);
         Thread.sleep(60000*2);
         
      } catch (Exception e)
      {
         Utils.log("Exception startAppiumServer() : " + e.getMessage());
      }

}*/

  public static void stopAppiumServer() {
    if (service != null) {
      service.stop();
    }
  }
  
}
