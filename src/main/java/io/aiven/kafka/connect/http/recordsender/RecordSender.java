/*    */ package io.aiven.kafka.connect.http.recordsender;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*    */ import io.aiven.kafka.connect.http.converter.RecordValueConverter;
/*    */ import io.aiven.kafka.connect.http.sender.HttpSender;
/*    */ import java.util.Collection;
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
/*    */ public abstract class RecordSender
/*    */ {
/*    */   protected final HttpSender httpSender;
/* 31 */   protected final RecordValueConverter recordValueConverter = new RecordValueConverter();
/*    */   
/*    */   protected RecordSender(HttpSender httpSender) {
/* 34 */     this.httpSender = httpSender;
/*    */   }
/*    */   
/*    */   public abstract void send(Collection<SinkRecord> paramCollection);
/*    */   
/*    */   public abstract void send(SinkRecord paramSinkRecord);
/*    */   
/*    */   public static RecordSender createRecordSender(HttpSender httpSender, HttpSinkConfig config) {
/* 42 */     if (config.batchingEnabled()) {
/* 43 */       return new BatchRecordSender(httpSender, config
/*    */           
/* 45 */           .batchMaxSize(), config
/* 46 */           .batchPrefix(), config
/* 47 */           .batchSuffix(), config
/* 48 */           .batchSeparator());
/*    */     }
/* 50 */     return new SingleRecordSender(httpSender);
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\recordsender\RecordSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */