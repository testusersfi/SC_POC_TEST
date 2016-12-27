package mwo.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import com.appium.base.PageBase;
import com.appium.base.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import com.report.factory.ExtentTestManager;

import mwo.pageobjects.LoginPageObjects;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class LoginPage extends PageBase {
  LoginPageObjects loginPageObjects = new LoginPageObjects();

  public LoginPage(AppiumDriver<MobileElement> driver) {
    super(driver);
    PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS),
        loginPageObjects);
  }

  public void loginFUnctionality(String Email, String Pwd) {
	  waitForPageToLoad(driver, loginPageObjects.SCREEN_HEADER);
	  Utils.captureInterimScreenshot(driver);
	  
	  // Clear and enter text in UserName Field
      loginPageObjects.LOGIN_USERNAME_FIELD.clear();
      loginPageObjects.LOGIN_USERNAME_FIELD.click();
      loginPageObjects.LOGIN_USERNAME_FIELD.sendKeys(Email);
      Utils.log("Login Page > Username Textfield is Clicked and entered");
      hideKeyboardBasedOnPlatform();

      // Clear and Enter Text in PassWord Field
      loginPageObjects.LOGIN_PASSWORD_FIELD.clear();
      loginPageObjects.LOGIN_PASSWORD_FIELD.click();
      loginPageObjects.LOGIN_PASSWORD_FIELD.sendKeys(Pwd);
      Utils.log("Login Page > Password Textfield is Clicked and entered");
      hideKeyboardBasedOnPlatform();
      
      // Click on Login Button
      if (isElementPresent(loginPageObjects.SIGNIN_BUTTON)) {
        loginPageObjects.SIGNIN_BUTTON.click();
        Utils.log("Login Page > Log In Button is Clicked");
      }
      
  }
  
  public void validLoginVerification() {
	  if(isElementPresent(By.xpath(String.format(loginPageObjects.LOGIN_STATUS, "Login Successfull")))) {
		  ExtentTestManager.getTest().log(LogStatus.PASS, "Login is successful");
	  } else {
		  ExtentTestManager.getTest().log(LogStatus.FAIL, "Login is failed");
	  }
	  
  }
  
  public void invalidLoginVerification() {
	  if(isElementPresent(By.xpath(String.format(loginPageObjects.LOGIN_STATUS, "Login Failed")))) {
		  ExtentTestManager.getTest().log(LogStatus.PASS, "Login failed with invalid credentials");
	  } else {
		  ExtentTestManager.getTest().log(LogStatus.FAIL, "invalid error");
	  }
  }
  
  public void AppLaunchVerification() {
  LoginPage loginPage = new LoginPage(driver);
  if (Utils.getDriverPlatform(driver).equals("AndroidDriver")) {
      Reporter.log("We are in Android, now checking for Alert messages...");
    } else if (Utils.getDriverPlatform(driver).equals("IOSDriver")) {
      Reporter.log("We are in IOS, now checking for Alert messages...");

    }
  assert loginPageObjects.SCREEN_HEADER.isDisplayed(); 
  assert isElementPresent(By.xpath(String.format(loginPageObjects.LOGIN_STATUS, "No Result")));
    Utils.log("IFS POC app Login screen displayed...");
  }

}
