[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.pacificwebconsulting.runtime/runtime-microservice/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/com.pacificwebconsulting.runtime/runtime-microservice)

Got a question?  [Email us](http://www.pacificwebconsulting.com/contact/) or reach out on [LinkedIn](https://www.linkedin.com/in/alombardo/) 

# Introduction

This microservice is to make available the following two components necessary for Selenium-based & TestNG automated testing projects to run in distributed test environment(s).  
Consumers of this project simply need to include this as a dependency and unzip the artifact in a desired location for general use.
  
  1. The runtime components for Selenium GRID
  2. The desired version(s) of supported Selenium Drivers
  
# Questions and issues

The [github issue tracker](https://github.com/AnthonyL22/runtime-microservice/issues) is **_only_** for bug reports and 
feature requests. Anything else, such as questions for help in using the library, should be [emailed to our team](http://www.pacificwebconsulting.com/contact/).  

# Technology

This project is designed to have the flexibility of going to the open-source communities on the web to dynamically fetch 
Selenium Drivers (for example: http://chromedriver.storage.googleapis.com/).  Rather than have hard-coded, potentially 
stale drivers embedded in your system, this microservice dynamically downloads, unzips, and integrates the supported 
Drivers used by Selenium in your project(s).  

## Driver APIs

[Chrome Drivers](http://chromedriver.storage.googleapis.com/) 

[IE, Safari, Other Drivers](http://selenium-release.storage.googleapis.com/) 

[PhantomJS Drivers](https://bitbucket.org/ariya/phantomjs/downloads/) 

[MS Edge Driver](http://www.microsoft.com/en-us/download/details.aspx?id=48740)
 
[Firefox 'Gecko' Driver](https://github.com/mozilla/geckodriver/releases/download/) 
    
To change the version of the drivers you'd like to use simply change the Driver Key properties in the POM.xml file and 
rebuild.
 
```
    <!--Driver KEYS-->
    <selenium.server.standalone.driver.key>3.4/selenium-server-standalone-3.4.0.jar</selenium.server.standalone.driver.key>
    <chrome.win.driver.key>2.28/chromedriver_win32.zip</chrome.win.driver.key>
    <chrome.linux32.driver.key>2.28/chromedriver_linux32.zip</chrome.linux32.driver.key>
    <chrome.linux64.driver.key>2.28/chromedriver_linux64.zip</chrome.linux64.driver.key>
    <chrome.mac32.driver.key>2.22/chromedriver_mac32.zip</chrome.mac32.driver.key>
    <ie.win32.driver.key>3.0/IEDriverServer_Win32_3.0.0.zip</ie.win32.driver.key>
    <ie.win64.driver.key>3.0/IEDriverServer_x64_3.0.0.zip</ie.win64.driver.key>
    <safari.driver.key>2.48/SafariDriver.safariextz</safari.driver.key>
    <gecko.win64.driver.key>v0.16.1/geckodriver-v0.16.1-win64.zip</gecko.win64.driver.key>
```

# Prerequisites

1. Maven 3.x

# Maven Dependency

The Maven logic below will unpack the driver and grid artifacts in your project's target directory for immediate use.  Simply,
 add the dependency and plugin below to your POM.xml.

```
<dependency>
    <groupId>com.pacificwebconsulting.runtime</groupId>
    <artifactId>runtime-microservice</artifactId>
    <version>1.0.0</version>
    <classifier>bin</classifier>
    <type>zip</type>
</dependency>
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>${maven-dependency-plugin.version}</version>
    <executions>
        <execution>
            <id>unpack</id>
            <phase>package</phase>
            <goals>
                <goal>unpack</goal>
            </goals>
            <configuration>
                <artifactItems>
                    <artifactItem>
                        <groupId>com.pwc.runtime</groupId>
                        <artifactId>runtime-microservice</artifactId>
                        <classifier>bin</classifier>
                        <type>zip</type>
                        <overWrite>true</overWrite>
                        <outputDirectory>${project.build.testOutputDirectory}</outputDirectory>
                    </artifactItem>
                </artifactItems>
                <outputDirectory>${project.build.testOutputDirectory}</outputDirectory>
                <overWriteReleases>false</overWriteReleases>
                <overWriteSnapshots>true</overWriteSnapshots>
                </configuration>
            </execution>
    </executions>
</plugin>
```

# Integration

Simply add dependency to your Maven-based automation module/project.  Then, when **mvn clean install** is performed all
the necessary drivers and grid components necessary will be downloaded to your **.../target/test-classes** directory
for use by your test.

# Selenium GRID

A fully functional Selenium Grid HOST and NODE are available when using this runtime framework.  Simply, go to your
**.../target/test-classes/grid** directory and perform the following in order according to your operating system:

  1. Start the HUB by clicking the start-hub script
  2. Start a NODE by clicking the start-node script (You may start more than one NODE if you'd like)
  3. Navigate to your local GRID **http://localhost:4444/grid/console** to verify the GRID is up and running
