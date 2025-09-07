/*    */ package io.aiven.kafka.connect.http.config;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
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
/*    */ public class KeyValuePairListValidator
/*    */   implements ConfigDef.Validator
/*    */ {
/*    */   private final String delimiter;
/*    */   
/*    */   public KeyValuePairListValidator(String delimiter) {
/* 30 */     this.delimiter = delimiter;
/*    */   }
/*    */ 
/*    */   
/*    */   public void ensureValid(String name, Object value) {
/* 35 */     if (!(value instanceof List)) {
/* 36 */       throw new ConfigException(name, value, "must be a list with the specified format");
/*    */     }
/*    */     
/* 39 */     List<String> values = (List<String>)value;
/* 40 */     HashSet<String> keySet = new HashSet<>();
/* 41 */     for (String headerField : values) {
/* 42 */       String[] splitHeaderField = headerField.split(this.delimiter, -1);
/* 43 */       String lowerCaseKey = splitHeaderField[0].toLowerCase();
/* 44 */       if (splitHeaderField.length != 2) {
/* 45 */         throw new ConfigException("Header field should use format header:value");
/*    */       }
/* 47 */       if (keySet.contains(lowerCaseKey)) {
/* 48 */         throw new ConfigException("Duplicate keys are not allowed (case-insensitive)");
/*    */       }
/* 50 */       keySet.add(lowerCaseKey);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "Key value pair string list with format header:value";
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\config\KeyValuePairListValidator.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */