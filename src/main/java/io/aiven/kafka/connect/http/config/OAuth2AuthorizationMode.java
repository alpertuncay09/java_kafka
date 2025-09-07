/*    */ package io.aiven.kafka.connect.http.config;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
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
/*    */ public enum OAuth2AuthorizationMode
/*    */ {
/* 25 */   HEADER,
/* 26 */   URL;
/*    */   
/*    */   static final List<String> OAUTH2_AUTHORIZATION_MODES;
/*    */   
/*    */   static {
/* 31 */     OAUTH2_AUTHORIZATION_MODES = (List<String>)Arrays.<OAuth2AuthorizationMode>stream(values()).map(Enum::name).collect(Collectors.toUnmodifiableList());
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\config\OAuth2AuthorizationMode.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */