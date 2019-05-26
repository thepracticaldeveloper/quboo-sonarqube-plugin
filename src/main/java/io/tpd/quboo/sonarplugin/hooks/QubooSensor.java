package io.tpd.quboo.sonarplugin.hooks;

import io.tpd.quboo.sonarplugin.settings.QubooProperties;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Optional;

public class QubooSensor implements Sensor {

  private final Logger log = Loggers.get(getClass());

  @Override
  public void describe(final SensorDescriptor descriptor) {
    descriptor.name("Quboo Sensor");
  }

  @Override
  public void execute(final SensorContext context) {
    final Optional<String> key = context.config().get(QubooProperties.ACCESS_KEY);
    final Optional<String> secret = context.config().get(QubooProperties.SECRET_KEY);
    final Optional<String> token = context.config().get(QubooProperties.TOKEN_KEY);
    key.ifPresent(accessKey -> context.addContextProperty(QubooProperties.ACCESS_KEY, accessKey));
    secret.ifPresent(s -> context.addContextProperty(QubooProperties.SECRET_KEY, s));
    token.ifPresent(s -> context.addContextProperty(QubooProperties.TOKEN_KEY, s));
    log.info("Access key is " + key.orElse("NOT PRESENT"));
  }
}
