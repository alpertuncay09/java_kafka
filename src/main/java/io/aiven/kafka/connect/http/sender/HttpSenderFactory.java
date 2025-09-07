/*     */ package io.aiven.kafka.connect.http.sender;
/*     */ 
/*     */ import io.aiven.kafka.connect.http.config.AuthorizationType;
/*     */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*     */ import java.net.ProxySelector;
/*     */ import java.net.Socket;
/*     */ import java.net.http.HttpClient;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509ExtendedTrustManager;
/*     */ import org.apache.kafka.connect.errors.ConnectException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpSenderFactory
/*     */ {
/*     */   public static HttpSender createHttpSender(HttpSinkConfig config) {
/*     */     OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender;
/*  40 */     HttpClient client = buildHttpClient(config);
/*  41 */     switch (config.authorizationType()) {
/*     */       case NONE:
/*  43 */         return new DefaultHttpSender(config, client);
/*     */       case STATIC:
/*  45 */         return new StaticAuthHttpSender(config, client);
/*     */       case OAUTH2:
/*  47 */         oauth2AccessTokenHttpSender = new OAuth2AccessTokenHttpSender(config, client);
/*     */         
/*  49 */         return new OAuth2HttpSender(config, client, oauth2AccessTokenHttpSender);
/*     */     } 
/*  51 */     throw new ConnectException("Can't create HTTP sender for auth type: " + config.authorizationType());
/*     */   }
/*     */ 
/*     */   
/*  55 */   private static final TrustManager DUMMY_TRUST_MANAGER = new X509ExtendedTrustManager()
/*     */     {
/*     */       public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public X509Certificate[] getAcceptedIssuers() {
/*  82 */         return new X509Certificate[0];
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
/*     */ 
/*     */ 
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   static HttpClient buildHttpClient(HttpSinkConfig config) {
/*  98 */     HttpClient.Builder clientBuilder = HttpClient.newBuilder();
/*  99 */     if (config.hasProxy()) {
/* 100 */       clientBuilder.proxy(ProxySelector.of(config.proxy()));
/*     */     }
/* 102 */     if (config.sslTrustAllCertificates()) {
/*     */       try {
/* 104 */         SSLContext sslContext = SSLContext.getInstance("TLS");
/* 105 */         sslContext.init(null, new TrustManager[] { DUMMY_TRUST_MANAGER }, new SecureRandom());
/* 106 */         clientBuilder.sslContext(sslContext);
/* 107 */       } catch (NoSuchAlgorithmException|java.security.KeyManagementException e) {
/* 108 */         throw new RuntimeException(e);
/*     */       } 
/*     */     }
/* 111 */     return clientBuilder.build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\HttpSenderFactory.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */