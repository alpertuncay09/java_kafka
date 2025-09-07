/*    */ package io.aiven.kafka.connect.http;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import org.apache.kafka.common.config.ConfigDef;
/*    */ import org.apache.kafka.connect.connector.Task;
/*    */ import org.apache.kafka.connect.sink.SinkConnector;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ public final class HttpSinkConnector
/*    */   extends SinkConnector
/*    */ {
/* 34 */   private static final Logger log = LoggerFactory.getLogger(HttpSinkConnector.class);
/*    */ 
/*    */   
/*    */   private Map<String, String> configProps;
/*    */ 
/*    */   
/*    */   private HttpSinkConfig config;
/*    */ 
/*    */ 
/*    */   
/*    */   public ConfigDef config() {
/* 45 */     return HttpSinkConfig.configDef();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(Map<String, String> props) {
/* 50 */     Objects.requireNonNull(props);
/*    */     
/* 52 */     this.configProps = Collections.unmodifiableMap(props);
/* 53 */     this.config = new HttpSinkConfig(props);
/* 54 */     log.info("Starting connector {}", this.config.connectorName());
/*    */   }
/*    */ 
/*    */   
/*    */   public Class<? extends Task> taskClass() {
/* 59 */     return (Class)HttpSinkTask.class;
/*    */   }
/*    */ 
/*    */   
/*    */   public List<Map<String, String>> taskConfigs(int maxTasks) {
/* 64 */     return Collections.nCopies(maxTasks, Map.copyOf(this.configProps));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stop() {
/* 70 */     log.info("Stopping connector {}", this.config.connectorName());
/*    */   }
/*    */ 
/*    */   
/*    */   public String version() {
/* 75 */     return Version.VERSION;
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\HttpSinkConnector.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */