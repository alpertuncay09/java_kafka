/*     */ package io.aiven.kafka.connect.http.sender;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*     */ import java.io.IOException;
/*     */ import java.net.http.HttpClient;
/*     */ import java.net.http.HttpRequest;
/*     */ import java.net.http.HttpResponse;
/*     */ import java.util.Map;
/*     */ import org.apache.kafka.connect.errors.ConnectException;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class OAuth2HttpSender
/*     */   extends AbstractHttpSender
/*     */   implements HttpSender
/*     */ {
/*     */   OAuth2HttpSender(HttpSinkConfig config, HttpClient httpClient, OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender) {
/*  44 */     super(config, new OAuth2AuthHttpRequestBuilder(config, oauth2AccessTokenHttpSender), httpClient);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpResponse<String> sendWithRetries(HttpRequest.Builder requestBuilder, HttpResponseHandler originHttpResponseHandler, int retries) {
/*  52 */     HttpResponseHandler handler = (response, remainingRetries) -> {
/*     */         if (response.statusCode() == 401 && remainingRetries > 0) {
/*     */           ((OAuth2AuthHttpRequestBuilder)this.httpRequestBuilder).renewAccessToken(requestBuilder);
/*     */ 
/*     */           
/*     */           sendWithRetries(requestBuilder, originHttpResponseHandler, remainingRetries - 1);
/*     */         } else {
/*     */           originHttpResponseHandler.onResponse(response, remainingRetries);
/*     */         } 
/*     */       };
/*     */     
/*  63 */     return super.sendWithRetries(requestBuilder, handler, retries);
/*     */   }
/*     */   
/*     */   static class OAuth2AuthHttpRequestBuilder
/*     */     extends DefaultHttpSender.DefaultHttpRequestBuilder {
/*  68 */     private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2AuthHttpRequestBuilder.class);
/*  69 */     private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
/*     */ 
/*     */     
/*     */     private final HttpSinkConfig config;
/*     */     
/*     */     private final OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender;
/*     */     
/*     */     private String accessToken;
/*     */ 
/*     */     
/*     */     OAuth2AuthHttpRequestBuilder(HttpSinkConfig config, OAuth2AccessTokenHttpSender oauth2AccessTokenHttpSender) {
/*  80 */       this.config = config;
/*  81 */       this.oauth2AccessTokenHttpSender = oauth2AccessTokenHttpSender;
/*     */     }
/*     */ 
/*     */     
/*     */     public HttpRequest.Builder build(HttpSinkConfig config) {
/*  86 */       return super.build(config)
/*     */         
/*  88 */         .header("Authorization", requestAccessToken());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void renewAccessToken(HttpRequest.Builder requestBuilder) {
/*  97 */       this.accessToken = null;
/*  98 */       requestBuilder.setHeader("Authorization", requestAccessToken());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private String requestAccessToken() {
/* 108 */       if (this.accessToken != null) {
/* 109 */         return this.accessToken;
/*     */       }
/* 111 */       LOGGER.info("Configure OAuth2 for URI: {} and Client ID: {}", this.config.oauth2AccessTokenUri(), this.config
/* 112 */           .oauth2ClientId());
/*     */ 
/*     */       
/*     */       try {
/* 116 */         HttpResponse<String> response = this.oauth2AccessTokenHttpSender.call();
/* 117 */         this.accessToken = buildAccessTokenAuthHeader((String)response.body());
/* 118 */       } catch (IOException e) {
/* 119 */         throw new ConnectException("Couldn't get OAuth2 access token", e);
/*     */       } 
/* 121 */       return this.accessToken;
/*     */     }
/*     */ 
/*     */     
/*     */     private String buildAccessTokenAuthHeader(String oauth2ResponseBody) throws JsonProcessingException {
/* 126 */       Map<String, String> accessTokenResponse = (Map<String, String>)OBJECT_MAPPER.readValue(oauth2ResponseBody, new TypeReference<Map<String, String>>() {  });
/* 127 */       if (!accessTokenResponse.containsKey(this.config.oauth2ResponseTokenProperty())) {
/* 128 */         throw new ConnectException("Couldn't find access token property " + this.config.oauth2ResponseTokenProperty() + " in response properties: " + accessTokenResponse
/* 129 */             .keySet());
/*     */       }
/* 131 */       String tokenType = accessTokenResponse.getOrDefault("token_type", "Bearer");
/* 132 */       String accessToken = accessTokenResponse.get(this.config.oauth2ResponseTokenProperty());
/* 133 */       return String.format("%s %s", new Object[] { tokenType, accessToken });
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends TypeReference<Map<String, String>> {}
/*     */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\OAuth2HttpSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */