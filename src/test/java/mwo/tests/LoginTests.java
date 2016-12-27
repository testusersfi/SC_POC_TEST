package mwo.tests;

import org.testng.annotations.Test;
import com.appium.testng.TestBase;
import mwo.pages.LoginPage;


public class LoginTests extends TestBase {

  LoginPage loginPage;

  public LoginTests() throws Exception {

  }

  @Test(groups = {"Final.Regression.Android", "Final.Regression.iOS"})
  public void testValidLogin() throws Exception {
    loginPage = new LoginPage(driver);
    loginPage.AppLaunchVerification();
    loginPage.loginFUnctionality("ganesh", "ganesh");
    loginPage.validLoginVerification();
  }
  
  @Test(groups = {"Final.Regression.Android", "Final.Regression.iOS"})
  public void testInValidLogin() throws Exception {
    loginPage = new LoginPage(driver);
    loginPage.AppLaunchVerification();
    loginPage.loginFUnctionality("Ganesh", "Ganesh123");
    loginPage.invalidLoginVerification();
  }


}
