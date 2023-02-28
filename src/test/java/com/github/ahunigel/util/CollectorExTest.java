package com.github.ahunigel.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

/**
 * Created by Nigel.Zheng on 1/7/2021.
 *
 * @author Nigel.Zheng
 */
public class CollectorExTest {
  @Test
  public void toMapOfNullables_WhenHasNullKey() {
    assertEquals(singletonMap(null, "value"),
        Stream.of("ignored").collect(CollectorEx.toMapOfNullables(i -> null, i -> "value"))
    );
  }

  @Test
  public void toMapOfNullables_WhenHasNullValue() {
    assertEquals(singletonMap("key", null),
        Stream.of("ignored").collect(CollectorEx.toMapOfNullables(i -> "key", i -> null))
    );
  }

  @Test
  public void toMapOfNullables_WhenHasDuplicateNullKeys() {
    assertThatThrownBy(
        () -> Stream.of(1, 2, 3).collect(CollectorEx.toMapOfNullables(i -> null, i -> i))
    ).isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate key null");
  }

  @Test
  public void toMapOfNullables_WhenHasDuplicateKeys_NoneHasNullValue() {
    assertThatThrownBy(
        () -> Stream.of(1, 2, 3).collect(CollectorEx.toMapOfNullables(i -> "duplicated-key", i -> i))
    ).isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate key duplicated-key");
  }

  @Test
  public void toMapOfNullables_WhenHasDuplicateKeys_OneHasNullValue() {
    assertThatThrownBy(
        () -> Stream.of(1, null, 3).collect(CollectorEx.toMapOfNullables(i -> "duplicated-key", i -> i))
    ).isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate key duplicated-key");
  }

  @Test
  public void toMapOfNullables_WhenHasDuplicateKeys_AllHasNullValue() {
    assertThatThrownBy(
        () -> Stream.of(null, null, null).collect(CollectorEx.toMapOfNullables(i -> "duplicated-key", i -> i))
    ).isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate key duplicated-key");
  }

  @Test
  public void testToMapWithNullValues() throws Exception {
    Map<Integer, Integer> result = Stream.of(1, 2, 3)
        .collect(CollectorEx.toMapWithNullValues(Function.identity(), x -> x % 2 == 1 ? x : null));

    assertThat(result)
        .isExactlyInstanceOf(HashMap.class)
        .hasSize(3)
        .containsEntry(1, 1)
        .containsEntry(2, null)
        .containsEntry(3, 3);
  }

  @Test
  public void testToMapWithNullValuesWithSupplier() throws Exception {
    Map<Integer, Integer> result = Stream.of(1, 2, 3)
        .collect(CollectorEx.toMapWithNullValues(Function.identity(), x -> x % 2 == 1 ? x : null, LinkedHashMap::new));

    assertThat(result)
        .isExactlyInstanceOf(LinkedHashMap.class)
        .hasSize(3)
        .containsEntry(1, 1)
        .containsEntry(2, null)
        .containsEntry(3, 3);
  }

  @Test
  public void testToMapWithNullValuesDuplicate() throws Exception {
    assertThatThrownBy(() -> Stream.of(1, 2, 3, 1)
        .collect(CollectorEx.toMapWithNullValues(Function.identity(), x -> x % 2 == 1 ? x : null)))
        .isExactlyInstanceOf(IllegalStateException.class)
        .hasMessage("Duplicate key 1");
  }
}
