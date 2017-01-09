package poc_app.pageobjects;

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
  @iOSFindBy(xpath = "//UIAButton[contains(@name,'MyButton')]")
  public MobileElement SIGNIN_BUTTON;

  @AndroidFindBy(
      xpath = "//android.widget.TextView[contains(@text,'POC TestApp')]")
  public MobileElement SCREEN_HEADER;
  
  public String LOGIN_STATUS =
	      "//android.widget.TextView[contains(@resource-id,'IFS_AutoSample.IFS_AutoSample:id/lblResult') and @text=\"%s\"]";
  
  @AndroidFindBy(xpath = "//android.widget.TextView[contains(@resource-id, 'android:id/message') and @text='Login Failed']")
  public MobileElement FAILURE_ALERT_MESSAGE;
  
  @AndroidFindBy(xpath = "//android.widget.Button[contains(@resource-id, 'android:id/button1') and @text='OK']")
  public MobileElement ALERT_BUTTON;
}
