package io.aiven.kafka.connect.http.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.kafka.common.config.ConfigDef;





















class FixedSetRecommender
  implements ConfigDef.Recommender
{
  private final List<Object> supportedValues;
  
  private FixedSetRecommender(Collection<?> supportedValues) {
    Objects.requireNonNull(supportedValues);
    this.supportedValues = new ArrayList(supportedValues);
  }

  
  public List<Object> validValues(String name, Map<String, Object> parsedConfig) {
    return Collections.unmodifiableList(this.supportedValues);
  }

  
  public boolean visible(String name, Map<String, Object> parsedConfig) {
    return true;
  }
  
  static FixedSetRecommender ofSupportedValues(Collection<?> supportedValues) {
    Objects.requireNonNull(supportedValues);
    return new FixedSetRecommender(supportedValues);
  }
}