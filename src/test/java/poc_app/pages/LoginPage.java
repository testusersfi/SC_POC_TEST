package poc_app.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import com.appium.base.PageBase;
import com.appium.base.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import com.report.factory.ExtentTestManager;

import poc_app.pageobjects.LoginPageObjects;
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

  public HomePage validLoginVerification(String Email, String Pwd) {
	  waitForPageToLoad(driver, loginPageObjects.SCREEN_HEADER);
	  Utils.captureInterimScreenshot(driver);
	  enterUserName(Email);
	  enterPassWord(Pwd);  
      // Click on Login Button
      if (isElementPresent(loginPageObjects.SIGNIN_BUTTON)) {
        loginPageObjects.SIGNIN_BUTTON.click();
        Utils.log("Login Page > Log In Button is Clicked");
      }
      
      return new HomePage(driver);
  }
  
  public void enterUserName(String email) {
	  // Clear and enter text in UserName Field
	  waitForPageToLoad(driver, loginPageObjects.LOGIN_USERNAME_FIELD);
      loginPageObjects.LOGIN_USERNAME_FIELD.clear();
      loginPageObjects.LOGIN_USERNAME_FIELD.click();
      loginPageObjects.LOGIN_USERNAME_FIELD.sendKeys(email);
      Utils.log("Login Page > Username Textfield is Clicked and entered");
      hideKeyboardBasedOnPlatform();
  }
  
  public void enterPassWord(String Password) {
      // Clear and Enter Text in PassWord Field
	  waitForPageToLoad(driver, loginPageObjects.LOGIN_PASSWORD_FIELD);
      loginPageObjects.LOGIN_PASSWORD_FIELD.clear();
      loginPageObjects.LOGIN_PASSWORD_FIELD.click();
      loginPageObjects.LOGIN_PASSWORD_FIELD.sendKeys(Password);
      Utils.log("Login Page > Password Textfield is Clicked and entered");
      hideKeyboardBasedOnPlatform();  
  }
  
  public void invalidLoginVerification(String Email, String Pwd) throws InterruptedException {
	  waitForPageToLoad(driver, loginPageObjects.SCREEN_HEADER);
	  Utils.captureInterimScreenshot(driver);
	  enterUserName(Email);
	  enterPassWord(Pwd);  
	  Thread.sleep(1000);
      // Click on Login Button
      if (isElementPresent(loginPageObjects.SIGNIN_BUTTON)) {
        loginPageObjects.SIGNIN_BUTTON.click();
        Utils.log("Login Page > Log In Button is Clicked");
      }
	  if(loginPageObjects.FAILURE_ALERT_MESSAGE.isDisplayed()) {
		  ExtentTestManager.getTest().log(LogStatus.PASS, "Login failed with invalid credentials");
		  loginPageObjects.ALERT_BUTTON.click();
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
    Utils.log("POC app Login screen displayed...");
  }

}
