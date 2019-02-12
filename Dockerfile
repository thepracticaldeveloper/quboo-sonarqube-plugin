FROM sonarqube:7.1
ADD  ./target/quboo-sonar-plugin-1.0.0.jar /opt/sonarqube/extensions/plugins/quboo-sonar-plugin.jar
