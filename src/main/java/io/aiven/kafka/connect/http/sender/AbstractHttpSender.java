package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





















abstract class AbstractHttpSender
{
  private static final Logger log = LoggerFactory.getLogger(AbstractHttpSender.class);
  
  protected final HttpClient httpClient;
  
  protected final HttpSinkConfig config;
  
  protected final HttpRequestBuilder httpRequestBuilder;
  
  protected AbstractHttpSender(HttpSinkConfig config, HttpRequestBuilder httpRequestBuilder, HttpClient httpClient) {
    this.config = Objects.<HttpSinkConfig>requireNonNull(config);
    this.httpRequestBuilder = Objects.<HttpRequestBuilder>requireNonNull(httpRequestBuilder);
    this.httpClient = Objects.<HttpClient>requireNonNull(httpClient);
  }

  
  public final HttpResponse<String> send(String body) {
    HttpRequest.Builder requestBuilder = this.httpRequestBuilder.build(this.config).POST(HttpRequest.BodyPublishers.ofString(body));
    return sendWithRetries(requestBuilder, HttpResponseHandler.ON_HTTP_ERROR_RESPONSE_HANDLER, this.config
        .maxRetries());
  }








  
  protected HttpResponse<String> sendWithRetries(HttpRequest.Builder requestBuilderWithPayload, HttpResponseHandler httpResponseHandler, int retries) {
    int remainingRetries = retries;
    while (remainingRetries >= 0) {

      
      try {
        HttpResponse<String> response = this.httpClient.send(requestBuilderWithPayload.build(), HttpResponse.BodyHandlers.ofString());
        log.debug("Server replied with status code {} and body {}", Integer.valueOf(response.statusCode()), response.body());
        
        httpResponseHandler.onResponse(response, remainingRetries);
        return response;
      } catch (IOException e) {
        log.info("Sending failed, will retry in {} ms ({} retries remain)", new Object[] { Integer.valueOf(this.config.retryBackoffMs()), 
              Integer.valueOf(remainingRetries), e });
        remainingRetries--;
        TimeUnit.MILLISECONDS.sleep(this.config.retryBackoffMs());
      }
      catch (InterruptedException e) {
        log.error("Sending failed due to InterruptedException, stopping", e);
        throw new ConnectException(e);
      } 
    } 
    log.error("Sending failed and no retries remain, stopping");
    throw new ConnectException("Sending failed and no retries remain, stopping");
  }
}