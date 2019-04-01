/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package io.tpd.quboo.sonarplugin.settings;

import org.sonar.api.config.PropertyDefinition;

import java.util.List;

import static java.util.Arrays.asList;

public class QubooProperties {

  public static final String ACCESS_KEY = "sonar.quboo.access-key";
  public static final String SECRET_KEY = "sonar.quboo.secret-key";
  public static final String DEFAULT_ACCESS_KEY = "your-access-key";
  public static final String DEFAULT_SECRET_KEY = "your-secret-key";
  public static final String CATEGORY = "Quboo";

  private QubooProperties() {
    // only statics
  }

  public static List<PropertyDefinition> getProperties() {
    return asList(
      PropertyDefinition.builder(ACCESS_KEY)
        .name("Quboo Access Key")
        .description("Your organization account access key to export report summary to Quboo")
        .defaultValue(DEFAULT_ACCESS_KEY)
        .category(CATEGORY)
        .build(),
      PropertyDefinition.builder(SECRET_KEY)
        .name("Quboo Secret Key")
        .description("Your organization account secret key to export report summary to Quboo")
        .defaultValue(DEFAULT_SECRET_KEY)
        .category(CATEGORY)
        .build()
    );
  }

}
