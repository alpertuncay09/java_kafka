/*    */ package io.aiven.kafka.connect.http.sender;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*    */ import io.aiven.kafka.connect.http.config.OAuth2AuthorizationMode;
/*    */ import io.aiven.kafka.connect.http.sender.request.OAuth2AccessTokenRequestForm;
/*    */ import java.net.URI;
/*    */ import java.net.http.HttpClient;
/*    */ import java.net.http.HttpRequest;
/*    */ import java.net.http.HttpResponse;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.time.Duration;
/*    */ import java.util.Base64;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class OAuth2AccessTokenHttpSender
/*    */   extends AbstractHttpSender
/*    */   implements HttpSender
/*    */ {
/*    */   OAuth2AccessTokenHttpSender(HttpSinkConfig config, HttpClient httpClient) {
/* 36 */     super(config, new AccessTokenHttpRequestBuilder(), httpClient);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   HttpResponse<String> call() {
/* 44 */     OAuth2AccessTokenRequestForm.Builder formBuilder = OAuth2AccessTokenRequestForm.newBuilder().withGrantTypeProperty(this.config.oauth2GrantTypeProperty()).withGrantType(this.config.oauth2GrantType()).withScope(this.config.oauth2ClientScope());
/*    */     
/* 46 */     if (this.config.oauth2AuthorizationMode() == OAuth2AuthorizationMode.URL) {
/* 47 */       formBuilder
/* 48 */         .withClientIdProperty(this.config.oauth2ClientIdProperty())
/* 49 */         .withClientId(this.config.oauth2ClientId())
/* 50 */         .withClientSecretProperty(this.config.oauth2ClientSecretProperty())
/* 51 */         .withClientSecret(this.config
/* 52 */           .oauth2ClientSecret()
/* 53 */           .value());
/*    */     }
/* 55 */     return send(formBuilder
/* 56 */         .build()
/* 57 */         .toBodyString());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static class AccessTokenHttpRequestBuilder
/*    */     implements HttpRequestBuilder
/*    */   {
/*    */     static final String HEADER_CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public HttpRequest.Builder build(HttpSinkConfig config) {
/* 72 */       HttpRequest.Builder builder = HttpRequest.newBuilder(Objects.<URI>requireNonNull(config.oauth2AccessTokenUri())).timeout(Duration.ofSeconds(config.httpTimeout())).header("Content-Type", "application/x-www-form-urlencoded");
/* 73 */       if (config.oauth2AuthorizationMode() == OAuth2AuthorizationMode.HEADER) {
/* 74 */         addClientIdAndSecretInRequestHeader(config, builder);
/*    */       }
/* 76 */       return builder;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private void addClientIdAndSecretInRequestHeader(HttpSinkConfig config, HttpRequest.Builder builder) {
/* 84 */       byte[] clientAndSecretBytes = (config.oauth2ClientId() + ":" + config.oauth2ClientId()).getBytes(StandardCharsets.UTF_8);
/*    */ 
/*    */       
/* 87 */       String clientAndSecretAuthHeader = "Basic " + Base64.getEncoder().encodeToString(clientAndSecretBytes);
/* 88 */       builder.header("Authorization", clientAndSecretAuthHeader);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\OAuth2AccessTokenHttpSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */