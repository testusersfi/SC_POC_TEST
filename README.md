# POC Test Automation Suite
This test automation suite is built in _Java_ using __Appium Java Client__. The design is based on _Page Object Model_, with separate classes for Page Objects and Pages. It is possible to run the tests as a *Test NG* tests

The TestNG based tests are in the *"mwo.login.tests"* package. The *"testng.xml"* file at the root is used to configure the test classes / groups to be included in the test run.

Additional configuration options are possible from _'build.properties'_. A user can also have a _'local.properties'_ file that is not in version control, but over-rides the _'build.properties'_ at run-time. This is so that the _build.properties_ could be kept intact, while allowing each test developer to customize using _'local.properties'_. 

The __Appium__ server is automatically launched from the __java__ code (using the _@BeforeSuite_ annotation). This means that the tester doesn't have to launch Appium.app before running the tests. 

__Ant__ is primarily used for packaging, test execution and generation of TestNG XSLT reports. __Fastlane__ is used to boot the emulator, optionally fetch the latest binaries (apk/ipa) from hockey app and trigger the ant tasks. Separate lanes are available for iOS & Android.

As for Test Reporting, the amazing __'ExtentReports'__ framework is used. This provides the ability to capture screenshots and display those in the test reports. The _Cukes_ report is generated in the __'target'__ directory and the __TestNG__ reports are available at _'test-output'_ and _'test-xslt-output'_ directory. __Extent Reports__ logs are generated at _'test-extentreport'_ directory. _Date-wise_ TestNG logs are stored at _'testng-reports'_ and _'testng-xslt-reports'_ directories.


## Tools Used
* Appium
* Test NG
* Cucumber with JUnit Runner class
* Extent Reporting
* Ant
* TestNG XLST Reporting
* Cucumber-Advanced HTML reporting tools
* Checkstyle (http://eclipse-cs.sourceforge.net/#!/install)

## Open Source Integrations used
* http://mkolisnyk.github.io/cucumber-reports/
* https://github.com/mkolisnyk/cucumber-reports
* https://github.com/damianszczepanik/cucumber-reporting
* https://github.com/anshooarora/extentreports-java


## Referenced Code:
* https://github.com/saikrishna321/PageObjectPatternAppium
* https://github.com/saikrishna321/AppiumTestDistribution
* https://github.com/bratgatty/appium-commoncode-mobile

## Document References
* https://gopekannan.wordpress.com/2016/07/06/customization-of-html-report-for-cucumber-specific-test-results/
* http://mkolisnyk.blogspot.in/2015/05/cucumber-jvm-advanced-reporting.html
* https://cucumber.io/docs/reference/jvm#java
* http://www.tutorialspoint.com/cucumber/cucumber_tutorial.pdf
* https://www.gitbook.com/download/pdf/book/sukesh15/cucumber-jvm-test-framework-
* http://appium.io/slate/en/master/?java#
* http://extentreports.relevantcodes.com/java/



