#!/usr/bin/env bash
echo Starting Selenium Hub...

java -jar selenium-server-standalone.jar -role hub -hubConfig "hubConfig.json"
