/*    */ package io.aiven.kafka.connect.http.config;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import org.apache.kafka.common.config.ConfigDef;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class FixedSetRecommender
/*    */   implements ConfigDef.Recommender
/*    */ {
/*    */   private final List<Object> supportedValues;
/*    */   
/*    */   private FixedSetRecommender(Collection<?> supportedValues) {
/* 37 */     Objects.requireNonNull(supportedValues);
/* 38 */     this.supportedValues = new ArrayList(supportedValues);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Object> validValues(String name, Map<String, Object> parsedConfig) {
/* 43 */     return Collections.unmodifiableList(this.supportedValues);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean visible(String name, Map<String, Object> parsedConfig) {
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   static FixedSetRecommender ofSupportedValues(Collection<?> supportedValues) {
/* 52 */     Objects.requireNonNull(supportedValues);
/* 53 */     return new FixedSetRecommender(supportedValues);
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\config\FixedSetRecommender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */