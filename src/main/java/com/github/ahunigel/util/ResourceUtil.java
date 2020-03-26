package com.github.ahunigel.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by nigel on 2018/8/14.
 *
 * @author nigel
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResourceUtil {

  public static String getNameForResource(EncodedResource resource) {
    return getNameForResource(resource.getResource());
  }

  public static String getNameForResource(Resource resource) {
    String name = resource.getDescription();
    if (!StringUtils.hasText(name)) {
      name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
    }
    return name;
  }

  public static PropertySource<?> loadPropertySource(String name, EncodedResource resource,
                                                     PropertySourceLoader sourceLoader) throws IOException {
    String resourceName = getName(name, resource);
    List<PropertySource<?>> testProperties = sourceLoader.load(resourceName, resource.getResource());
    PropertySource<?> propertySource = testProperties.stream().findFirst().orElse(null);

    return propertySource;
  }

  public static String getName(String name, EncodedResource resource) {
    return name != null ? name : getNameForResource(resource.getResource());
  }

  public static String getName(String name, Resource resource) {
    return name != null ? name : getNameForResource(resource);
  }
}
