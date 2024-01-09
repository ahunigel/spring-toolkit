package com.github.ahunigel.util;

import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MockConfigurationPropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BinderTest {
  private final List<ConfigurationPropertySource> sources = new ArrayList<>();

  private final Binder binder = new Binder(this.sources);

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void bindToJavaBeanShouldReturnPopulatedBean() {
    this.sources.add(new MockConfigurationPropertySource("foo.value", "bar"));
    JavaBean result = this.binder.bind("foo", Bindable.of(JavaBean.class)).get();
    assertThat(result.getValue()).isEqualTo("bar");
  }

  static class JavaBean {

    private String value;

    private List<String> items = Collections.emptyList();

    String getValue() {
      return this.value;
    }

    void setValue(String value) {
      this.value = value;
    }

    List<String> getItems() {
      return this.items;
    }

  }

  @Test
  @SneakyThrows
  void bindYmlResource() {
    PropertySource<?> yamlPropSource = ResourceUtil.loadPropertySource("yml-test",
        new EncodedResource(new ClassPathResource("testP.yml")), new YamlPropertySourceLoader());
    this.sources.add(ConfigurationPropertySource.from(yamlPropSource));
    TestP result = this.binder.bind("nz", Bindable.of(TestP.class)).get();
    assertThat(result.getIntI()).isEqualTo(5);
    assertThat(result.getFloatF()).isEqualTo(6.0f);
    assertThat(result.getStringS()).isEqualTo("nigel");
  }

  @Data
  static class TestP {
    private int intI;
    private float floatF;
    private String stringS;
  }
}