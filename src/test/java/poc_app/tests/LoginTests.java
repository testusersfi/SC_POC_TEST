package poc_app.tests;

import org.testng.annotations.Test;
import com.appium.testng.TestBase;
import poc_app.pages.LoginPage;
import poc_app.pages.HomePage;


public class LoginTests extends TestBase {

  LoginPage loginPage;
  HomePage homePage;

  public LoginTests() throws Exception {

  }

  @Test(groups = {"Final.Regression.Android", "Final.Regression.iOS"})
  public void testValidLogin() throws Exception {
    loginPage = new LoginPage(driver);
    loginPage.AppLaunchVerification();
    homePage = loginPage.validLoginVerification("admin", "welcome@123");
    homePage.homeScreenVerification();
  }
  
  @Test(groups = {"Final.Regression.Android", "Final.Regression.iOS"})
  public void testInValidLogin() throws Exception {
    loginPage = new LoginPage(driver);
    loginPage.AppLaunchVerification();
    loginPage.invalidLoginVerification("admin", "welcome123");
  }


}
