/*    */ package io.aiven.kafka.connect.http.config;
/*    */ 
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
/*    */ public class NonBlankStringValidator
/*    */   implements ConfigDef.Validator
/*    */ {
/*    */   private final boolean skipNullString;
/*    */   
/*    */   public NonBlankStringValidator(boolean skipNullString) {
/* 26 */     this.skipNullString = skipNullString;
/*    */   }
/*    */ 
/*    */   
/*    */   public void ensureValid(String name, Object value) {
/* 31 */     if (this.skipNullString && value == null) {
/*    */       return;
/*    */     }
/*    */     
/* 35 */     if (value == null) {
/* 36 */       throw new ConfigException(name, null, "can't be null");
/*    */     }
/*    */     
/* 39 */     if (!(value instanceof String)) {
/* 40 */       throw new ConfigException(name, value, "must be string");
/*    */     }
/*    */     
/* 43 */     String stringValue = (String)value;
/* 44 */     if (stringValue.isBlank()) {
/* 45 */       throw new ConfigException(name, value, "String must be non-blank");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return "Non-blank string";
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\config\NonBlankStringValidator.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */