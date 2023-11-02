[![Maven Central](https://img.shields.io/maven-central/v/com.pacificwebconsulting.runtime/runtime-microservice.svg)](https://img.shields.io/maven-central/v/com.pacificwebconsulting.runtime/runtime-microservice.svg)

[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)

## Browsers support

| [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/edge/edge_48x48.png" alt="IE / Edge" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)<br/>IE / Edge | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/firefox/firefox_48x48.png" alt="Firefox" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)<br/>Firefox | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/chrome/chrome_48x48.png" alt="Chrome" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)<br/>Chrome | [<img src="https://raw.githubusercontent.com/alrra/browser-logos/master/src/safari/safari_48x48.png" alt="Safari" width="24px" height="24px" />](http://godban.github.io/browsers-support-badges/)<br/>Safari |
|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 116 & below                                                                                                                                                                                                     | 32.2 & below                                                                                                                                                                                                      | 119 & below                                                                                                                                                                                                   | latest version                                                                                                                                                                                                |


Got a question?  [Email us](http://www.pacificwebconsulting.com/contact/) or reach out on [LinkedIn](https://www.linkedin.com/in/alombardo/) 

# Introduction

This microservice is to make available the following two components necessary for Selenium-based & TestNG automated testing projects to run in distributed test environment(s).  

Consumers of this project simply need to include this as a Maven dependency and all components will be automatically unzipped to the proper location for general use at test runtime.
  
  1. The runtime components for Selenium GRID
  2. The desired version(s) of supported Selenium Drivers
  
# Questions and issues

The [github issue tracker](https://github.com/AnthonyL22/runtime-microservice/issues) is **_only_** for bug reports and 
feature requests. Anything else, such as questions for help in using the library, should be [emailed to our team](http://www.pacificwebconsulting.com/contact/).  Thanks!

# Technology

This project is designed to have the flexibility of going to the open-source communities on the web to dynamically fetch 
Selenium Drivers (for example: https://googlechromelabs.github.io/chrome-for-testing/).  Rather than have hard-coded, potentially 
stale drivers embedded in your system, this microservice dynamically downloads, unzips, and integrates the supported 
Drivers used by Selenium in your project(s).  

## Driver APIs

[Chrome Drivers](https://googlechromelabs.github.io/chrome-for-testing/) 

[IE, Safari, Other Drivers](http://selenium-release.storage.googleapis.com/) 

[MS Edge Driver](https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/)
 
[Firefox 'Gecko' Driver](https://github.com/mozilla/geckodriver/releases/) 
    
To change the version of the drivers you'd like to use simply change the Driver Key properties in the POM.xml file and 
rebuild.
 
```
    <!--Driver KEYS-->
     <driver.key.chrome.linux64>116.0.5845.96/linux64/chromedriver-linux64.zip</driver.key.chrome.linux64>
    <driver.key.chrome.mac64>116.0.5845.96/mac-x64/chromedriver-mac-x64.zip</driver.key.chrome.mac64>
    <driver.key.chrome.win64>116.0.5845.96/win64/chromedriver-win64.zip</driver.key.chrome.win64>
    <driver.key.edge.mac64>114.0.1823.82/edgedriver_mac64.zip</driver.key.edge.mac64>
    <driver.key.edge.win64>114.0.1823.82/edgedriver_win64.zip</driver.key.edge.win64>
    <driver.key.gecko.linux64>v0.32.2/geckodriver-v0.32.2-linux64.tar.gz</driver.key.gecko.linux64>
    <driver.key.gecko.mac64>v0.32.2/geckodriver-v0.32.2-macos.tar.gz</driver.key.gecko.mac64>
    <driver.key.gecko.win64>v0.32.2/geckodriver-v0.32.2-win64.zip</driver.key.gecko.win64>
    <driver.key.ie.win32>3.150/IEDriverServer_Win32_3.150.0.zip</driver.key.ie.win32>
    <driver.key.ie.win64>3.150/IEDriverServer_x64_3.150.0.zip</driver.key.ie.win64>
    <driver.key.safari>2.53/SafariDriver.safariextz</driver.key.safari>
    <driver.key.selenium.server.standalone>4.0/selenium-server-standalone-4.0.0-alpha-2.zip</driver.key.selenium.server.standalone>
```

# Prerequisites

1. Java 11
2. Maven 3.x

# Maven Dependency

The Maven logic below will unpack the driver and grid artifacts in your project's target directory for immediate use.  Simply,
 add the dependency and plugin below to your POM.xml.

```
<dependency>
    <groupId>com.pacificwebconsulting.runtime</groupId>
    <artifactId>runtime-microservice</artifactId>
    <version>1.0.35</version>
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
