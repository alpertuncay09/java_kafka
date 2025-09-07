package io.aiven.kafka.connect.http;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.sink.SinkConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


















public final class HttpSinkConnector
  extends SinkConnector
{
  private static final Logger log = LoggerFactory.getLogger(HttpSinkConnector.class);

  
  private Map<String, String> configProps;

  
  private HttpSinkConfig config;


  
  public ConfigDef config() {
    return HttpSinkConfig.configDef();
  }

  
  public void start(Map<String, String> props) {
    Objects.requireNonNull(props);
    
    this.configProps = Collections.unmodifiableMap(props);
    this.config = new HttpSinkConfig(props);
    log.info("Starting connector {}", this.config.connectorName());
  }

  
  public Class<? extends Task> taskClass() {
    return (Class)HttpSinkTask.class;
  }

  
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    return Collections.nCopies(maxTasks, Map.copyOf(this.configProps));
  }


  
  public void stop() {
    log.info("Stopping connector {}", this.config.connectorName());
  }

  
  public String version() {
    return Version.VERSION;
  }
}