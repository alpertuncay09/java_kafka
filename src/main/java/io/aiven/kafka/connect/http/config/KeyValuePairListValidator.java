package io.aiven.kafka.connect.http.config;

import java.util.HashSet;
import java.util.List;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

















public class KeyValuePairListValidator
  implements ConfigDef.Validator
{
  private final String delimiter;
  
  public KeyValuePairListValidator(String delimiter) {
    this.delimiter = delimiter;
  }

  
  public void ensureValid(String name, Object value) {
    if (!(value instanceof List)) {
      throw new ConfigException(name, value, "must be a list with the specified format");
    }
    
    List<String> values = (List<String>)value;
    HashSet<String> keySet = new HashSet<>();
    for (String headerField : values) {
      String[] splitHeaderField = headerField.split(this.delimiter, -1);
      String lowerCaseKey = splitHeaderField[0].toLowerCase();
      if (splitHeaderField.length != 2) {
        throw new ConfigException("Header field should use format header:value");
      }
      if (keySet.contains(lowerCaseKey)) {
        throw new ConfigException("Duplicate keys are not allowed (case-insensitive)");
      }
      keySet.add(lowerCaseKey);
    } 
  }

  
  public String toString() {
    return "Key value pair string list with format header:value";
  }
}