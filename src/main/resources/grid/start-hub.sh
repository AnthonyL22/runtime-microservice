#!/usr/bin/env bash
echo Starting Selenium Hub...

java -jar selenium-server-standalone-3.0.1.jar -role hub -hubConfig "hubConfig.json"