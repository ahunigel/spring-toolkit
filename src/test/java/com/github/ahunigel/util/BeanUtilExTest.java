package com.github.ahunigel.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nigel on 2020/3/22.
 *
 * @author nigel
 */
public class BeanUtilExTest {
  private Foo foo;
  private Eoo eoo;
  private Doo doo;
  private Coo coo;

  private Boo boo;

  @Before
  public void setUp() {
    foo = new Foo();
    foo.setF(6.8f);
    foo.setI(15);
    foo.setS("nigel");
    eoo = new Eoo();
    eoo.setS("nigel");
    doo = new Doo("nigel");
    coo = new Coo("nigel");

    boo = new Boo();
    boo.setF(6.8f);
    boo.setI(15);
    boo.setS("nigel");
  }

  @Test
  public void superCopyProperties() {
    Boo target = new Boo();
    BeanUtilEx.copyProperties(foo, target);
    assertThat(target).isEqualTo(boo);
  }

  @Test
  public void superCopyPropertiesIgnoreProperties() {
    Boo target = new Boo();
    BeanUtilEx.copyProperties(foo, target, "s");
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "s");
  }

  /**
   * @deprecated should remove along with {@link BeanUtilEx#copyProperties(Object, Object, Predicate)}
   */
  @Deprecated
  @Test
  public void copyProperties_valueIgnoreFilter() {
    Boo target = new Boo();
    boo.setNothing("value");
    BeanUtilEx.copyProperties(foo, target, Objects::isNull);
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "nothing");
  }

  @Test
  public void copyPropertiesIgnoreValue() {
    Boo target = new Boo();
    boo.setNothing("value");
    BeanUtilEx.copyPropertiesIgnoreValue(foo, target, Objects::isNull);
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "nothing");
  }

  @Test
  public void copyPropertiesIgnoreName() {
    Boo target = new Boo();
    foo.setNothing("value");
    boo.setNothing("value");
    BeanUtilEx.copyPropertiesIgnoreName(foo, target, name -> name.length() == 1);
    assertThat(target).isNotEqualTo(boo).isEqualToComparingOnlyGivenFields(boo, "nothing");
  }

  @Test
  public void copyProperties_nameFilterAndValueFilter() {
    Boo target = new Boo();
    foo.setNothing("value");
    boo.setNothing("value");
    BeanUtilEx.copyProperties(foo, target, name -> name.length() > 1, Objects::nonNull);
    assertThat(target).isNotEqualTo(boo).isEqualToComparingOnlyGivenFields(boo, "nothing");
  }

  @Test
  public void copyProperties_nameFilterAndValueFilter_readMethodPrivate() {
    Boo target = new Boo();
    BeanUtilEx.copyProperties(eoo, target, name -> name.equals("s"), Objects::nonNull);
    assertThat(target).isNotEqualTo(boo).isEqualToComparingOnlyGivenFields(new Boo(), "s");
  }

  @Test
  public void copyProperties_nameFilterAndValueFilter_readMethodNoAccess() {
    Boo target = new Boo();
    BeanUtilEx.copyProperties(doo, target, name -> name.equals("s"), Objects::nonNull);
    assertThat(target).isNotEqualTo(boo).isEqualToComparingOnlyGivenFields(new Boo(), "s");
  }

  @Test
  public void copyProperties_nameFilterAndValueFilter_sourcePrivateClass() {
    Boo target = new Boo();
    BeanUtilEx.copyProperties(coo, target, name -> name.equals("s"), Objects::nonNull);
    assertThat(target).isNotEqualTo(boo).isEqualToComparingOnlyGivenFields(boo, "s");
  }

  @Test
  public void copyPropertiesIgnore_nameFilterAndValueFilter() {
    Boo target = new Boo();
    boo.setNothing("value");
    BeanUtilEx.copyPropertiesIgnore(foo, target, name -> name.equals("s"), Objects::isNull);
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "s", "nothing");
  }

  @Test
  public void copyPropertiesIgnore_nameFilterAndValueFilterAllFields() {
    Boo target = new Boo();
    boo.setNothing("value");
    BeanUtilEx.copyPropertiesIgnore(foo, target, name -> name.length() == 1, Objects::isNull);
    assertThat(target).isNotEqualTo(boo).isEqualTo(new Boo());
  }

  @Test
  public void copyPropertiesFilterName() {
    Boo target = new Boo();
    foo.setNothing("value");
    boo.setNothing("value");
    BeanUtilEx.copyPropertiesFilterName(foo, target, name -> name.length() == 1);
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "nothing");
  }

  @Test
  public void copyPropertiesFilterValue() {
    Boo target = new Boo();
    boo.setNothing("value");
    BeanUtilEx.copyPropertiesFilterValue(foo, target, Objects::nonNull);
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "nothing");
  }

  @Test
  public void copyNonNullPropertiesForSameClass() {
    Foo target = new Foo();
    Object newFoo = BeanUtilEx.copyNonNullPropertiesForSameClass(foo, target);
    assertThat(target).isEqualTo(foo).isEqualTo(newFoo);
  }

  @Test
  public void copyNonNullPropertiesForSameClass_whenNotSameClass() {
    Boo target = new Boo();
    Object newBoo = BeanUtilEx.copyNonNullPropertiesForSameClass(foo, target);
    assertThat(newBoo).isNull();
    assertThat(target).isNotEqualTo(boo).isEqualTo(new Boo());
  }

  @Test
  public void copyNonNullPropertiesForSameClass_whenNullSource() {
    Boo target = new Boo();
    Object newBoo = BeanUtilEx.copyNonNullPropertiesForSameClass(null, target);
    assertThat(newBoo).isNull();
    assertThat(target).isNotEqualTo(boo).isEqualTo(new Boo());
  }

  @Test
  public void copyNonNullPropertiesForSameClass_whenNullTarget() {
    Boo target = null;
    Object newBoo = BeanUtilEx.copyNonNullPropertiesForSameClass(boo, target);
    assertThat(newBoo).isNull();
    assertThat(target).isNull();
  }

  @Test
  public void copyNonNullProperties() {
    Boo target = new Boo();
    boo.setNothing("value");
    BeanUtilEx.copyNonNullProperties(foo, target);
    assertThat(target).isNotEqualTo(boo).isEqualToIgnoringGivenFields(boo, "nothing");
  }


  @Data
  public static class Foo {
    private String s;
    private int i;
    private float f;
    private String nothing;
  }

  @Data
  public static class Eoo {
    @Getter(AccessLevel.PRIVATE)
    private String s;
  }

  @RequiredArgsConstructor
  public static class Doo {
    private final String s;
  }

  @Data
  private static class Coo {
    private final String s;
  }

  @Data
  public static class Boo {
    private String s;
    private int i;
    private float f;
    private String nothing;
  }
}
