/*     */ package io.aiven.kafka.connect.http.sender.request;
/*     */ 
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
/*     */ import java.util.StringJoiner;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OAuth2AccessTokenRequestForm
/*     */ {
/*     */   private static final String SCOPE = "scope";
/*     */   private final String grantTypeProperty;
/*     */   private final String grantType;
/*     */   private final String scope;
/*     */   private final String clientIdProperty;
/*     */   private final String clientId;
/*     */   private final String clientSecretProperty;
/*     */   private final String clientSecret;
/*     */   
/*     */   private OAuth2AccessTokenRequestForm(String grantTypeProperty, String grantType, String scope, String clientIdProperty, String clientId, String clientSecretProperty, String clientSecret) {
/*  47 */     this.grantTypeProperty = grantTypeProperty;
/*  48 */     this.grantType = grantType;
/*  49 */     this.scope = scope;
/*  50 */     this.clientIdProperty = clientIdProperty;
/*  51 */     this.clientId = clientId;
/*  52 */     this.clientSecretProperty = clientSecretProperty;
/*  53 */     this.clientSecret = clientSecret;
/*     */   }
/*     */   
/*     */   public String toBodyString() {
/*  57 */     StringJoiner stringJoiner = (new StringJoiner("&")).add(encodeNameAndValue(this.grantTypeProperty, this.grantType));
/*  58 */     if (this.scope != null) {
/*  59 */       stringJoiner.add(encodeNameAndValue("scope", this.scope));
/*     */     }
/*  61 */     if (this.clientId != null && this.clientSecret != null) {
/*  62 */       stringJoiner
/*  63 */         .add(encodeNameAndValue(this.clientIdProperty, this.clientId))
/*  64 */         .add(encodeNameAndValue(this.clientSecretProperty, this.clientSecret));
/*     */     }
/*  66 */     return stringJoiner.toString();
/*     */   }
/*     */   
/*     */   private String encodeNameAndValue(String name, String value) {
/*  70 */     return String.format("%s=%s", new Object[] { encode(name), encode(value) });
/*     */   }
/*     */   
/*     */   private static String encode(String value) {
/*  74 */     return URLEncoder.encode(value, StandardCharsets.UTF_8);
/*     */   }
/*     */   
/*     */   public static Builder newBuilder() {
/*  78 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private String grantTypeProperty;
/*     */     
/*     */     private String grantType;
/*     */     
/*     */     private String scope;
/*     */     
/*     */     private String clientIdProperty;
/*     */     
/*     */     private String clientId;
/*     */     private String clientSecretProperty;
/*     */     private String clientSecret;
/*     */     
/*     */     public Builder withGrantTypeProperty(String grantTypeProperty) {
/*  97 */       this.grantTypeProperty = grantTypeProperty;
/*  98 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withGrantType(String grantType) {
/* 102 */       this.grantType = grantType;
/* 103 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withScope(String scope) {
/* 107 */       this.scope = scope;
/* 108 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withClientIdProperty(String clientIdProperty) {
/* 112 */       this.clientIdProperty = clientIdProperty;
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withClientId(String clientId) {
/* 117 */       this.clientId = clientId;
/* 118 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withClientSecretProperty(String clientSecretProperty) {
/* 122 */       this.clientSecretProperty = clientSecretProperty;
/* 123 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withClientSecret(String clientSecret) {
/* 127 */       this.clientSecret = clientSecret;
/* 128 */       return this;
/*     */     }
/*     */     
/*     */     public OAuth2AccessTokenRequestForm build() {
/* 132 */       Objects.requireNonNull(this.grantTypeProperty, "The grant type property is required");
/* 133 */       Objects.requireNonNull(this.grantType, "The grant type is required");
/*     */ 
/*     */       
/* 136 */       if (this.clientIdProperty != null || this.clientSecretProperty != null) {
/* 137 */         Objects.requireNonNull(this.clientIdProperty, "The client id property is required");
/* 138 */         Objects.requireNonNull(this.clientSecretProperty, "The client secret property is required");
/*     */       } 
/*     */       
/* 141 */       if (this.clientId != null || this.clientSecret != null) {
/* 142 */         Objects.requireNonNull(this.clientId, "The client id is required");
/* 143 */         Objects.requireNonNull(this.clientSecret, "The client secret is required");
/*     */       } 
/*     */       
/* 146 */       return new OAuth2AccessTokenRequestForm(this.grantTypeProperty, this.grantType, this.scope, this.clientIdProperty, this.clientId, this.clientSecretProperty, this.clientSecret);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\request\OAuth2AccessTokenRequestForm.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */