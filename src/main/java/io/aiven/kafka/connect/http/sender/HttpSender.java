package io.aiven.kafka.connect.http.sender;

import java.net.http.HttpResponse;

public interface HttpSender {
  HttpResponse<String> send(String paramString);
}


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\sender\HttpSender.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */