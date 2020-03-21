package com.github.ahunigel.converter;

import lombok.Data;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nigel on 2020/3/21.
 *
 * @author nigel
 */
public class BaseConverterTest {

  private FooBooBaseConverter converter;
  private Foo foo;
  private Boo boo;

  @Before
  public void setUp() {
    converter = new FooBooBaseConverter();
    foo = new Foo();
    foo.setF(6.8f);
    foo.setI(15);
    foo.setS("nigel");
    boo = new Boo();
    boo.setF(6.8f);
    boo.setI(15);
    boo.setS("nigel");
  }

  @Test
  public void testConvert() {
    assertThat(converter.convert(foo)).isNotNull().isEqualTo(boo);
    assertThat(converter.convert(foo, new Boo())).isNotNull().isEqualTo(boo);
    assertThat(converter.reverseConvert(boo, new Foo())).isNotNull().isEqualTo(foo);
  }

  @Test
  public void testFunction() {
    List<Boo> booList = Arrays.asList(foo).stream().map(converter).collect(Collectors.toList());
    booList.stream().forEach(b -> assertThat(b).isNotNull().isEqualTo(boo));
  }

  public static class FooBooBaseConverter extends BaseConverter<Foo, Boo> {
    @Override
    public Boo convert(Foo source) {
      Boo boo = new Boo();
      return convert(source, boo);
    }
  }

  @Data
  public static class Foo {
    private String s;
    private int i;
    private float f;
  }

  @Data
  public static class Boo {
    private String s;
    private int i;
    private float f;
  }
}
