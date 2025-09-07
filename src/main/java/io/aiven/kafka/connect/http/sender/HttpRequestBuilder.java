package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.net.http.HttpRequest;

interface HttpRequestBuilder {
  public static final String HEADER_AUTHORIZATION = "Authorization";
  
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  
  HttpRequest.Builder build(HttpSinkConfig paramHttpSinkConfig);
}


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\HttpRequestBuilder.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */