#!/bin/bash
set -ex

cd ../CALiveAppAutomation/

#cp -f ../cricket-ios/artifacts/Cricket.app ./binaries/

# Start iOS Simulator
# Oh ! Hang on - Appium starts iOS simulator by itself. So, commenting the line below.
# (xcrun instruments -w "iPhone 6 (9.3)" || xcrun instruments -s devices)

#EXECUTING APPIUM BUILD
#-- CHECK ANT VERSION --#
echo "Starting the tests now..."
ant -version
ant -f build.xml -Dplatform=ios
echo "Test execution completed."
