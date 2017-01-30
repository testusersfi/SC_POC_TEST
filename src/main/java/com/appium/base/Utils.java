package com.appium.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.report.factory.ExtentTestManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class Utils {


  public static String RUNNER_MODE = "standalone";

  private static ExtentReports extent;
  // These are not used for now, as it is inherited from
  // AppiumTestDistribution
  public static String APPIUM_SERVER_ADDRESS = "127.0.0.1";
  public static String EXTENT_REPORTS_DIRNAME;
  public static String EXTENT_REPORTS_FILENAME;
  public static String SCREENSHOTS_DIRNAME;

  // These holds the properties
  public static Properties PROPERTIES;
  private static Properties configProperties;
  private static Properties appProperties;
  private static Properties localProperties;

  static {
    loadProperties();
  }

  public static void loadProperties() {
    // Load from config.properties
    String defaultPropertyFileName = "config.properties";
    Utils.log("Default Property File Name : " + defaultPropertyFileName);
    configProperties = doLoadPropertyFile(defaultPropertyFileName);
    PROPERTIES = configProperties;

    // Load from local.properties (to get APPNAME)
    String localPropertyFileName = "local.properties";
    Utils.log("Local Property File Name : " + localPropertyFileName);
    localProperties = doLoadPropertyFile(localPropertyFileName);
    PROPERTIES.putAll(localProperties);

    // Super impose App specific properties here
    String appPropertyFileName =
        "app_" + PROPERTIES.getProperty("APPNAME").toLowerCase() + ".properties";
    Utils.log("App Property File Name : " + appPropertyFileName);
    appProperties = doLoadPropertyFile(appPropertyFileName);
    PROPERTIES.putAll(appProperties);

    // Finally we overwrite them with local properties as that is the final
    Utils.log("Final say - Local Property File : " + localPropertyFileName);
    PROPERTIES.putAll(localProperties);

    Utils.log("--------Property file values---------");
    Utils.log("----APPNAME ------" + PROPERTIES.getProperty("APPNAME"));
    Utils.log("----Runner ------" + PROPERTIES.getProperty("RUNNER"));
    Utils.log("----Platform----" + PROPERTIES.getProperty("platform"));
    Utils.log("----APK PATH----" + PROPERTIES.getProperty("ANDROID_APP_PATH"));
    Utils.log("----IPA PATH----" + PROPERTIES.getProperty("IOS_APP_PATH"));
    Utils.log("----User Dir----" + System.getProperty("user.dir"));

    EXTENT_REPORTS_DIRNAME =
        getChildDir(getChildDir("test-reports"), "extent-reports").getAbsolutePath();
    EXTENT_REPORTS_FILENAME = new File(new File(EXTENT_REPORTS_DIRNAME),
        "TestAppExtentReport_" + Utils.PROPERTIES.getProperty("platform") + ".html")
            .getAbsolutePath();
    SCREENSHOTS_DIRNAME = getChildDir(getChildDir("target"), "screenshot_int").getAbsolutePath();


  }

  private static Properties doLoadPropertyFile(String propertyFileName) {
    File f = new File(propertyFileName);
    if (f.exists() && !f.isDirectory()) {
      FileInputStream fis = null;
      Properties tmpProperty = new Properties();
      try {
        fis = new FileInputStream(propertyFileName);
        tmpProperty.load(fis);
      } catch (FileNotFoundException e) {
        Utils.log("Property File: " + propertyFileName + " is NotFound");
      } catch (IOException e) {
        Utils.log("IOEXCeption");
      } finally {
        if (fis != null) {
          try {
            fis.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      return tmpProperty;
    } else {
      return null;
    }
  }

  public static String convertStringsForSingleQuoteInText(String Converted_XPathCompatible) {
    Utils.log("before Converted_XPathCompatible2 : " + Converted_XPathCompatible);
    Converted_XPathCompatible = Converted_XPathCompatible.replaceAll("'", "\'");
    Utils.log("After Converted_XPathCompatible2 : " + Converted_XPathCompatible);
    return Converted_XPathCompatible;
  }

  public static void log(String logMessage) {
	    System.out.println(logMessage);
	    // Reporter.log(logMessage);
	    if (ExtentTestManager.getTest() != null) {
	      ExtentTestManager.getTest().log(LogStatus.INFO, logMessage + "<br>");
	    }
	  }

public static void captureInterimScreenshot(AppiumDriver<MobileElement> driver) {
  File scrTmp, scrFile;
  scrTmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
  Utils.log("ScrTmp location :" +scrTmp.toString());
  String methodName = new Exception().getStackTrace()[1].getMethodName();
  Utils.log("method name :" +methodName);
  String className = new Exception().getStackTrace()[1].getClassName();
  className = className.substring(className.lastIndexOf('.') + 1);
  Utils.log("class name :" +className);
  scrFile = new File(new File(SCREENSHOTS_DIRNAME),
      Utils.getDriverPlatform(driver).replaceFirst("Driver", "") + "_interim_"
          + Utils.getCurrentDateAndTime() + "_" + className + "_" + methodName + ".png");
  String scrFileRelPath;
  try {
    FileUtils.copyFile(scrTmp, scrFile);
  } catch (IOException e) {
    e.printStackTrace();
  }
  scrFileRelPath = Utils.convertToRelativePath(Utils.EXTENT_REPORTS_DIRNAME, scrFile.getAbsolutePath());
  Utils.log("scrFileRelPath :" +scrFileRelPath.toString());
  Utils.getExtentTest().log(LogStatus.INFO,
          "Intermediate Screenshot => " + Utils.getExtentTest().addScreenCapture(scrFileRelPath));
}

public synchronized static ExtentTest getExtentTest() {
	    if (Utils.RUNNER_MODE.equalsIgnoreCase("standalone")) {
	      return com.appium.reports.ExtentTestManager.getTest();
	    } else {
	      return com.report.factory.ExtentTestManager.getTest();
	    }
	  }
public static File getChildDir(String dirName) {
  return getChildDir(new File(System.getProperty("user.dir")), dirName);
}

public static File getChildDir(File parentDir, String dirName) {
  File childDir = new File(parentDir, dirName);
  if (!childDir.exists()) {
    childDir.mkdirs();
  }
  return childDir;
}

public static String convertToRelativePath(String basePath, String filePath) {
  StringBuilder relativePath = null;

  // Thanks to:
  // http://mrpmorris.blogspot.com/2007/05/convert-absolute-path-to-relative-path.html
  basePath = basePath.replaceAll("\\\\", "/");
  filePath = filePath.replaceAll("\\\\", "/");

  if (basePath.equals(filePath) == true) {

  } else {
    String[] absoluteDirectories = basePath.split("/");
    String[] relativeDirectories = filePath.split("/");

    // Get the shortest of the two paths
    int length = absoluteDirectories.length < relativeDirectories.length
        ? absoluteDirectories.length : relativeDirectories.length;

    // Use to determine where in the loop we exited
    int lastCommonRoot = -1;
    int index;

    // Find common root
    for (index = 0; index < length; index++) {
      if (absoluteDirectories[index].equals(relativeDirectories[index])) {
        lastCommonRoot = index;
      } else {
        break;
        // If we didn't find a common prefix then throw
      }
    }
    if (lastCommonRoot != -1) {
      // Build up the relative path
      relativePath = new StringBuilder();
      // Add on the ..
      for (index = lastCommonRoot + 1; index < absoluteDirectories.length; index++) {
        if (absoluteDirectories[index].length() > 0) {
          relativePath.append("../");
        }
      }
      for (index = lastCommonRoot + 1; index < relativeDirectories.length - 1; index++) {
        relativePath.append(relativeDirectories[index] + "/");
      }

      int lastPiece = relativeDirectories.length - 1;
      if (lastPiece > lastCommonRoot) {
        relativePath.append(relativeDirectories[lastPiece]);
      }
    }
  }
  return relativePath == null ? null : relativePath.toString();
}

public static String getCurrentDateAndTime() {
  return new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new java.util.Date());
}

public static String getDriverPlatform(AppiumDriver<MobileElement> driver) {
  Utils.log("driver.getCapabilities().getPlatform().toString() = "
      + driver.getCapabilities().getPlatform().toString());
  Utils.log("Utils.getDriverPlatform(driver) = " + driver.toString().split(":")[0].trim());
  return driver.toString().split(":")[0].trim();
}


public synchronized static ExtentReports initialiseExtentReports() {
  if (Utils.RUNNER_MODE.equalsIgnoreCase("standalone")) {
    return com.appium.reports.ExtentManager.getInstance()
        .getReporter(Utils.EXTENT_REPORTS_FILENAME);
  } else {
    extent = com.report.factory.ExtentManager.getInstance();
    ExtentTestManager.loadConfig();
    return extent;
  }
}


public synchronized static void closeExtentReports() {
  if (Utils.RUNNER_MODE.equalsIgnoreCase("standalone")) {
    com.appium.reports.ExtentManager.getInstance().closeAndBackUp();
  } else {
    com.report.factory.ExtentManager.getInstance().close();
  }
}

public static synchronized ExtentTest startExtentTest(String testName, String desc) {
  if (Utils.RUNNER_MODE.equalsIgnoreCase("standalone")) {
    return com.appium.reports.ExtentTestManager.startTest(testName, desc);
  } else {
    return com.report.factory.ExtentTestManager.startTest(testName, desc, "deviceid");
  }
}

public synchronized static ExtentReports getExtentReports() {
  if (Utils.RUNNER_MODE.equalsIgnoreCase("standalone")) {
    return com.appium.reports.ExtentManager.getInstance().getReporter();
  } else {
    return com.report.factory.ExtentManager.getInstance();
  }
}

}
