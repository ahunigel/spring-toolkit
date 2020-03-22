package com.github.ahunigel.json;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by nigel on 2020/3/22.
 *
 * @author nigel
 */
public class JsonPropertySourceLoader implements PropertySourceLoader {
  @Override
  public String[] getFileExtensions() {
    return new String[]{"json"};
  }

  @Override
  public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
    List<Map<String, ?>> loaded = new OriginTrackedJsonLoader(resource).loadAsYaml();
    if (loaded.isEmpty()) {
      return Collections.emptyList();
    }
    List<PropertySource<?>> propertySources = new ArrayList<>(loaded.size());
    for (int i = 0; i < loaded.size(); i++) {
      propertySources.add(new OriginTrackedMapPropertySource(
          name + (loaded.size() != 1 ? " (document #" + i + ")" : ""),
          loaded.get(i)));
    }

    return propertySources;
  }
}
