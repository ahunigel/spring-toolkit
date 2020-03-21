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
public class ReversibleConverterTest {

  private FooBooReversibleConverter converter;
  private Foo foo;
  private Boo boo;

  @Before
  public void setUp() {
    converter = new FooBooReversibleConverter();
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
    assertThat(converter.doForward(foo)).isNotNull().isEqualTo(boo);
    assertThat(converter.convert(foo, new Boo())).isNotNull().isEqualTo(boo);
    assertThat(converter.reverseConvert(boo, new Foo())).isNotNull().isEqualTo(foo);
    assertThat(converter.reversible().convert(boo)).isNotNull().isEqualTo(foo);
    assertThat(converter.reverse().convert(boo)).isNotNull().isEqualTo(foo);
    assertThat(converter.doBackward(boo)).isNotNull().isEqualTo(foo);
    assertThat(converter.reverse().reversible().convert(foo)).isNotNull().isEqualTo(boo);
  }

  @Test
  public void testFunction() {
    List<Boo> booList = Arrays.asList(foo).stream().map(converter).collect(Collectors.toList());
    booList.stream().forEach(b -> assertThat(b).isNotNull().isEqualTo(boo));

    booList.stream().map(converter.reverse()).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
    booList.stream().map(converter.reverse().reversible().reverse()).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
  }

  @Test
  public void testConvertAll() {
    Iterable<Boo> booList = converter.convertAll(Arrays.asList(foo));
    booList.forEach(b -> assertThat(b).isNotNull().isEqualTo(boo));

    converter.reverse().convertAll(booList).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
  }

  public static class FooBooReversibleConverter extends ReversibleConverter<Foo, Boo> {
    @Override
    protected Boo doForward(Foo foo) {
      Boo boo = new Boo();
      return convert(foo, boo);
    }

    @Override
    protected Foo doBackward(Boo boo) {
      Foo foo = new Foo();
      return reverseConvert(boo, foo);
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
