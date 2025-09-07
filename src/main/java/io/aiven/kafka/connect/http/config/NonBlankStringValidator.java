package io.aiven.kafka.connect.http.config;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;















public class NonBlankStringValidator
  implements ConfigDef.Validator
{
  private final boolean skipNullString;
  
  public NonBlankStringValidator(boolean skipNullString) {
    this.skipNullString = skipNullString;
  }

  
  public void ensureValid(String name, Object value) {
    if (this.skipNullString && value == null) {
      return;
    }
    
    if (value == null) {
      throw new ConfigException(name, null, "can't be null");
    }
    
    if (!(value instanceof String)) {
      throw new ConfigException(name, value, "must be string");
    }
    
    String stringValue = (String)value;
    if (stringValue.isBlank()) {
      throw new ConfigException(name, value, "String must be non-blank");
    }
  }

  
  public String toString() {
    return "Non-blank string";
  }
}