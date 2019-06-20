package io.tpd.quboo.sonarplugin.hooks;

import io.tpd.quboo.sonarplugin.settings.QubooProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QubooSensorTest {

  private QubooSensor qubooSensor;

  @Mock
  private SensorDescriptor sensorDescriptor;
  @Mock
  private SensorContext sensorContext;
  @Mock
  private Configuration configuration;

  @Before
  public void setup() {
    qubooSensor = new QubooSensor();
  }

  @Test
  public void descriptorHasName() {
    qubooSensor.describe(sensorDescriptor);

    verify(sensorDescriptor).name("Quboo Sensor");
  }

  @Test
  public void captureProperties() {
    given(configuration.get(QubooProperties.ACCESS_KEY)).willReturn(Optional.of("access"));
    given(configuration.get(QubooProperties.SECRET_KEY)).willReturn(Optional.of("secret"));
    given(configuration.get(QubooProperties.TOKEN_KEY)).willReturn(Optional.of("token"));
    given(sensorContext.config()).willReturn(configuration);

    qubooSensor.execute(sensorContext);

    verify(sensorContext).addContextProperty(QubooProperties.ACCESS_KEY, "access");
    verify(sensorContext).addContextProperty(QubooProperties.SECRET_KEY, "secret");
    verify(sensorContext).addContextProperty(QubooProperties.TOKEN_KEY, "token");
  }
}
