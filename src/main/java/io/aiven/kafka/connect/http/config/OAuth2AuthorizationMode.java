package io.aiven.kafka.connect.http.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

















public enum OAuth2AuthorizationMode
{
  HEADER,
  URL;
  
  static final List<String> OAUTH2_AUTHORIZATION_MODES;
  
  static {
    OAUTH2_AUTHORIZATION_MODES = (List<String>)Arrays.<OAuth2AuthorizationMode>stream(values()).map(Enum::name).collect(Collectors.toUnmodifiableList());
  }
}