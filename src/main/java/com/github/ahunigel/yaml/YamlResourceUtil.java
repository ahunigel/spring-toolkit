package com.github.ahunigel.yaml;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.util.Map;
import java.util.Properties;

import static com.github.ahunigel.util.ResourceUtil.getName;

/**
 * Created by nigel on 2018/8/14.
 *
 * @author nigel
 */
public final class YamlResourceUtil {

  private YamlResourceUtil() {
    throw new InstantiationError();
  }

  public static Properties toProperties(EncodedResource resource) {
    return toProperties(resource.getResource());
  }

  public static Properties toProperties(Resource resource) {
    YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
    bean.setResources(resource);
    return bean.getObject();
  }

  public static PropertiesPropertySource toPropertiesSource(EncodedResource resource, String name) {
    return toPropertiesSource(resource.getResource(), name);
  }

  public static PropertiesPropertySource toPropertiesSource(Resource resource, String name) {
    return new PropertiesPropertySource(getName(name, resource), toProperties(resource));
  }

  public static Map<String, Object> toMap(EncodedResource resource) {
    return toMap(resource.getResource());
  }

  public static Map<String, Object> toMap(Resource resource) {
    YamlMapFactoryBean mapFactory = new YamlMapFactoryBean();
    mapFactory.setResources(resource);
    return mapFactory.getObject();
  }

  public static MapPropertySource toMapSource(EncodedResource resource, String name) {
    return toMapSource(resource.getResource(), name);
  }

  public static MapPropertySource toMapSource(Resource resource, String name) {
    return new MapPropertySource(getName(name, resource), toMap(resource));
  }

}
