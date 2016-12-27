package mwo.pageobjects;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class LoginPageObjects {

  @AndroidFindBy(id = "IFS_AutoSample.IFS_AutoSample:id/userName")
  @iOSFindBy(xpath = "")
  public MobileElement LOGIN_USERNAME_FIELD;

  @AndroidFindBy(id = "IFS_AutoSample.IFS_AutoSample:id/password")
  @iOSFindBy(xpath = "")
  public MobileElement LOGIN_PASSWORD_FIELD;

  @AndroidFindBy(id = "IFS_AutoSample.IFS_AutoSample:id/MyButton")
  @iOSFindBy(xpath = "//UIAButton[contains(@name,'Go to Extras')]")
  public MobileElement SIGNIN_BUTTON;

  @AndroidFindBy(
      xpath = "//android.widget.TextView[contains(@text,'IFS_AutoSample')]")
  public MobileElement SCREEN_HEADER;
  
  public String LOGIN_STATUS =
	      "//android.widget.TextView[contains(@resource-id,'IFS_AutoSample.IFS_AutoSample:id/lblResult') and @text=\"%s\"]";
}
