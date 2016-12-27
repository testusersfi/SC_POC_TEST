package com.appium.base;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSFindBy;

public class BasePageObjects {

  @iOSFindBy(xpath = "//UIANavigationBar/UIAButton[@name='Back']")
  public MobileElement IOS_BACK;

  @iOSFindBy(xpath = "//UIANavigationBar/UIAButton[@name='Done']")
  public MobileElement IOS_DONE;

}
