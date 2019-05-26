#!/usr/bin/env bash
mvn package
cp -v ./target/*.jar /usr/local/Cellar/sonarqube/7.7/libexec/extensions/plugins
sonar restart
