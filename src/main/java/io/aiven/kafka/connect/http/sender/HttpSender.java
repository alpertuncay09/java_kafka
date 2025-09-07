package io.aiven.kafka.connect.http.sender;

import java.net.http.HttpResponse;

public interface HttpSender {
  HttpResponse<String> send(String paramString);
}