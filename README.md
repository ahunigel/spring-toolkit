# spring-toolkit
[![](https://jitpack.io/v/ahunigel/spring-toolkit.svg)](https://jitpack.io/#ahunigel/spring-toolkit)

Provide an additional toolkit library for spring framework.

Notes: All the testing features are moved to https://github.com/ahunigel/spring-test-toolkit

## Features
- `YamlPropertySourceFactory`
    - Spring `@PropertySource` does not support `yml` and `yaml` by default, this factory help load yaml files
- `ReversibleConverter<A, B>`
    - reverse converter with `.reverse()` method
    - functional converter, used for java `stream` mapping
    - instance of spring converter
- `BeanUtilEx`
    - Enhance the spring `BeanUtils`, provide `Predicate` as name or value filters for copy properties
- `JsonPropertySourceFactory`
    - Spring `@PropertySource` does not support `json` by default, this factory help load json files
- `CollectionUtilEx`
    - concat(c1, c2, ..., cN)
    - nullToEmpty(Collection c)

## How to use

### Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
## Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.ahunigel:spring-toolkit:{version}'
}
```
_Refer to https://jitpack.io/#ahunigel/spring-toolkit for details._

## Step 3. Sample code
```java
@PropertySource(value = {"classpath:custom.yml", "classpath:custom2.yml"}, factory = YamlPropertySourceFactory.class)
public class FooApplication {
}
```

```java
public class FooConverter extends ReversibleConverter<Foo, Boo> {
}
```

```java
public class FooTest {
  @Test
  public void testFoo() {
    assertThat(converter.convert(foo)).isNotNull().isEqualTo(boo);
    assertThat(converter.doForward(foo)).isNotNull().isEqualTo(boo);
    assertThat(converter.doBackward(boo)).isNotNull().isEqualTo(foo);
    assertThat(converter.convert(foo, new Boo())).isNotNull().isEqualTo(boo);
    assertThat(converter.reverseConvert(boo, new Foo())).isNotNull().isEqualTo(foo);
    assertThat(converter.reverse().convert(boo)).isNotNull().isEqualTo(foo);
    assertThat(converter.reverse().reverse().convert(foo)).isNotNull().isEqualTo(boo);
    
    List<Boo> booList = Arrays.asList(foo).stream().map(converter).collect(Collectors.toList());
    booList.stream().map(converter.reverse()).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
    booList.stream().map(converter.reverse().reversible().reverse()).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
    
    Iterable<Boo> booList = converter.convertAll(Arrays.asList(foo));
    booList.forEach(b -> assertThat(b).isNotNull().isEqualTo(boo));
    converter.reverse().convertAll(booList).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
  }
}
```

```java
public class FooUtil {
  public static void copy(Foo foo, Foo target) {
    BeanUtilEx.copyProperties(foo, target, name -> name.length() > 1, Objects::nonNull);
  }
}
```

```java
@PropertySource(value = {"classpath:custom.json", "classpath:custom.json"}, factory = JsonPropertySourceFactory.class)
public class FooApplication {
}
```


## References
- [Using YAML Instead of Properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-yaml)
- [Spring @PropertySource using YAML](https://stackoverflow.com/questions/21271468/spring-propertysource-using-yaml)
- [Spring YAML Configuration](https://www.baeldung.com/spring-yaml)
- [spring-test-toolkit](https://github.com/ahunigel/spring-test-toolkit)
- [Load spring-boot properties from json file](https://stackoverflow.com/questions/44564166/load-spring-boot-properties-from-json-file)

## TODOs

- @YamlPropertySource
- @JsonPropertySource
- Support yaml for @TestPropertySource

