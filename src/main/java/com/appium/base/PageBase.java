package com.appium.base;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.annotation.values.ElementDescription;
import com.annotation.values.PageName;
import com.relevantcodes.extentreports.LogStatus;
import com.report.factory.ExtentTestManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.HideKeyboardStrategy;

public abstract class PageBase {
  public static AppiumDriver<MobileElement> driver;
  BasePageObjects basePageObjects = new BasePageObjects();

  public PageBase(AppiumDriver<MobileElement> driver) {
    this.driver = driver;
    PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS),
        basePageObjects);
  }

  public static WebElement getElementWhenVisible(WebDriver driver, MobileElement mobileElement) {
    WebElement el = null;
    WebDriverWait wait = new WebDriverWait(driver, 20);
    el = wait.until(ExpectedConditions.visibilityOf(mobileElement));
    return el;
  }

  public static void waitUntilElementsAreInvisible(List<WebElement> mobileElement) {
    WebDriverWait wait = new WebDriverWait(driver, 20);
    wait.until(ExpectedConditions.invisibilityOfAllElements(mobileElement));
  }


  protected static boolean isElementPresent(MobileElement mobileElement) {
    Boolean elementPresent = true;
    try {
      elementPresent = mobileElement.isDisplayed();
    } catch (UnsupportedCommandException | NoSuchElementException ignored) {
      elementPresent = false;
    }
    return elementPresent;
  }


  public static void acceptPermissionAlertAndroid() {
    try {
      String alertText =
          driver.findElementById("com.android.packageinstaller:id/permission_message").getText();
      driver.findElementById("com.android.packageinstaller:id/permission_allow_button").click();
      Reporter.log("Alert Text: " + alertText);
    } catch (Exception e) {
      Reporter.log("Exception while Accepting Alert: " + e.getStackTrace());
    }
  }
 
  public void takeScreenShot(String fileName) {
    File file = new File(fileName + ".png");
    File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    try {
      FileUtils.copyFile(tmpFile, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean waitForPageToLoad(AppiumDriver<MobileElement> driver,
      WebElement mobileElement) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, 30);
      wait.until(ExpectedConditions.visibilityOf(mobileElement));
      return true;
    } catch (TimeoutException | NoSuchElementException e) {
      return false;
    }
  }

  public static MobileElement waitForElement(AppiumDriver<MobileElement> driver,
      MobileElement mobileElement) {
    waitForPageToLoad(driver, mobileElement);
    MobileElement el = mobileElement;
    return el;
  }


  public void threadSleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }


  public void hideKeyboardBasedOnPlatform() {
    if (Utils.getDriverPlatform(driver).equals("AndroidDriver")) {
      try {
        Utils.log("Android > Trying to hide Keyboard");
        driver.hideKeyboard();
      } catch (Exception ea) {
        ea.printStackTrace();
      }
    } else if (Utils.getDriverPlatform(driver).equals("IOSDriver")) {
      try {
        Utils.log("iOS > Trying to hide Keyboard using 'PRESS_KEY > Done'");
        ((IOSDriver<MobileElement>) driver).hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Done");
      } catch (Exception ei1) {
        try {
          Utils.log("iOS > Trying to hide Keyboard using 'PRESS_KEY > Go'");
          ((IOSDriver<MobileElement>) driver).hideKeyboard(HideKeyboardStrategy.PRESS_KEY, "Go");
        } catch (Exception ei2) {
          try {
            Utils.log("iOS > Trying to hide Keyboard using 'TAP_OUTSIDE'");
            ((IOSDriver<MobileElement>) driver).hideKeyboard(HideKeyboardStrategy.TAP_OUTSIDE, "");
          } catch (Exception ei3) {
            try {
              Utils.log("iOS > Trying to hide Keyboard using native means");
              driver.hideKeyboard();
            } catch (Exception ei4) {
              ei4.printStackTrace();
            }
          }
        }
      }
    }
  }

  public void logStepIntoExtentReport(String elementDescription, String action, String typeString) {
    ExtentTestManager.getTest().log(LogStatus.INFO, withBoldHTML(action),
        elementDescription + "; " + withBoldHTML("Text") + ": " + typeString);
  }

  public String withBoldHTML(String string) {
    if (!string.trim().isEmpty()) {
      return "<b>" + string + "</b>";
    } else {
      return "";
    }
  }

  public String getPageObjectElementDescription(Object pageObject, String fieldName) {
    try {
      return this.getClass().getAnnotation(PageName.class).value() + "::" + pageObject.getClass()
          .getField(fieldName).getAnnotation(ElementDescription.class).value();
    } catch (NoSuchFieldException e) {

      e.printStackTrace();
    }
    return "";
  }
}
