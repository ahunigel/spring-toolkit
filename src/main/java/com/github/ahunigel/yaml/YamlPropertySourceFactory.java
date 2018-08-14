package com.github.ahunigel.yaml;

import com.github.ahunigel.util.ResourceUtil;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.List;

/**
 * Created by nigel on 2018/8/9.
 *
 * @author nigel
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

  private YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
    String resourceName = name != null ? name : ResourceUtil.getNameForResource(resource.getResource());
    List<PropertySource<?>> yamlTestProperties = sourceLoader.load(resourceName, resource.getResource());
    PropertySource<?> yamlPropertySource = yamlTestProperties.stream().findFirst().orElse(null);

    return yamlPropertySource;
  }

}
