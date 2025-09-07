/*    */ package io.aiven.kafka.connect.http.sender;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*    */ import java.net.http.HttpClient;
/*    */ import java.net.http.HttpRequest;
/*    */ import java.time.Duration;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultHttpSender
/*    */   extends AbstractHttpSender
/*    */   implements HttpSender
/*    */ {
/*    */   DefaultHttpSender(HttpSinkConfig config, HttpClient client) {
/* 29 */     super(config, new DefaultHttpRequestBuilder(), client);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   static class DefaultHttpRequestBuilder
/*    */     implements HttpRequestBuilder
/*    */   {
/*    */     public HttpRequest.Builder build(HttpSinkConfig config) {
/* 38 */       HttpRequest.Builder httpRequest = HttpRequest.newBuilder(config.httpUri()).timeout(Duration.ofSeconds(config.httpTimeout()));
/*    */ 
/*    */       
/* 41 */       Objects.requireNonNull(httpRequest); config.getAdditionalHeaders().forEach(httpRequest::header);
/* 42 */       if (config.headerContentType() != null) {
/* 43 */         httpRequest.header("Content-Type", config.headerContentType());
/*    */       }
/* 45 */       return httpRequest;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\DefaultHttpSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */