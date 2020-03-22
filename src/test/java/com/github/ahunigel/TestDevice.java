package com.github.ahunigel;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestComponent;

@Data
@TestComponent
public class TestDevice {

  @Value("${test.device.ip:127.0.0.1}")
  private String ip;

  @Value("${test.device.port:80}")
  private int port;

  @Value("${test.device.portSSL:443}")
  private int portSsl;

  @Value("${test.device.username:user}")
  private String userName;

  @Value("${test.device.password:Emerson01}")
  private String password;

  @Value("${test.device.mainApi:cgi-bin/rci.cgi}")
  private String mainApi;
}
