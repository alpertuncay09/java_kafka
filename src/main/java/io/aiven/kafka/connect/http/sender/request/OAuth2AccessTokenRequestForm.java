package io.aiven.kafka.connect.http.sender.request;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.StringJoiner;




























public class OAuth2AccessTokenRequestForm
{
  private static final String SCOPE = "scope";
  private final String grantTypeProperty;
  private final String grantType;
  private final String scope;
  private final String clientIdProperty;
  private final String clientId;
  private final String clientSecretProperty;
  private final String clientSecret;
  
  private OAuth2AccessTokenRequestForm(String grantTypeProperty, String grantType, String scope, String clientIdProperty, String clientId, String clientSecretProperty, String clientSecret) {
    this.grantTypeProperty = grantTypeProperty;
    this.grantType = grantType;
    this.scope = scope;
    this.clientIdProperty = clientIdProperty;
    this.clientId = clientId;
    this.clientSecretProperty = clientSecretProperty;
    this.clientSecret = clientSecret;
  }
  
  public String toBodyString() {
    StringJoiner stringJoiner = (new StringJoiner("&")).add(encodeNameAndValue(this.grantTypeProperty, this.grantType));
    if (this.scope != null) {
      stringJoiner.add(encodeNameAndValue("scope", this.scope));
    }
    if (this.clientId != null && this.clientSecret != null) {
      stringJoiner
        .add(encodeNameAndValue(this.clientIdProperty, this.clientId))
        .add(encodeNameAndValue(this.clientSecretProperty, this.clientSecret));
    }
    return stringJoiner.toString();
  }
  
  private String encodeNameAndValue(String name, String value) {
    return String.format("%s=%s", new Object[] { encode(name), encode(value) });
  }
  
  private static String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
  
  public static Builder newBuilder() {
    return new Builder();
  }

  
  public static class Builder
  {
    private String grantTypeProperty;
    
    private String grantType;
    
    private String scope;
    
    private String clientIdProperty;
    
    private String clientId;
    private String clientSecretProperty;
    private String clientSecret;
    
    public Builder withGrantTypeProperty(String grantTypeProperty) {
      this.grantTypeProperty = grantTypeProperty;
      return this;
    }
    
    public Builder withGrantType(String grantType) {
      this.grantType = grantType;
      return this;
    }
    
    public Builder withScope(String scope) {
      this.scope = scope;
      return this;
    }
    
    public Builder withClientIdProperty(String clientIdProperty) {
      this.clientIdProperty = clientIdProperty;
      return this;
    }
    
    public Builder withClientId(String clientId) {
      this.clientId = clientId;
      return this;
    }
    
    public Builder withClientSecretProperty(String clientSecretProperty) {
      this.clientSecretProperty = clientSecretProperty;
      return this;
    }
    
    public Builder withClientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }
    
    public OAuth2AccessTokenRequestForm build() {
      Objects.requireNonNull(this.grantTypeProperty, "The grant type property is required");
      Objects.requireNonNull(this.grantType, "The grant type is required");

      
      if (this.clientIdProperty != null || this.clientSecretProperty != null) {
        Objects.requireNonNull(this.clientIdProperty, "The client id property is required");
        Objects.requireNonNull(this.clientSecretProperty, "The client secret property is required");
      } 
      
      if (this.clientId != null || this.clientSecret != null) {
        Objects.requireNonNull(this.clientId, "The client id is required");
        Objects.requireNonNull(this.clientSecret, "The client secret is required");
      } 
      
      return new OAuth2AccessTokenRequestForm(this.grantTypeProperty, this.grantType, this.scope, this.clientIdProperty, this.clientId, this.clientSecretProperty, this.clientSecret);
    }
  }
}