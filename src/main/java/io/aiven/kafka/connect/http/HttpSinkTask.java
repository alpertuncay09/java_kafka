package io.aiven.kafka.connect.http;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import io.aiven.kafka.connect.http.recordsender.RecordSender;
import io.aiven.kafka.connect.http.sender.HttpSender;
import io.aiven.kafka.connect.http.sender.HttpSenderFactory;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.sink.ErrantRecordReporter;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

















public final class HttpSinkTask
  extends SinkTask
{
  private static final Logger log = LoggerFactory.getLogger(HttpSinkTask.class);

  
  private RecordSender recordSender;

  
  private ErrantRecordReporter reporter;

  
  private boolean useLegacySend;


  
  public void start(Map<String, String> props) {
    Objects.requireNonNull(props);
    HttpSinkConfig config = new HttpSinkConfig(props);
    HttpSender httpSender = HttpSenderFactory.createHttpSender(config);
    this.recordSender = RecordSender.createRecordSender(httpSender, config);
    this.useLegacySend = config.batchingEnabled();
    
    if (Objects.nonNull(config.kafkaRetryBackoffMs())) {
      this.context.timeout(config.kafkaRetryBackoffMs().longValue());
    }
    
    try {
      if (this.context.errantRecordReporter() == null) {
        log.info("Errant record reporter not configured.");
      }

      
      this.reporter = this.context.errantRecordReporter();
    } catch (NoClassDefFoundError|NoSuchMethodError e) {
      
      log.warn("Apache Kafka versions prior to 2.6 do not support the errant record reporter.");
    } 
  }

  
  public void put(Collection<SinkRecord> records) {
    log.debug("Received {} records", Integer.valueOf(records.size()));
    
    if (!records.isEmpty())
    {
      if (!this.useLegacySend) {
        sendEach(records);
      } else {
        sendBatch(records);
      } 
    }
  }





  
  private void sendEach(Collection<SinkRecord> records) {
    for (SinkRecord record : records) {
      if (record.value() == null)
      {
        throw new DataException("Record value must not be null");
      }
      
      try {
        this.recordSender.send(record);
      } catch (ConnectException e) {
        if (this.reporter != null) {
          this.reporter.report(record, (Throwable)e);
          continue;
        } 
        throw new ConnectException(e.getMessage());
      } 
    } 
  }





  
  private void sendBatch(Collection<SinkRecord> records) {
    for (SinkRecord record : records) {
      if (record.value() == null)
      {
        throw new DataException("Record value must not be null");
      }
    } 
    
    this.recordSender.send(records);
  }


  
  public void stop() {}


  
  public String version() {
    return Version.VERSION;
  }
}