/*    */ package io.aiven.kafka.connect.http.sender;
/*    */ 
/*    */ import io.aiven.kafka.connect.http.config.HttpSinkConfig;
/*    */ import java.io.IOException;
/*    */ import java.net.http.HttpClient;
/*    */ import java.net.http.HttpRequest;
/*    */ import java.net.http.HttpResponse;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import org.apache.kafka.connect.errors.ConnectException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractHttpSender
/*    */ {
/* 36 */   private static final Logger log = LoggerFactory.getLogger(AbstractHttpSender.class);
/*    */   
/*    */   protected final HttpClient httpClient;
/*    */   
/*    */   protected final HttpSinkConfig config;
/*    */   
/*    */   protected final HttpRequestBuilder httpRequestBuilder;
/*    */   
/*    */   protected AbstractHttpSender(HttpSinkConfig config, HttpRequestBuilder httpRequestBuilder, HttpClient httpClient) {
/* 45 */     this.config = Objects.<HttpSinkConfig>requireNonNull(config);
/* 46 */     this.httpRequestBuilder = Objects.<HttpRequestBuilder>requireNonNull(httpRequestBuilder);
/* 47 */     this.httpClient = Objects.<HttpClient>requireNonNull(httpClient);
/*    */   }
/*    */ 
/*    */   
/*    */   public final HttpResponse<String> send(String body) {
/* 52 */     HttpRequest.Builder requestBuilder = this.httpRequestBuilder.build(this.config).POST(HttpRequest.BodyPublishers.ofString(body));
/* 53 */     return sendWithRetries(requestBuilder, HttpResponseHandler.ON_HTTP_ERROR_RESPONSE_HANDLER, this.config
/* 54 */         .maxRetries());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpResponse<String> sendWithRetries(HttpRequest.Builder requestBuilderWithPayload, HttpResponseHandler httpResponseHandler, int retries) {
/* 66 */     int remainingRetries = retries;
/* 67 */     while (remainingRetries >= 0) {
/*    */ 
/*    */       
/*    */       try {
/* 71 */         HttpResponse<String> response = this.httpClient.send(requestBuilderWithPayload.build(), HttpResponse.BodyHandlers.ofString());
/* 72 */         log.debug("Server replied with status code {} and body {}", Integer.valueOf(response.statusCode()), response.body());
/*    */         
/* 74 */         httpResponseHandler.onResponse(response, remainingRetries);
/* 75 */         return response;
/* 76 */       } catch (IOException e) {
/* 77 */         log.info("Sending failed, will retry in {} ms ({} retries remain)", new Object[] { Integer.valueOf(this.config.retryBackoffMs()), 
/* 78 */               Integer.valueOf(remainingRetries), e });
/* 79 */         remainingRetries--;
/* 80 */         TimeUnit.MILLISECONDS.sleep(this.config.retryBackoffMs());
/*    */       }
/* 82 */       catch (InterruptedException e) {
/* 83 */         log.error("Sending failed due to InterruptedException, stopping", e);
/* 84 */         throw new ConnectException(e);
/*    */       } 
/*    */     } 
/* 87 */     log.error("Sending failed and no retries remain, stopping");
/* 88 */     throw new ConnectException("Sending failed and no retries remain, stopping");
/*    */   }
/*    */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\AbstractHttpSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */