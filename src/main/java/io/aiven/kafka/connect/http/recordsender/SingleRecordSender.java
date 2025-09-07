package io.aiven.kafka.connect.http.recordsender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.aiven.kafka.connect.http.sender.HttpSender;
import java.util.Collection;
import java.util.Map;
import org.apache.kafka.connect.sink.SinkRecord;



















final class SingleRecordSender
  extends RecordSender
{
  private final ObjectMapper objectMapper = new ObjectMapper();
  
  protected SingleRecordSender(HttpSender httpSender) {
    super(httpSender);
  }

  
  public void send(Collection<SinkRecord> records) {
    for (SinkRecord record : records) {
      send(record);
    }
  }

  
  public void send(SinkRecord record) {
    String body = this.recordValueConverter.convert(record);

    
    String modifiedBody = modifyBodyIfNecessary(body);

    
    this.httpSender.send(modifiedBody);
  }


  
  private String modifyBodyIfNecessary(String body) {
    try {
      Map<String, Object> jsonMap = (Map<String, Object>)this.objectMapper.readValue(body, Map.class);

      
      if (jsonMap.containsKey("productType")) {
        int productType = ((Integer)jsonMap.get("productType")).intValue();

        
        if ((productType == 24 || productType == 12 || productType == 212) && 
          jsonMap.containsKey("amount")) {
          double amount = ((Number)jsonMap.get("amount")).doubleValue();
          double newAmount = amount / 1024.0D;
          jsonMap.put("amount", Double.valueOf(newAmount));
          System.out.println("New amount for productType " + productType + " (divided by 1024): " + newAmount);
        } 
      } 





      
      return this.objectMapper.writeValueAsString(jsonMap);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      
      return body;
    } 
  }
}


