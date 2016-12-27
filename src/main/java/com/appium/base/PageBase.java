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

  public enum SWIPE_V_OPTIONS {
    UP, DOWN
  }

  public enum SWIPE_H_OPTIONS {
    LEFT, RIGHT
  }

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

  public static int randomFunc(int n) {
    Random random = new Random();
    return random.nextInt(n);
  }


  public void rotateLandscape() {
    driver.rotate(ScreenOrientation.LANDSCAPE);
  }

  public void rotatePortrait() {
    driver.rotate(ScreenOrientation.PORTRAIT);
  }

  public String getOrientation() {
    String screenOrientation = driver.getOrientation().toString();
    Utils.log("Screen Orientation = " + screenOrientation);
    return screenOrientation;
  }

  public void swipeHorizontalUntilTextExists(String expected) {
    do {
      swipingHorizontal();
    } while (!driver.getPageSource().contains(expected));
  }

  public static boolean isElementPresent(By by) {
    Boolean iselementpresent = driver.findElements(by).size() != 0;
    return iselementpresent;
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

  public boolean isElementNotPresent(By by) {
    try {
      driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
      return driver.findElements(by).isEmpty();
    } finally {
      driver.manage().timeouts().implicitlyWait(
          Integer.parseInt(Utils.PROPERTIES.getProperty("IMPLICIT_WAIT_SECONDS")),
          TimeUnit.SECONDS);
    }
  }

  public static boolean isPermissionAlertPresentAndroid() {
    try {
      WebDriverWait wait = new WebDriverWait(driver, 3);
      wait.until(ExpectedConditions
          .presenceOfElementLocated(By.id("com.android.packageinstaller:id/permission_message")));
      driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
      Utils.captureInterimScreenshot(driver);
      Reporter.log("((AndroidDriver<MobileElement>) driver).currentActivity() = "
          + ((AndroidDriver<MobileElement>) driver).currentActivity());
      if (((AndroidDriver<MobileElement>) driver).currentActivity()
          .contains(".permission.ui.GrantPermissionsActivity")) {
        Reporter.log("Alert found with Activity :.permission.ui.GrantPermissionsActivity");
        return true;
      } else {
        return false;
      }
    } catch (NoAlertPresentException | TimeoutException e) {
      driver.manage().timeouts().implicitlyWait(
          Integer.parseInt(Utils.PROPERTIES.getProperty("IMPLICIT_WAIT_SECONDS")),
          TimeUnit.SECONDS);
      Utils.captureInterimScreenshot(driver);
      return false;
    }
  }

  public static boolean isAlertPresentAndroid() {
    try {
      driver.findElementByXPath(
          "//android.widget.TextView[contains(@resource-id,'android:id/message')]");
      return true;
    } catch (NoAlertPresentException | TimeoutException | NoSuchElementException e) {
      return false;
    }
  }

  public static boolean isAlertPresentIOS() {
    try {
      driver.switchTo().alert();
      Reporter.log("Alert found");
      return true;
    } catch (NoAlertPresentException | TimeoutException | NoSuchElementException e) {
      try {
        driver.findElementByXPath("//UIAAlert");
        return true;
      } catch (NoAlertPresentException | TimeoutException | NoSuchElementException e1) {
        return false;
      }
    }
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

  public static void acceptAlertAndroid(String acceptButtonText) {
    try {
      String alertText = driver.findElementById("android:id/message").getText();
      driver
          .findElementByXPath("//android.widget.Button[contains(@text,'" + acceptButtonText + "')]")
          .click();
      Reporter.log("Alert Text: " + alertText);
    } catch (Exception e) {
      Reporter.log("Exception while Accepting Alert: " + e.getStackTrace());
    }
  }

  public static void acceptAlertIOS(String acceptButtonText) {
    try {
      String alertText =
          driver.findElementByXPath("//UIAAlert/UIAScrollView/UIAStaticText[1]").getText();
      driver.findElementByXPath(
          "//UIAAlert/UIACollectionView/UIACollectionCell/UIAButton[contains(@name,'"
              + acceptButtonText + "')]")
          .click();
      Reporter.log("Alert Text: " + alertText);
    } catch (Exception e) {
      Reporter.log("Exception while Accepting Alert: " + e.getStackTrace());
    }
  }

  public static String Date_Time_inStringFormat() {
    return (new SimpleDateFormat("dd:MM:yyyy HH:mm:ss").format(new Date())).replaceAll(":", "")
        .replaceAll(" ", "");
  }

  public static String Date_inStringFormat() {
    return (new SimpleDateFormat("dd:MM:yyyy").format(new Date())).replaceAll(":", "_")
        .replaceAll(" ", "");
  }

  public void swipingHorizontal() {

    // Get the size of screen.
    Dimension size;
    size = driver.manage().window().getSize();
    Utils.log("driver.manage().window().getSize() = " + size);

    // Find swipe start and end point from screen's with and height.
    // Find startx point which is at right side of screen.
    int startx = (int) (size.width * 0.70);
    // Find endx point which is at left side of screen.
    int endx = (int) (size.width * 0.30);
    // Find vertical point where you wants to swipe. It is in middle of
    // screen height.
    int starty = size.height / 2;
    Utils.log("startx = " + startx + " ,endx = " + endx + " , starty = " + starty);

    // Swipe from Right to Left.
    driver.swipe(startx, starty, endx, starty, 3000);
    threadSleep(2000);

    // Swipe from Left to Right.
    driver.swipe(endx, starty, startx, starty, 3000);
    threadSleep(2000);
  }

 

  public String getCurrentMethodName() {
    return Thread.currentThread().getStackTrace()[2].getMethodName();
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

  public void goBack() {
    if (Utils.getDriverPlatform(driver).equals("AndroidDriver")) {
      ((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
    } else if (Utils.getDriverPlatform(driver).equals("IOSDriver")) {
      if (isElementPresent(basePageObjects.IOS_DONE)) {
        basePageObjects.IOS_DONE.click();
      } else {
        basePageObjects.IOS_BACK.click();
      }
    }
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
