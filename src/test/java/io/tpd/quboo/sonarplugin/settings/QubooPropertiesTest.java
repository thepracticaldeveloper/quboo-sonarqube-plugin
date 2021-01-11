package io.tpd.quboo.sonarplugin.settings;

import org.junit.Test;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class QubooPropertiesTest {

  @Test
  public void allPropertiesConfigured() {
    final List<PropertyDefinition> properties = QubooProperties.getProperties();

    assertThat(properties.size()).isEqualTo(5);
    assertThat(properties.stream().map(PropertyDefinition::key).collect(Collectors.toList()))
      .containsExactly(QubooProperties.ACCESS_KEY, QubooProperties.SECRET_KEY, QubooProperties.TOKEN_KEY,
        QubooProperties.SELECTED_PROJECTS_KEY, QubooProperties.REJECTED_PROJECTS_KEY);
    assertThat(properties.get(2).type()).isEqualTo(PropertyType.PASSWORD);
  }
}
