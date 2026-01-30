package com.github.ahunigel.yaml;

import com.github.ahunigel.TestApp;
import com.github.ahunigel.TestDevice;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nigel on 2020/3/21.
 *
 * @author nigel
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {TestApp.class, TestDevice.class, YamlPropertySourceFactoryTest.YamlTestConfig.class})
public class YamlPropertySourceFactoryTest {
  @Autowired
  private Environment environment;
  @Autowired
  private TestDevice testDevice;
  @Value("${nz.int}")
  private int nzInt;
  @Value("${nz.float}")
  private float nzFloat;
  @Value("${nz.string}")
  private String nzString;

  @SneakyThrows
  @Test
  public void testBean() {
    TestDevice expectDevice = new TestDevice();
    expectDevice.setIp("192.168.168.1");
    expectDevice.setPort(8080);
    expectDevice.setPortSsl(8443);
    expectDevice.setUserName("nigel");
    expectDevice.setPassword("zheng");
    expectDevice.setMainApi("app/index.php");

    assertThat(testDevice).isEqualTo(expectDevice);
  }

  @SneakyThrows
  @Test
  public void testEnv() {
    assertThat(environment.getProperty("nz.int")).isNotNull().isEqualTo("5");
    assertThat(environment.getProperty("nz.float")).isNotNull().isEqualTo("6.0");
    assertThat(environment.getProperty("nz.string")).isNotNull().isEqualTo("nigel");
  }

  @SneakyThrows
  @Test
  public void testValueAnnotation() {
    assertThat(nzInt).isNotZero().isEqualTo(5);
    assertThat(nzFloat).isNotNaN().isEqualTo(6.0f);
    assertThat(nzString).isNotNull().isEqualTo("nigel");
  }

  @PropertySource(value = {"classpath:test.yml", "classpath:test-device.yml"}, factory = YamlPropertySourceFactory.class)
  public static class YamlTestConfig {
  }
}
