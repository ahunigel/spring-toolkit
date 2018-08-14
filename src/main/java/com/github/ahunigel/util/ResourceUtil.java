package com.github.ahunigel.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

/**
 * Created by nigel on 2018/8/14.
 *
 * @author nigel
 */
public final class ResourceUtil {

  private ResourceUtil() {
    throw new InstantiationError();
  }

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
}
