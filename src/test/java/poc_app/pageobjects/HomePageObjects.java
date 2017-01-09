package poc_app.pageobjects;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class HomePageObjects {

  @AndroidFindBy(
      xpath = "//android.widget.TextView[contains(@text,'Home')]")
  @iOSFindBy(xpath = "//UIASTATICTEXT[@text='Home'")
  public MobileElement SCREEN_HEADER;
  
  @AndroidFindBy(xpath = "//android.widget.TextView[contains(@resource-id, 'IFS_AutoSample.IFS_AutoSample:id/textView1') and @text='Welcome to Home screen']")
  @iOSFindBy(xpath = "//UIASTATICTEXT[@text='Welcome to Home Screen'")
  public MobileElement WELCOME_TEXT;
  

}
