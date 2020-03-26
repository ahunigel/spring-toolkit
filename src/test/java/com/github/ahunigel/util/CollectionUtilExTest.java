package com.github.ahunigel.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by nigel on 2020/3/26.
 *
 * @author nigel
 */
public class CollectionUtilExTest {

  private Collection<String> c1;
  private Collection<String> c2;
  private List<String> l1;
  private List<String> listWithNull;
  private Set<String> s1;
  private Set<String> setWithNull;

  @Before
  public void setUp() {
    c1 = Lists.newArrayList("c1e1", "c1e2");
    c2 = Lists.newArrayList("c2e1", "c2e2", "c2e3");
    l1 = Lists.newArrayList("l1e1", "l1e2", "l1e3");
    listWithNull = Lists.newArrayList("l2e1", "l2e2", "l2e3", "l2e4", null); //size=6
    listWithNull.add(null);
    s1 = Sets.newHashSet("s1e1", "s1e2", "s1e3", "s1e4");
    setWithNull = Sets.newHashSet("s2e1", "s2e2", null); //size=3
    setWithNull.add(null);
  }

  @Test
  public void nullToEmpty() {
    Collection<?> actual = CollectionUtilEx.nullToEmpty(null);
    assertThat(actual).isEmpty();
  }

  @Test
  public void nullToEmpty_whenNotNull() {
    Collection<?> actual = CollectionUtilEx.nullToEmpty(c1);
    assertThat(actual).isNotEmpty().hasSameSizeAs(c1);
  }

  @Test
  public void concatNull() {
    Collection<String> actual = CollectionUtilEx.concat(null);
    assertThat(actual).isEmpty();
  }

  @Test
  public void concatWithNull() {
    Collection<String> actual = CollectionUtilEx.concat(c1, null);
    assertThat(actual).isNotEmpty().hasSameSizeAs(c1);
  }

  @Test
  public void concatWithNullElement() {
    Collection<String> actual = CollectionUtilEx.concat(c1, null, listWithNull, setWithNull);
    assertThat(actual).isNotEmpty().hasSize(c1.size() + listWithNull.size() + setWithNull.size());
  }

  @Test
  public void concatWithoutNullElement() {
    Collection<String> actual = CollectionUtilEx.concatWithoutNull(c1, null, listWithNull, setWithNull);
    assertThat(actual).isNotEmpty().hasSize(c1.size() + listWithNull.size() + setWithNull.size() - 3);
  }

  @Test
  public void concatAndDistinct() {
    Collection<String> actual = CollectionUtilEx.concatAndDistinct(c1, null, listWithNull, setWithNull);
    assertThat(actual).isNotEmpty().hasSize(c1.size() + listWithNull.size() + setWithNull.size() - 2);
  }

  @Test
  public void concatAndFilter() {
    Collection<String> actual = CollectionUtilEx.concatAndFilter(e -> nonNull(e) && e.endsWith("e3"),
        c1, c2, null, l1, listWithNull, null, s1, setWithNull);
    assertThat(actual).isNotEmpty().hasSize(4);
  }

  @Test
  public void concatNothing() {
    Collection<String> actual = CollectionUtilEx.concat();
    assertThat(actual).isEmpty();
  }

  @Test
  public void concatSomething() {
    Collection<String> actual = CollectionUtilEx.concat(c1, c2);
    assertThat(actual).isNotEmpty().hasSize(c1.size() + c2.size());
  }

  @Test
  public void concatListAndSet() {
    Collection<String> actual = CollectionUtilEx.concat(c1, l1, s1);
    assertThat(actual).isNotEmpty().hasSize(c1.size() + l1.size() + s1.size());
  }
}
