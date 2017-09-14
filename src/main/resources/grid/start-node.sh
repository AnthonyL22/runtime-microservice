#!/usr/bin/env bash
echo Starting Selenium Node on Linux ENV...

java -jar -Dwebdriver.chrome.driver="../drivers/chrome/chrome_linux_64" -Dwebdriver.gecko.driver="../drivers/firefox/geckodriver" -Dphantomjs.binary.path="../drivers/phantomjs/phantomjs" selenium-server-standalone.jar -role node -nodeConfig "defaultNodeConfig.json"
