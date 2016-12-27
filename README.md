# CA Live Test Automation Suite
This test automation suite is built in _Java_ using __Appium Java Client__. The design is based on _Page Object Model_, with separate classes for Page Objects and Pages. It is possible to run the tests as a *Test NG* tests or as a *Cucumber* Test. 

The TestNG based tests are in the *"au.com.cricket.tests"* package. The *"testng.xml"* file at the root is used to configure the test classes / groups to be included in the test run.

The *Cucumber BDD style .feature files* are in the **"features"** directory and the step definitions (glue code) are found in the **"au.com.cricket.steps"** package. The runner class **"RunCukesTest"** is _JUnit_ based and the Cucumber test is triggered from this class.

Additional configuration options are possible from _'build.properties'_. A user can also have a _'local.properties'_ file that is not in version control, but over-rides the _'build.properties'_ at run-time. This is so that the _build.properties_ could be kept intact, while allowing each test developer to customize using _'local.properties'_. 

The __Appium__ server is automatically launched from the __java__ code (using the _@BeforeSuite_ annotation). This means that the tester doesn't have to launch Appium.app before running the tests. 

__Ant__ is primarily used for packaging, test execution and generation of TestNG XSLT reports. __Fastlane__ is used to boot the emulator, optionally fetch the latest binaries (apk/ipa) from hockey app and trigger the ant tasks. Separate lanes are available for iOS & Android.

As for Test Reporting, the amazing __'ExtentReports'__ framework is used. This provides the ability to capture screenshots and display those in the test reports. The _Cukes_ report is generated in the __'target'__ directory and the __TestNG__ reports are available at _'test-output'_ and _'test-xslt-output'_ directory. __Extent Reports__ logs are generated at _'test-extentreport'_ directory. _Date-wise_ TestNG logs are stored at _'testng-reports'_ and _'testng-xslt-reports'_ directories.

## Setting up the Environment
Read [Appium Set-up and Test Execution Guide](https://cricketaustralia.atlassian.net/wiki/display/TEST/Appium+Test+setup+and+Execution+Guide) to do the environment set-up.

## Notes for Test Developers
* The __test code__ resides in __src/test/java__ while the __main framework__ code resides in __src/main/java__. 
* Follow the __package naming__ conventions - __au.com.APPNAME.pageobjects, pages, tests and steps__. 
* The __base class__ of all the page classes should be __'PageBase'__, tests should be __'TestBase'__ and for steps, it should be __'StepBase'__. 
* The __ClassNames__ should begin with Capital and have camel case. The __methodNames__ should begin with small letter and should be in camel case.  
* The __APPNAME__ _property_ should have either __calive__ or __network__ or __mycricket__. The __app_APPNAME.properties__ and __testng_APPNAME.properties__ should be created for every app.
* The property values in __config.properties__ and overridden by __app_APPNAME.properties__, which are then overridden by local.properties. Finally command line args override everything. Note that ant or maven scripts write the command line args to local.properties. The file _local.properties_ is _ignored_ from being submitted to git.
* The project when imported to Eclipse sets the _code-formatting preferences_. __Do not overwrite the code formatter preferences from Eclipse__.
* For _Android_ builds, download the __.apk__ from either dev or prod space from Hockey. As for iOS real device, download the dev builds. Appium doesn't work with IPA signed using Enterprise Distribution certificate. As for iOS Simulator, get the developers to build one .app for the simulator or fetch the app source code and run 'fastlane ios localbuild'. This should build for simulator as well.  
* To run the tests on __Android Emulator__, ensure the the correct version of _chromedriver_ is used for the tests (using __CHROME_DRIVER_EXECUTABLE__ property) corresponding to the 'Android System Webview' version. The Android _real devices_ should have _latest_ version of __Chrome browser__ installed.
* As for __cucumber__, write the _feature_ files under __'src/test/features-APPNAME'__ directory. Place the _glue_ code / steps under _'au.com.APPNAME.steps'_.
* For executing the tests on _Multiple devices_, change the _base_ class of __'TestBase'__ to 'TestBaseDistributed'. For parallel execution of tests on multiple connected devices, set the property RUNNER to parallel. For distributed run, set RUNNER to distribute.
* For executing the tests on _single device_, change the _base_ class of __'TestBase'__ to 'TestBaseStandalone'. This is the current set-up, but investigations are on to set this up dynamically.   

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
* https://github.com/email2vimalraj/CucumberExtentReporter

## Referenced Code:
* https://github.com/saikrishna321/PageObjectPatternAppium
* https://github.com/saikrishna321/AppiumTestDistribution
* https://github.com/bratgatty/appium-commoncode-mobile
* https://github.com/testvagrant/AccountsDemoTests_CucumberJVMExample

## Document References
* https://gopekannan.wordpress.com/2016/07/06/customization-of-html-report-for-cucumber-specific-test-results/
* http://mkolisnyk.blogspot.in/2015/05/cucumber-jvm-advanced-reporting.html
* https://cucumber.io/docs/reference/jvm#java
* http://www.tutorialspoint.com/cucumber/cucumber_tutorial.pdf
* https://www.gitbook.com/download/pdf/book/sukesh15/cucumber-jvm-test-framework-
* http://appium.io/slate/en/master/?java#
* http://extentreports.relevantcodes.com/java/

## Open Items
* Phase out Ant build and move archive reports feature to maven
* Create different profiles in pom.xml to execute different tests
* Customise Extent Reports filename.
* Move surefire-reports folder under test-reports.


## Recently Implemented Features
* Maven Integration + maintaining Appium Compatibility
* Integration with AppiumTestDistribution
* Separate out framework code to 'main' and test code to 'test' directories.
* Flat folder structure, remove top level folder 'CALiveAppAutomation'
* Modified Eclipse project to use [eclipse-java-google-style.xml](https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml)
* Enforce checkstyle [google_checks.xml](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) checker xml & run the same from 'mvn clean install -Dmaven.test.skip=true'
* One suite for all Test Apps.
* Make maven build to write to local.properties file based on -D command-line args.
