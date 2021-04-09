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
    toContextPropertyIfPresent(context, QubooProperties.ACCESS_KEY);
    toContextPropertyIfPresent(context, QubooProperties.SECRET_KEY);
    toContextPropertyIfPresent(context, QubooProperties.TOKEN_KEY);
    toContextPropertyIfPresent(context, QubooProperties.SELECTED_PROJECTS_KEY);
    toContextPropertyIfPresent(context, QubooProperties.REJECTED_PROJECTS_KEY);
    toContextPropertyIfPresent(context, QubooProperties.SELECTED_USERS_KEY);
    Optional<String> accessKeyOptional = context.config().get(QubooProperties.ACCESS_KEY);
    if (accessKeyOptional.isPresent() && accessKeyOptional.get().equals(QubooProperties.DEFAULT_ACCESS_KEY)) {
      log.warn("WARNING: Quboo will ignore this analysis because you haven't set the Quboo Access (and Secret) Keys. Go to your Sonarqube server (as admin), Administration -> Configuration -> Quboo and enter the values you find in your Quboo account settings.");
    } else {
      log.info("Access key is " + accessKeyOptional.orElse("NOT PRESENT"));
    }
  }

  private static void toContextPropertyIfPresent(final SensorContext context,
                                                 final String propertyName) {
    context.config().get(propertyName).ifPresent(p ->
      context.addContextProperty(propertyName, p));
  }
}
