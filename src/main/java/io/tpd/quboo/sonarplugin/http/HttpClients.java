package io.tpd.quboo.sonarplugin.http;

import okhttp3.OkHttpClient;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.stream.Stream;

public class HttpClients {

  private final Logger log = Loggers.get(getClass());

  private X509TrustManager x509TrustManager;

  public HttpClients() {
    loadQubooTrustStore();
  }

  private void loadQubooTrustStore() {
    try {
      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
      trustStore.load(getClass().getResourceAsStream("/cacerts_quboo"), "changeit".toCharArray());

      final TrustManagerFactory trustManagerFactory = TrustManagerFactory
        .getInstance(TrustManagerFactory.getDefaultAlgorithm());
      trustManagerFactory.init(trustStore);

      x509TrustManager = Stream.of(trustManagerFactory.getTrustManagers())
        .filter(tm -> tm instanceof X509TrustManager).findFirst()
        .map(tm -> (X509TrustManager) tm)
        .orElseThrow(() -> new IllegalStateException("There is no X509TrustManager in factory"));
      log.info("Quboo's Truststore loaded successfully");
      log.info("Quboo's Truststore contains {} accepted issuers", x509TrustManager.getAcceptedIssuers().length);
    } catch (final Exception e) {
      log.error("ERROR! Could not load Quboo truststore", e);
      throw new IllegalStateException("Could not load Quboo truststore", e);
    }
  }

  /**
   * Even though Quboo uses a valid LetsEncrypt certificate, its CA might not be included by default in some
   * Java distributions. For that reason we use a client that trusts also this particular CA.
   *
   * @return an OkHttpClient that trusts all default certificates (in JDK 11) plus the LetsEncrypt CA
   */
  public OkHttpClient getQubooTrustedOkHttpClient() {
    try {
      // No-op trust manager
      final TrustManager[] trustManagers = new TrustManager[]{
        new QubooTrustManager(this.x509TrustManager)
      };

      // Install the trust-manager
      final SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, trustManagers, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManagers[0]);

      return builder.build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static class QubooTrustManager implements X509TrustManager {

    private final X509TrustManager tm;

    private QubooTrustManager(final X509TrustManager tm) {
      this.tm = tm;
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
      tm.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
      tm.checkServerTrusted(chain, authType);
    }

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return tm.getAcceptedIssuers();
    }
  }
}
