package io.tpd.quboo.sonarplugin;

import io.tpd.quboo.sonarplugin.hooks.QubooConnector;
import io.tpd.quboo.sonarplugin.settings.QubooProperties;
import org.sonar.api.Plugin;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class QubooPlugin implements Plugin {

  public static final String QUBOO_SERVER = "";
  public static final String QUBOO_HEADER_ACCESS_KEY = "x-quboo-access-key";
  public static final String QUBOO_HEADER_SECRET_KEY = "x-quboo-secret-key";

  @Override
  public void define(Context context) {
    context.addExtension(QubooConnector.class);
    context.addExtensions(QubooProperties.getProperties());
  }
}
