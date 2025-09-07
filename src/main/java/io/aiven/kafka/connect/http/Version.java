/*    */ package io.aiven.kafka.connect.http;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
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
/*    */ class Version
/*    */ {
/* 26 */   private static final Logger log = LoggerFactory.getLogger(Version.class);
/*    */   
/*    */   private static final String PROPERTIES_FILENAME = "http-connector-for-apache-kafka-version.properties";
/*    */   
/*    */   static final String VERSION;
/*    */   
/*    */   static {
/* 33 */     Properties props = new Properties();
/*    */     
/* 35 */     try { InputStream resourceStream = Version.class.getClassLoader().getResourceAsStream("http-connector-for-apache-kafka-version.properties"); 
/* 36 */       try { props.load(resourceStream);
/* 37 */         if (resourceStream != null) resourceStream.close();  } catch (Throwable throwable) { if (resourceStream != null) try { resourceStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 38 */     { log.warn("Error while loading {}: {}", "http-connector-for-apache-kafka-version.properties", e.getMessage()); }
/*    */     
/* 40 */     VERSION = props.getProperty("version", "unknown").trim();
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\Version.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */