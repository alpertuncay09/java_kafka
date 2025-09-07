package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.net.http.HttpRequest;

interface HttpRequestBuilder {
  public static final String HEADER_AUTHORIZATION = "Authorization";
  
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  
  HttpRequest.Builder build(HttpSinkConfig paramHttpSinkConfig);
}