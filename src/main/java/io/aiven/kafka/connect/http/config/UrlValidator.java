/*    */ package io.aiven.kafka.connect.http.config;
/*    */ 
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URL;
/*    */ import org.apache.kafka.common.config.ConfigDef;
/*    */ import org.apache.kafka.common.config.ConfigException;
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
/*    */ class UrlValidator
/*    */   implements ConfigDef.Validator
/*    */ {
/*    */   private final boolean skipNullString;
/*    */   
/*    */   UrlValidator() {
/* 30 */     this(false);
/*    */   }
/*    */   
/*    */   UrlValidator(boolean skipNullString) {
/* 34 */     this.skipNullString = skipNullString;
/*    */   }
/*    */ 
/*    */   
/*    */   public void ensureValid(String name, Object value) {
/* 39 */     if (this.skipNullString && value == null) {
/*    */       return;
/*    */     }
/* 42 */     if (value == null) {
/* 43 */       throw new ConfigException(name, null, "can't be null");
/*    */     }
/* 45 */     if (!(value instanceof String)) {
/* 46 */       throw new ConfigException(name, value, "must be string");
/*    */     }
/*    */     try {
/* 49 */       new URL((String)value);
/* 50 */     } catch (MalformedURLException e) {
/* 51 */       throw new ConfigException(name, value, "malformed URL");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "HTTP(S) URL";
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\config\UrlValidator.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */