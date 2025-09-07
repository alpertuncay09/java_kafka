/*    */ package io.aiven.kafka.connect.http.sender;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*    */ import java.net.http.HttpClient;
/*    */ import java.net.http.HttpRequest;
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
/*    */ 
/*    */ class StaticAuthHttpSender
/*    */   extends AbstractHttpSender
/*    */   implements HttpSender
/*    */ {
/*    */   StaticAuthHttpSender(HttpSinkConfig config, HttpClient client) {
/* 28 */     super(config, new StaticAuthHttpRequestBuilder(), client);
/*    */   }
/*    */   
/*    */   private static class StaticAuthHttpRequestBuilder
/*    */     extends DefaultHttpSender.DefaultHttpRequestBuilder
/*    */   {
/*    */     public HttpRequest.Builder build(HttpSinkConfig config) {
/* 35 */       return super
/* 36 */         .build(config)
/* 37 */         .header("Authorization", config.headerAuthorization());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\StaticAuthHttpSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */