package io.aiven.kafka.connect.http.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.apache.kafka.connect.errors.ConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
























class OAuth2HttpSender
  extends AbstractHttpSender
  implements HttpSender
{
  OAuth2HttpSender(HttpSinkConfig config, HttpClient httpClient, OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender) {
    super(config, new OAuth2AuthHttpRequestBuilder(config, oauth2AccessTokenHttpSender), httpClient);
  }




  
  protected HttpResponse<String> sendWithRetries(HttpRequest.Builder requestBuilder, HttpResponseHandler originHttpResponseHandler, int retries) {
    HttpResponseHandler handler = (response, remainingRetries) -> {
        if (response.statusCode() == 401 && remainingRetries > 0) {
          ((OAuth2AuthHttpRequestBuilder)this.httpRequestBuilder).renewAccessToken(requestBuilder);

          
          sendWithRetries(requestBuilder, originHttpResponseHandler, remainingRetries - 1);
        } else {
          originHttpResponseHandler.onResponse(response, remainingRetries);
        } 
      };
    
    return super.sendWithRetries(requestBuilder, handler, retries);
  }
  
  static class OAuth2AuthHttpRequestBuilder
    extends DefaultHttpSender.DefaultHttpRequestBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthHttpRequestBuilder.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    
    private final HttpSinkConfig config;
    
    private final OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender;
    
    private String accessToken;

    
    OAuth2AuthHttpRequestBuilder(HttpSinkConfig config, OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender) {
      this.config = config;
      this.oauth2AccessTokenHttpSender = oauth2AccessTokenHttpSender;
    }

    
    public HttpRequest.Builder build(HttpSinkConfig config) {
      return super.build(config)
        
        .header("Authorization", requestAccessToken());
    }





    
    void renewAccessToken(HttpRequest.Builder requestBuilder) {
      this.accessToken = null;
      requestBuilder.setHeader("Authorization", requestAccessToken());
    }






    
    private String requestAccessToken() {
      if (this.accessToken != null) {
        return this.accessToken;
      }
      LOGGER.info("Configure OAuth2 for URI: {} and Client ID: {}", this.config.oauth2AccessTokenUri(), this.config
          .oauth2ClientId());

      
      try {
        HttpResponse<String> response = this.oauth2AccessTokenHttpSender.call();
        this.accessToken = buildAccessTokenAuthHeader((String)response.body());
      } catch (IOException e) {
        throw new ConnectException("Couldn't get OAuth2 access token", e);
      } 
      return this.accessToken;
    }

    
    private String buildAccessTokenAuthHeader(String oauth2ResponseBody) throws JsonProcessingException {
      Map<String, String> accessTokenResponse = (Map<String, String>)OBJECT_MAPPER.readValue(oauth2ResponseBody, new TypeReference<Map<String, String>>() {  });
      if (!accessTokenResponse.containsKey(this.config.oauth2ResponseTokenProperty())) {
        throw new ConnectException("Couldn't find access token property " + this.config.oauth2ResponseTokenProperty() + " in response properties: " + accessTokenResponse
            .keySet());
      }
      String tokenType = accessTokenResponse.getOrDefault("token_type", "Bearer");
      String accessToken = accessTokenResponse.get(this.config.oauth2ResponseTokenProperty());
      return String.format("%s %s", new Object[] { tokenType, accessToken });
    }
  }
  
  class null extends TypeReference<Map<String, String>> {}
}