FROM sonarqube:7.9-community
ADD  ./target/quboo-sonar-plugin*.jar /opt/sonarqube/extensions/plugins/quboo-sonar-plugin.jar
