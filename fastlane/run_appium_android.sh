#!/bin/bash
set -ex

#cd ../demoPOC
export CHROMEDRIVER_VERSION=2.20

echo "Directory Setup"
# --- SET UP ANT PATH -- #
# We don't need this if you used 'brew install ant'
#export ANT_HOME=./apache-ant-1.9.7
#export PATH=$PATH:$ANT_HOME/bin
echo "After Ant Home Setup"

#Start the emulator
echo "Booting the Emulator"
emulator -list-avds
emulator -avd Nexus5_API22 -wipe-data & EMULATOR_PID=$!

# Wait for Android to finish booting
WAIT_CMD="adb wait-for-device shell getprop init.svc.bootanim"
until $WAIT_CMD | grep -m 1 stopped; do
  echo "Waiting for Emulator to boot..."
  sleep 1
done
echo "Finished Booting the Emulator."


# Unlock the Lock Screen...
adb shell input keyevent 82

# Clear and capture logcat...
adb logcat -c
adb logcat > log/logcat.log & LOGCAT_PID=$!
echo "Started logcat to capture the logs."

#EXECUTING APPIUM BUILD
#-- CHECK ANT VERSION --#
echo "Starting the tests now..."
ant -version
ant -f build.xml -Dplatform=android -DAPPNAME=testapp runtestngtests
echo "Test execution completed."

# Stop the background processes
kill $LOGCAT_PID
kill $EMULATOR_PID
echo "Emulator completed."
exit 0
