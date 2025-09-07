package io.aiven.kafka.connect.http.recordsender;

import io.aiven.kafka.connect.http.sender.HttpSender;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;





















final class BatchRecordSender
  extends RecordSender
{
  private final int batchMaxSize;
  private final String batchPrefix;
  private final String batchSuffix;
  private final String batchSeparator;
  
  protected BatchRecordSender(HttpSender httpSender, int batchMaxSize, String batchPrefix, String batchSuffix, String batchSeparator) {
    super(httpSender);
    this.batchMaxSize = batchMaxSize;
    this.batchPrefix = batchPrefix;
    this.batchSuffix = batchSuffix;
    this.batchSeparator = batchSeparator;
  }

  
  public void send(Collection<SinkRecord> records) {
    List<SinkRecord> batch = new ArrayList<>(this.batchMaxSize);
    for (SinkRecord record : records) {
      batch.add(record);
      if (batch.size() >= this.batchMaxSize) {
        String body = createRequestBody(batch);
        batch.clear();
        this.httpSender.send(body);
      } 
    } 
    
    if (!batch.isEmpty()) {
      String body = createRequestBody(batch);
      this.httpSender.send(body);
    } 
  }

  
  public void send(SinkRecord record) {
    throw new ConnectException("Don't call this method for batch sending");
  }
  
  private String createRequestBody(Collection<SinkRecord> batch) {
    StringBuilder result = new StringBuilder();
    if (!this.batchPrefix.isEmpty()) {
      result.append(this.batchPrefix);
    }
    Iterator<SinkRecord> it = batch.iterator();
    if (it.hasNext()) {
      result.append(this.recordValueConverter.convert(it.next()));
      while (it.hasNext()) {
        result.append(this.batchSeparator);
        result.append(this.recordValueConverter.convert(it.next()));
      } 
    } 
    if (!this.batchSuffix.isEmpty()) {
      result.append(this.batchSuffix);
    }
    return result.toString();
  }
}