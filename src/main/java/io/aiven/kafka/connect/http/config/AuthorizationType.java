/*    */ package io.aiven.kafka.connect.http.config;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.Objects;
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
/*    */ public enum AuthorizationType
/*    */ {
/* 25 */   NONE("none"),
/* 26 */   OAUTH2("oauth2"),
/* 27 */   STATIC("static");
/*    */   public final String name;
/*    */   public static final Collection<String> NAMES;
/*    */   
/*    */   AuthorizationType(String name) {
/* 32 */     this.name = name;
/*    */   }
/*    */   
/*    */   public static AuthorizationType forName(String name) {
/* 36 */     Objects.requireNonNull(name);
/*    */     
/* 38 */     if (NONE.name.equalsIgnoreCase(name))
/* 39 */       return NONE; 
/* 40 */     if (OAUTH2.name.equalsIgnoreCase(name))
/* 41 */       return OAUTH2; 
/* 42 */     if (STATIC.name.equalsIgnoreCase(name)) {
/* 43 */       return STATIC;
/*    */     }
/* 45 */     throw new IllegalArgumentException("Unknown authorization type: " + name);
/*    */   }
/*    */ 
/*    */   
/*    */   static {
/* 50 */     NAMES = (Collection<String>)Arrays.<AuthorizationType>stream(values()).map(v -> v.name).collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\config\AuthorizationType.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */