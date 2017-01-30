package com.appium.reports;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.testng.Reporter;

import com.appium.base.Utils;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.NetworkMode;

public class ExtentManager {
  private ExtentManager() {

  }

  public static ExtentManager getInstance() {
    if (mInstance == null) {
      mInstance = new ExtentManager();
    }
    return mInstance;
  }

  private static ExtentManager mInstance;
  private static String mReportFilePath;
  private ExtentReports extent;
  private ArrayList<String> reporterClassOutput;

  public synchronized ExtentReports getReporter(String filePath) {
    return mInstance.doGetReporter(filePath);
  }

  public synchronized ExtentReports doGetReporter(String filePath) {
    if (extent == null) {
      extent = new ExtentReports(filePath, true, DisplayOrder.NEWEST_FIRST, NetworkMode.OFFLINE);
      mReportFilePath = filePath;
      reporterClassOutput = new ArrayList<String>();
      // extent.startReporter(ReporterType.DB, (new
      // File(filePath)).getParent() + File.separator + "extent.db");
      // extent.x();
    }
    // optional
    // extent.config().documentTitle("Automation
    // Report").reportName("Regression").reportHeadline("");
    loadConfig();
    extent.addSystemInfo("Appium Java Client Version", "4.0.0")
        .addSystemInfo("Appium Server Version", "1.5.3");
    Map<String, String> sysInfo = new HashMap<String, String>();
    sysInfo.put("Platform", Utils.PROPERTIES.getProperty("platform"));
    extent.addSystemInfo(sysInfo);
    return extent;
  }

  public synchronized ExtentReports getReporter() {
    return extent;
  }

  public synchronized void loadConfig() {
    extent.loadConfig(new File(System.getProperty("user.dir") + "/src/test/resources/config/extent-config.xml"));
  }

  public synchronized void logFinalScreenshot(String imgSrc, String headerName) {
    imgSrc =
        "<div class='col l4 m6 s12'><div class='card-panel'><span class='panel-name'><h6 class='md-display-2'>"
            + headerName + "</h6></span><img src='" + imgSrc
            + "' style=\"width:100%;height:100%;\"></div></div>";
    extent.setTestRunnerOutput(imgSrc);
  }

  public synchronized void logRunnerHeading(String reportHeader) {
    reportHeader =
        "<div class='col s12'><div class='card-panel'><span class='panel-name'><h5 class='md-display-2'>"
            + reportHeader + "</span></h5></div></div>";
    extent.setTestRunnerOutput(reportHeader);
  }

  public synchronized void logFinalReporterLog() {
    extent.setTestRunnerOutput("<div class='row'>");
    for (String s : reporterClassOutput) {
      extent.setTestRunnerOutput(s);
    }
    extent.setTestRunnerOutput("</div>");
  }

  public synchronized void logClassReporterOutput(String className) {
    if (!(Reporter.getOutput().isEmpty())) {
      StringBuilder builder = new StringBuilder();
      builder.append(
          "<div class='col l4 m6 s12'><div class='card-panel'><span class='panel-name'><h6 class='md-display-2'>"
              + className + "</h6></span>");
      builder.append(System.lineSeparator());
      for (String s : Reporter.getOutput()) {
        builder.append(s);
        builder.append("<br>");
      }
      builder.append("</div></div>");
      builder.append(System.lineSeparator());
      Reporter.clear();
      reporterClassOutput.add(builder.toString());
    }
  }

  public synchronized void closeAndBackUp() {
    // Close the Extent Report
    extent.close();
    // Take a back-up of current report for future use - Not Needed
    /*
     * File extOrig, extBkup; extOrig = new File(mReportFilePath); extBkup = new
     * File(extOrig.getAbsolutePath().replaceFirst(".html", "_" + Utils.TEST_START_DATE_TIMESTAMP +
     * ".html")); try { FileUtils.copyFile(extOrig, extBkup); } catch (IOException e) {
     * e.printStackTrace(); }
     */
  }
}
