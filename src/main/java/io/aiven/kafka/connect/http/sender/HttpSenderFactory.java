package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.AuthorizationType;
import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.http.HttpClient;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import org.apache.kafka.connect.errors.ConnectException;


















public final class HttpSenderFactory
{
  public static HttpSender createHttpSender(HttpSinkConfig config) {
    OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender;
    HttpClient client = buildHttpClient(config);
    switch (config.authorizationType()) {
      case NONE:
        return new DefaultHttpSender(config, client);
      case STATIC:
        return new StaticAuthHttpSender(config, client);
      case OAUTH2:
        oauth2AccessTokenHttpSender = new OAuth2AccessTokenHttpSender(config, client);
        
        return new OAuth2HttpSender(config, client, oauth2AccessTokenHttpSender);
    } 
    throw new ConnectException("Can't create HTTP sender for auth type: " + config.authorizationType());
  }

  
  private static final TrustManager DUMMY_TRUST_MANAGER = new X509ExtendedTrustManager()
    {
      public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {}




      
      public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {}




      
      public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {}




      
      public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {}




      
      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }



      
      public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}


      
      public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
    };


  
  static HttpClient buildHttpClient(HttpSinkConfig config) {
    HttpClient.Builder clientBuilder = HttpClient.newBuilder();
    if (config.hasProxy()) {
      clientBuilder.proxy(ProxySelector.of(config.proxy()));
    }
    if (config.sslTrustAllCertificates()) {
      try {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { DUMMY_TRUST_MANAGER }, new SecureRandom());
        clientBuilder.sslContext(sslContext);
      } catch (NoSuchAlgorithmException|java.security.KeyManagementException e) {
        throw new RuntimeException(e);
      } 
    }
    return clientBuilder.build();
  }
}