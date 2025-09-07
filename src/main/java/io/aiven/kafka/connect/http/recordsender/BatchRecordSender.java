/*    */ package io.aiven.kafka.connect.http.recordsender;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.sender.HttpSender;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import org.apache.kafka.connect.errors.ConnectException;
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
/*    */ 
/*    */ 
/*    */ final class BatchRecordSender
/*    */   extends RecordSender
/*    */ {
/*    */   private final int batchMaxSize;
/*    */   private final String batchPrefix;
/*    */   private final String batchSuffix;
/*    */   private final String batchSeparator;
/*    */   
/*    */   protected BatchRecordSender(HttpSender httpSender, int batchMaxSize, String batchPrefix, String batchSuffix, String batchSeparator) {
/* 40 */     super(httpSender);
/* 41 */     this.batchMaxSize = batchMaxSize;
/* 42 */     this.batchPrefix = batchPrefix;
/* 43 */     this.batchSuffix = batchSuffix;
/* 44 */     this.batchSeparator = batchSeparator;
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(Collection<SinkRecord> records) {
/* 49 */     List<SinkRecord> batch = new ArrayList<>(this.batchMaxSize);
/* 50 */     for (SinkRecord record : records) {
/* 51 */       batch.add(record);
/* 52 */       if (batch.size() >= this.batchMaxSize) {
/* 53 */         String body = createRequestBody(batch);
/* 54 */         batch.clear();
/* 55 */         this.httpSender.send(body);
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     if (!batch.isEmpty()) {
/* 60 */       String body = createRequestBody(batch);
/* 61 */       this.httpSender.send(body);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(SinkRecord record) {
/* 67 */     throw new ConnectException("Don't call this method for batch sending");
/*    */   }
/*    */   
/*    */   private String createRequestBody(Collection<SinkRecord> batch) {
/* 71 */     StringBuilder result = new StringBuilder();
/* 72 */     if (!this.batchPrefix.isEmpty()) {
/* 73 */       result.append(this.batchPrefix);
/*    */     }
/* 75 */     Iterator<SinkRecord> it = batch.iterator();
/* 76 */     if (it.hasNext()) {
/* 77 */       result.append(this.recordValueConverter.convert(it.next()));
/* 78 */       while (it.hasNext()) {
/* 79 */         result.append(this.batchSeparator);
/* 80 */         result.append(this.recordValueConverter.convert(it.next()));
/*    */       } 
/*    */     } 
/* 83 */     if (!this.batchSuffix.isEmpty()) {
/* 84 */       result.append(this.batchSuffix);
/*    */     }
/* 86 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\recordsender\BatchRecordSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */