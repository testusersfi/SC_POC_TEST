package com.appium.testng;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.annotation.values.Description;
import com.appium.base.AppiumSingleTest;
import com.appium.base.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class TestBaseStandalone extends AppiumSingleTest {

  protected static ExtentReports extent;
  protected static ExtentTest parent;
  protected static ExtentTest child;
  protected File scrTmp;
  protected File scrFile;
  private static String testClassName;
  protected AppiumDriver<MobileElement> driver;
  
  private Map<Long, ExtentTest> parentContext = new HashMap<Long, ExtentTest>();

  public TestBaseStandalone() throws Exception {

  }

  public AppiumDriver<MobileElement> getDriver() {
    return this.driver;
  }

  /*
   * Before suite
   * 
   */
  @BeforeSuite(alwaysRun = true)
  public void beforeSuite() throws Exception {
	    extent = Utils.initialiseExtentReports();
	    startAppiumServer();
    // ExtentManager.getInstance().logRunnerHeading("Final Screenshots");
    // extent.setTestRunnerOutput("<div class='row'>");
  }

  /*
   * After suite will be responsible to close the report properly at the end You can have another
   * afterSuite as well in the derived class and this one will be called in the end making it the
   * last method to be called in test exe
   */
  @AfterSuite(alwaysRun = true)
  public void afterSuite() throws Exception {
    stopAppiumServer();
   extent.setTestRunnerOutput("</div");
    // ExtentManager.getInstance().logRunnerHeading("Reporter Output (Before
    // & After Class Method Logs)");
    // ExtentManager.getInstance().logFinalReporterLog();
    // ExtentManager.getInstance().closeAndBackUp();
   Utils.closeExtentReports();
  }

  @BeforeClass(alwaysRun = true)
  public void beforeClass() throws Exception {
	    String className = getClass().getSimpleName();
	    String classDescription = "";
	    if (getClass().getAnnotation(Description.class) != null) {
	      classDescription = getClass().getAnnotation(Description.class).value();
	    }
	    parent = Utils.startExtentTest(className, classDescription);
	    parentContext.put(Thread.currentThread().getId(), parent);
  }

  @AfterClass(alwaysRun = true)
  public void afterClass() throws Exception {
	    Utils.getExtentReports().endTest(parent);
	    Utils.getExtentReports().flush();
  }

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod(Method caller) throws Exception {
	  child = Utils.startExtentTest(caller.getName(), caller.getClass().getSimpleName());
	    // From beforeClass
	    driver = super.initializeDriver();
	    Utils.log(">> " + caller.getDeclaringClass().getSimpleName() + "." + caller.getName());
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod(ITestResult result) throws Exception {
		if (result.getStatus() == ITestResult.FAILURE) {
			Utils.getExtentTest().log(LogStatus.FAIL, result.getThrowable());
		} else if (result.getStatus() == ITestResult.SKIP) {
			Utils.getExtentTest().log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
		} else {
			Utils.getExtentTest().log(LogStatus.PASS, "Test passed");
		}
    // Add Final Screenshot
    testClassName = result.getTestClass().getRealClass().getSimpleName();
    captureFinalScreenshot(testClassName, result.getMethod().getMethodName());
    Utils.log("<< " + testClassName + "." + result.getMethod().getMethodName());
    parentContext.get(Thread.currentThread().getId()).appendChild(child);
    Utils.getExtentReports().flush();
    // From afterClass
    driver.quit();
  }

  private void captureFinalScreenshot(String className, String methodName) {
    scrTmp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    scrFile = new File(new File(Utils.SCREENSHOTS_DIRNAME), Utils.PROPERTIES.getProperty("platform")
        + "_final_" + className + "_" + methodName + ".png");
    Utils.log("ScrFile:" + scrFile.toString());
    String scrFileRelPath;
    try {
      FileUtils.copyFile(scrTmp, scrFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
    scrFileRelPath = Utils.convertToRelativePath(Utils.EXTENT_REPORTS_DIRNAME, scrFile.getAbsolutePath());
    Utils.log("ScrFilerelPath:" + scrFileRelPath);
    child.log(LogStatus.INFO, "Final Screenshot => " + parent.addScreenCapture(scrFileRelPath));
  }
}
