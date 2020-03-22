package com.github.ahunigel.json;

import com.github.ahunigel.util.ResourceUtil;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;

/**
 * Created by nigel on 2020/3/21.
 *
 * @author nigel
 */
public class JsonPropertySourceFactory implements PropertySourceFactory {
  private PropertySourceLoader sourceLoader = new JsonPropertySourceLoader();

  @Override
  public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
    return ResourceUtil.loadPropertySource(name, resource, sourceLoader);
  }

}
