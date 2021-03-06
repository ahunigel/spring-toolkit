package com.github.ahunigel.yaml;

import com.github.ahunigel.util.ResourceUtil;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

/**
 * Created by nigel on 2018/8/9.
 *
 * @author nigel
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

  private PropertySourceLoader sourceLoader = new YamlPropertySourceLoader();

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
    return ResourceUtil.loadPropertySource(name, resource, sourceLoader);
  }

}
