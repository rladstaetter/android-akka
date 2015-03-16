# Android Akka

This project serves as an example how to setup a project with maven to deploy a scala app 
which uses [Akka](http://akka.io/).

### Instructions

#### Android SDK dependencies
Install everything from the SDK manager. Yes, everything.

#### Deploy android sdk to maven locally
```
git clone https://github.com/srcreigh/maven-android-sdk-deployer
cd maven-android-sdk-deployer
mvn install
```

#### Install the zinc incremental compiler
```
brew install zinc
zinc -start
```

#### Build the app
```
cd android-akka
mvn install
```

### Deploy the app to your device
```
adb install target/android-akka.apk
```

Then, run the app on your device.
