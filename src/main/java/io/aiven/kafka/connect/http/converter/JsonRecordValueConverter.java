/*    */ package io.aiven.kafka.connect.http.converter;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.Map;
/*    */ import org.apache.kafka.connect.json.JsonConverter;
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
/*    */ class JsonRecordValueConverter
/*    */   implements RecordValueConverter.Converter
/*    */ {
/*    */   private final JsonConverter jsonConverter;
/*    */   
/*    */   public JsonRecordValueConverter() {
/* 30 */     this.jsonConverter = new JsonConverter();
/* 31 */     this.jsonConverter.configure(Map.of("schemas.enable", Boolean.valueOf(false), "converter.type", "value"));
/*    */   }
/*    */ 
/*    */   
/*    */   public String convert(SinkRecord record) {
/* 36 */     return new String(this.jsonConverter
/* 37 */         .fromConnectData(record.topic(), record.valueSchema(), record.value()), StandardCharsets.UTF_8);
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\converter\JsonRecordValueConverter.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */