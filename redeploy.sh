#!/usr/bin/env bash
mvn package
cp -v ./target/*.jar /usr/local/Cellar/sonarqube/8.2.0.32929/libexec/extensions/plugins
sonar restart
