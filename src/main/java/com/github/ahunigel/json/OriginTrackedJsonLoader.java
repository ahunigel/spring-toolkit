package com.github.ahunigel.json;

import com.github.ahunigel.yaml.YamlResourceUtil;
import com.google.common.io.Files;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by nigel on 2020/3/22.
 *
 * @author nigel
 */
public class OriginTrackedJsonLoader {
  private final Resource resource;

  public OriginTrackedJsonLoader(Resource resource) {
    this.resource = resource;
  }

  public List<Map<String, ?>> load() throws IOException {
    JsonParser parser = JsonParserFactory.getJsonParser();
    String json = Files.toString(resource.getFile(), StandardCharsets.UTF_8);
    Map<String, ?> map = parser.parseMap(json);
    return Collections.singletonList(map);
  }

  public List<Map<String, ?>> loadAsYaml() {
    Map<String, ?> properties = (Map) YamlResourceUtil.toProperties(resource);
    if (properties.isEmpty()) {
      return Collections.emptyList();
    }
    return Collections.singletonList(properties);
  }
}
