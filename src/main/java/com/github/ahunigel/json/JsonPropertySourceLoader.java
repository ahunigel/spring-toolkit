package com.github.ahunigel.json;

import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;

/**
 * Created by nigel on 2020/3/22.
 *
 * @author nigel
 */
public class JsonPropertySourceLoader extends YamlPropertySourceLoader implements PropertySourceLoader {
  @Override
  public String[] getFileExtensions() {
    return new String[]{"json"};
  }
}
