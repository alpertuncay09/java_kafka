package io.aiven.kafka.connect.http.converter;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.sink.SinkRecord;

















class JsonRecordValueConverter
  implements RecordValueConverter.Converter
{
  private final JsonConverter jsonConverter;
  
  public JsonRecordValueConverter() {
    this.jsonConverter = new JsonConverter();
    this.jsonConverter.configure(Map.of("schemas.enable", Boolean.valueOf(false), "converter.type", "value"));
  }

  
  public String convert(SinkRecord record) {
    return new String(this.jsonConverter
        .fromConnectData(record.topic(), record.valueSchema(), record.value()), StandardCharsets.UTF_8);
  }
}