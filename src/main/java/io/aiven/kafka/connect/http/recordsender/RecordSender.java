package io.aiven.kafka.connect.http.recordsender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import io.aiven.kafka.connect.http.converter.RecordValueConverter;
import io.aiven.kafka.connect.http.sender.HttpSender;
import java.util.Collection;
import org.apache.kafka.connect.sink.SinkRecord;




















public abstract class RecordSender
{
  protected final HttpSender httpSender;
  protected final RecordValueConverter recordValueConverter = new RecordValueConverter();
  
  protected RecordSender(HttpSender httpSender) {
    this.httpSender = httpSender;
  }
  
  public abstract void send(Collection<SinkRecord> paramCollection);
  
  public abstract void send(SinkRecord paramSinkRecord);
  
  public static RecordSender createRecordSender(HttpSender httpSender, HttpSinkConfig config) {
    if (config.batchingEnabled()) {
      return new BatchRecordSender(httpSender, config
          
          .batchMaxSize(), config
          .batchPrefix(), config
          .batchSuffix(), config
          .batchSeparator());
    }
    return new SingleRecordSender(httpSender);
  }
}