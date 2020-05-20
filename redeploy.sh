#!/usr/bin/env bash
mvn package
rm -rf /usr/local/Cellar/sonarqube/8.2.0.32929/libexec/extensions/plugins/quboo*.*
cp -v ./target/*-SNAPSHOT.jar /usr/local/Cellar/sonarqube/8.2.0.32929/libexec/extensions/plugins
sonar restart
