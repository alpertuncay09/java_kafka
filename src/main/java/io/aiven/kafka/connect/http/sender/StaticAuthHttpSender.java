package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

















class StaticAuthHttpSender
  extends AbstractHttpSender
  implements HttpSender
{
  StaticAuthHttpSender(HttpSinkConfig config, HttpClient client) {
    super(config, new StaticAuthHttpRequestBuilder(), client);
  }
  
  private static class StaticAuthHttpRequestBuilder
    extends DefaultHttpSender.DefaultHttpRequestBuilder
  {
    public HttpRequest.Builder build(HttpSinkConfig config) {
      return super
        .build(config)
        .header("Authorization", config.headerAuthorization());
    }
  }
}