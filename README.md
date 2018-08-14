# spring-security-oauth2-test
[![](https://jitpack.io/v/ahunigel/spring-toolkit.svg)](https://jitpack.io/#ahunigel/spring-toolkit)

Provide an additional toolkit library for spring framework.

## Features
- `YamlPropertySourceFactory`
    - Spring `@PropertySource` does not support yaml by default, this factory help load yaml

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
@PropertySource(value = "classpath:custom.yml", factory = YamlPropertySourceFactory.class)
```

## References
- [Using YAML Instead of Properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-yaml)
- [Spring @PropertySource using YAML](https://stackoverflow.com/questions/21271468/spring-propertysource-using-yaml)
- [Spring YAML Configuration](https://www.baeldung.com/spring-yaml)

## TODOs

- @YamlPropertySource

- Support yaml for @TestPropertySource
