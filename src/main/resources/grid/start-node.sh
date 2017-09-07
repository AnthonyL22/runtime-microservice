#!/usr/bin/env bash
echo Starting Selenium Node on Linux ENV...

java -jar -Dwebdriver.chrome.driver="../drivers/chrome/chrome_linux_64" -Dwebdriver.gecko.driver="../drivers/firefox/geckodriver" selenium-server-standalone-3.5.3.jar -role node -nodeConfig "defaultNodeConfig.json"
