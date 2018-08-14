package com.github.ahunigel.yaml;

import com.github.ahunigel.util.ResourceUtil;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * Created by nigel on 2018/8/9.
 *
 * @author nigel
 */
public abstract class YamlFileApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  protected abstract String getResourceLocation();

  private YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    try {
      Resource resource = applicationContext.getResource(getResourceLocation());

      List<PropertySource<?>> yamlTestProperties = sourceLoader.load(ResourceUtil.getNameForResource(resource), resource);
      yamlTestProperties.stream().forEach(propertySource ->
          applicationContext.getEnvironment().getPropertySources().addFirst(propertySource));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
