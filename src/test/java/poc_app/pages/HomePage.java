package poc_app.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;
import com.appium.base.PageBase;
import com.appium.base.Utils;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import com.appium.reports.ExtentTestManager;

import poc_app.pageobjects.HomePageObjects;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class HomePage extends PageBase {
 HomePageObjects homePageObjects = new HomePageObjects();

  public HomePage(AppiumDriver<MobileElement> driver) {
    super(driver);
    PageFactory.initElements(new AppiumFieldDecorator(driver, 5, TimeUnit.SECONDS),
    		homePageObjects);
  }

  public void homeScreenVerification() {
	  waitForPageToLoad(driver, homePageObjects.SCREEN_HEADER);
	  Utils.captureInterimScreenshot(driver);
	  screenTitleVerification();
	  welcomeTextVerification();
  }
  
  public void screenTitleVerification() {
	  if(homePageObjects.SCREEN_HEADER.isDisplayed()) {
		  ExtentTestManager.getTest().log(LogStatus.PASS, "Login is successful and Home screen is displayed");
	  } else {
		  ExtentTestManager.getTest().log(LogStatus.FAIL, "Login is failed");
	  }
	  
  }
  
  public void welcomeTextVerification() {
	  if(homePageObjects.WELCOME_TEXT.isDisplayed()) {
		  ExtentTestManager.getTest().log(LogStatus.PASS, "welcome to home screen text is displayed");
	  } else {
		  ExtentTestManager.getTest().log(LogStatus.FAIL, "invalid text");
	  }
  }
  

}
