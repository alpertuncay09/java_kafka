package io.aiven.kafka.connect.http.config;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
















public enum AuthorizationType
{
  NONE("none"),
  OAUTH2("oauth2"),
  STATIC("static");
  public final String name;
  public static final Collection<String> NAMES;
  
  AuthorizationType(String name) {
    this.name = name;
  }
  
  public static AuthorizationType forName(String name) {
    Objects.requireNonNull(name);
    
    if (NONE.name.equalsIgnoreCase(name))
      return NONE; 
    if (OAUTH2.name.equalsIgnoreCase(name))
      return OAUTH2; 
    if (STATIC.name.equalsIgnoreCase(name)) {
      return STATIC;
    }
    throw new IllegalArgumentException("Unknown authorization type: " + name);
  }

  
  static {
    NAMES = (Collection<String>)Arrays.<AuthorizationType>stream(values()).map(v -> v.name).collect(Collectors.toList());
  }
}