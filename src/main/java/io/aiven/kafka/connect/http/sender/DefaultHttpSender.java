package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.Objects;
















class DefaultHttpSender
  extends AbstractHttpSender
  implements HttpSender
{
  DefaultHttpSender(HttpSinkConfig config, HttpClient client) {
    super(config, new DefaultHttpRequestBuilder(), client);
  }


  
  static class DefaultHttpRequestBuilder
    implements HttpRequestBuilder
  {
    public HttpRequest.Builder build(HttpSinkConfig config) {
      HttpRequest.Builder httpRequest = HttpRequest.newBuilder(config.httpUri()).timeout(Duration.ofSeconds(config.httpTimeout()));

      
      Objects.requireNonNull(httpRequest); config.getAdditionalHeaders().forEach(httpRequest::header);
      if (config.headerContentType() != null) {
        httpRequest.header("Content-Type", config.headerContentType());
      }
      return httpRequest;
    }
  }
}