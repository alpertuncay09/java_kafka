package io.aiven.kafka.connect.http.config;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

















class UrlValidator
  implements ConfigDef.Validator
{
  private final boolean skipNullString;
  
  UrlValidator() {
    this(false);
  }
  
  UrlValidator(boolean skipNullString) {
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
    try {
      new URL((String)value);
    } catch (MalformedURLException e) {
      throw new ConfigException(name, value, "malformed URL");
    } 
  }

  
  public String toString() {
    return "HTTP(S) URL";
  }
}