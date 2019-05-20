# spring-toolkit
[![](https://jitpack.io/v/ahunigel/spring-toolkit.svg)](https://jitpack.io/#ahunigel/spring-toolkit)

Provide an additional toolkit library for spring framework.

## Features
- `YamlPropertySourceFactory`
    - Spring `@PropertySource` does not support yaml by default, this factory help load yaml
- `ReversibleConverter<A, B>`
    - reverse converter with `.reverse()` method
    - functional converter, used for java `stream` mapping
    - instance of spring converter
- JUnit 4 `@IfProfileValue` profile source enhancement
    - `@ProfileValueSourceConfiguration(EnvironmentProfileValueSource.class)`, use environment as profile value source
    - `@ProfileValueSourceConfiguration(MergedSystemEnvAndPropertyProfileValueSource.class)`, use environment and system properties as profile value source
- JUnit 4 `@RunTestOnWindowsOnly` annotation, restrict JUnit 4 tests running only on windows operation system

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
public class FooApplication {}
```

```java
class FooConverter extends ReversibleConverter<Foo, Boo> {}
```

```java
@ProfileValueSourceConfiguration(EnvironmentProfileValueSource.class)
public class FooTest {
}
```

```java
@ProfileValueSourceConfiguration(MergedSystemEnvAndPropertyProfileValueSource.class)
public class FooTest {}
```

```java
@RunTestOnWindowsOnly
public class FooTest {}
```

## References
- [Using YAML Instead of Properties](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config-yaml)
- [Spring @PropertySource using YAML](https://stackoverflow.com/questions/21271468/spring-propertysource-using-yaml)
- [Spring YAML Configuration](https://www.baeldung.com/spring-yaml)

## TODOs

- @YamlPropertySource

- Support yaml for @TestPropertySource
