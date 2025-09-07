package io.aiven.kafka.connect.http;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

















class Version
{
  private static final Logger log = LoggerFactory.getLogger(Version.class);
  
  private static final String PROPERTIES_FILENAME = "http-connector-for-apache-kafka-version.properties";
  
  static final String VERSION;
  
  static {
    Properties props = new Properties();
    
    try { InputStream resourceStream = Version.class.getClassLoader().getResourceAsStream("http-connector-for-apache-kafka-version.properties"); 
      try { props.load(resourceStream);
        if (resourceStream != null) resourceStream.close();  } catch (Throwable throwable) { if (resourceStream != null) try { resourceStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
    { log.warn("Error while loading {}: {}", "http-connector-for-apache-kafka-version.properties", e.getMessage()); }
    
    VERSION = props.getProperty("version", "unknown").trim();
  }
}