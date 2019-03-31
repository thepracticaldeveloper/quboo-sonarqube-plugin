package io.tpd.quboo.sonarplugin.http;

import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClients {

  private HttpClients() {
  }

  /**
   * Even though Quboo uses a valid LetsEncrypt certificate, its CA might not be included by default in some
   * Java distributions. For that reason we use a client that trusts all certificates.
   *
   * @return an OkHttpClient that trusts all certificates
   */
  public static OkHttpClient getUnsafeOkHttpClient() {
    try {
      // No-op trust manager
      final TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
          }

          @Override
          public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
          }

          @Override
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
          }
        }
      };

      // Install the all-trusting trust manager
      final SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
      // Create an ssl socket factory with our all-trusting manager
      final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

      OkHttpClient.Builder builder = new OkHttpClient.Builder();
      builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
      builder.hostnameVerifier((hostname, session) -> true);

      return builder.build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
