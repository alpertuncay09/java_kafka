/*     */ package io.aiven.kafka.connect.http;
/*     */ 
/*     */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*     */ import io.aiven.kafka.connect.http.recordsender.RecordSender;
/*     */ import io.aiven.kafka.connect.http.sender.HttpSender;
/*     */ import io.aiven.kafka.connect.http.sender.HttpSenderFactory;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import org.apache.kafka.connect.errors.ConnectException;
/*     */ import org.apache.kafka.connect.errors.DataException;
/*     */ import org.apache.kafka.connect.sink.ErrantRecordReporter;
/*     */ import org.apache.kafka.connect.sink.SinkRecord;
/*     */ import org.apache.kafka.connect.sink.SinkTask;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HttpSinkTask
/*     */   extends SinkTask
/*     */ {
/*  37 */   private static final Logger log = LoggerFactory.getLogger(HttpSinkTask.class);
/*     */ 
/*     */   
/*     */   private RecordSender recordSender;
/*     */ 
/*     */   
/*     */   private ErrantRecordReporter reporter;
/*     */ 
/*     */   
/*     */   private boolean useLegacySend;
/*     */ 
/*     */ 
/*     */   
/*     */   public void start(Map<String, String> props) {
/*  51 */     Objects.requireNonNull(props);
/*  52 */     HttpSinkConfig config = new HttpSinkConfig(props);
/*  53 */     HttpSender httpSender = HttpSenderFactory.createHttpSender(config);
/*  54 */     this.recordSender = RecordSender.createRecordSender(httpSender, config);
/*  55 */     this.useLegacySend = config.batchingEnabled();
/*     */     
/*  57 */     if (Objects.nonNull(config.kafkaRetryBackoffMs())) {
/*  58 */       this.context.timeout(config.kafkaRetryBackoffMs().longValue());
/*     */     }
/*     */     
/*     */     try {
/*  62 */       if (this.context.errantRecordReporter() == null) {
/*  63 */         log.info("Errant record reporter not configured.");
/*     */       }
/*     */ 
/*     */       
/*  67 */       this.reporter = this.context.errantRecordReporter();
/*  68 */     } catch (NoClassDefFoundError|NoSuchMethodError e) {
/*     */       
/*  70 */       log.warn("Apache Kafka versions prior to 2.6 do not support the errant record reporter.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(Collection<SinkRecord> records) {
/*  76 */     log.debug("Received {} records", Integer.valueOf(records.size()));
/*     */     
/*  78 */     if (!records.isEmpty())
/*     */     {
/*  80 */       if (!this.useLegacySend) {
/*  81 */         sendEach(records);
/*     */       } else {
/*  83 */         sendBatch(records);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendEach(Collection<SinkRecord> records) {
/*  94 */     for (SinkRecord record : records) {
/*  95 */       if (record.value() == null)
/*     */       {
/*  97 */         throw new DataException("Record value must not be null");
/*     */       }
/*     */       
/*     */       try {
/* 101 */         this.recordSender.send(record);
/* 102 */       } catch (ConnectException e) {
/* 103 */         if (this.reporter != null) {
/* 104 */           this.reporter.report(record, (Throwable)e);
/*     */           continue;
/*     */         } 
/* 107 */         throw new ConnectException(e.getMessage());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendBatch(Collection<SinkRecord> records) {
/* 118 */     for (SinkRecord record : records) {
/* 119 */       if (record.value() == null)
/*     */       {
/* 121 */         throw new DataException("Record value must not be null");
/*     */       }
/*     */     } 
/*     */     
/* 125 */     this.recordSender.send(records);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String version() {
/* 135 */     return Version.VERSION;
/*     */   }
/*     */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\HttpSinkTask.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */