package io.aiven.kafka.connect.http.sender;

import io.aiven.kafka.connect.http.config.HttpSinkConfig;
import io.aiven.kafka.connect.http.config.OAuth2AuthorizationMode;
import io.aiven.kafka.connect.http.sender.request.OAuth2AccessTokenRequestForm;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

















class OAuth2AccessTokenHttpSender
  extends AbstractHttpSender
  implements HttpSender
{
  OAuth2AccessTokenHttpSender(HttpSinkConfig config, HttpClient httpClient) {
    super(config, new AccessTokenHttpRequestBuilder(), httpClient);
  }




  
  HttpResponse<String> call() {
    OAuth2AccessTokenRequestForm.Builder formBuilder = OAuth2AccessTokenRequestForm.newBuilder().withGrantTypeProperty(this.config.oauth2GrantTypeProperty()).withGrantType(this.config.oauth2GrantType()).withScope(this.config.oauth2ClientScope());
    
    if (this.config.oauth2AuthorizationMode() == OAuth2AuthorizationMode.URL) {
      formBuilder
        .withClientIdProperty(this.config.oauth2ClientIdProperty())
        .withClientId(this.config.oauth2ClientId())
        .withClientSecretProperty(this.config.oauth2ClientSecretProperty())
        .withClientSecret(this.config
          .oauth2ClientSecret()
          .value());
    }
    return send(formBuilder
        .build()
        .toBodyString());
  }



  
  private static class AccessTokenHttpRequestBuilder
    implements HttpRequestBuilder
  {
    static final String HEADER_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";



    
    public HttpRequest.Builder build(HttpSinkConfig config) {
      HttpRequest.Builder builder = HttpRequest.newBuilder(Objects.<URI>requireNonNull(config.oauth2AccessTokenUri())).timeout(Duration.ofSeconds(config.httpTimeout())).header("Content-Type", "application/x-www-form-urlencoded");
      if (config.oauth2AuthorizationMode() == OAuth2AuthorizationMode.HEADER) {
        addClientIdAndSecretInRequestHeader(config, builder);
      }
      return builder;
    }




    
    private void addClientIdAndSecretInRequestHeader(HttpSinkConfig config, HttpRequest.Builder builder) {
      byte[] clientAndSecretBytes = (config.oauth2ClientId() + ":" + config.oauth2ClientId()).getBytes(StandardCharsets.UTF_8);

      
      String clientAndSecretAuthHeader = "Basic " + Base64.getEncoder().encodeToString(clientAndSecretBytes);
      builder.header("Authorization", clientAndSecretAuthHeader);
    }
  }
}