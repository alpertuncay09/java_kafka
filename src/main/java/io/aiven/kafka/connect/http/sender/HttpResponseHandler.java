package io.aiven.kafka.connect.http.sender;

import java.io.IOException;
import java.io.Serializable;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
















interface HttpResponseHandler
{
  public static final Logger LOGGER = LoggerFactory.getLogger(HttpResponseHandler.class);
  public static final HttpResponseHandler ON_HTTP_ERROR_RESPONSE_HANDLER;
  
  static {
    ON_HTTP_ERROR_RESPONSE_HANDLER = ((response, remainingRetries) -> {
        if (response.statusCode() >= 400) {
          HttpRequest request = response.request();
          Serializable uri = (request != null) ? request.uri() : "UNKNOWN";
          LOGGER.warn("Got unexpected HTTP status code: {} and body: {}. Requested URI: {}", new Object[] { Integer.valueOf(response.statusCode()), response.body(), uri });
          throw new IOException("Server replied with status code " + response.statusCode() + " and body " + (String)response.body());
        } 
      });
  }
  
  void onResponse(HttpResponse<String> paramHttpResponse, int paramInt) throws IOException;
}