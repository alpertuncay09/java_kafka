/*    */ package io.aiven.kafka.connect.http.recordsender;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonProcessingException;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import io.aiven.kafka.connect.http.sender.HttpSender;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import org.apache.kafka.connect.sink.SinkRecord;
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
/*    */ final class SingleRecordSender
/*    */   extends RecordSender
/*    */ {
/* 31 */   private final ObjectMapper objectMapper = new ObjectMapper();
/*    */   
/*    */   protected SingleRecordSender(HttpSender httpSender) {
/* 34 */     super(httpSender);
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(Collection<SinkRecord> records) {
/* 39 */     for (SinkRecord record : records) {
/* 40 */       send(record);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(SinkRecord record) {
/* 46 */     String body = this.recordValueConverter.convert(record);
/*    */ 
/*    */     
/* 49 */     String modifiedBody = modifyBodyIfNecessary(body);
/*    */ 
/*    */     
/* 52 */     this.httpSender.send(modifiedBody);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private String modifyBodyIfNecessary(String body) {
/*    */     try {
/* 59 */       Map<String, Object> jsonMap = (Map<String, Object>)this.objectMapper.readValue(body, Map.class);
/*    */ 
/*    */       
/* 62 */       if (jsonMap.containsKey("productType")) {
/* 63 */         int productType = ((Integer)jsonMap.get("productType")).intValue();
/*    */ 
/*    */         
/* 66 */         if ((productType == 24 || productType == 12) && 
/* 67 */           jsonMap.containsKey("amount")) {
/* 68 */           double amount = ((Number)jsonMap.get("amount")).doubleValue();
/* 69 */           double newAmount = amount / 1024.0D;
/* 70 */           jsonMap.put("amount", Double.valueOf(newAmount));
/* 71 */           System.out.println("New amount for productType " + productType + " (divided by 1024): " + newAmount);
/*    */         } 
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 80 */       return this.objectMapper.writeValueAsString(jsonMap);
/* 81 */     } catch (JsonProcessingException e) {
/* 82 */       e.printStackTrace();
/*    */       
/* 84 */       return body;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\recordsender\SingleRecordSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */