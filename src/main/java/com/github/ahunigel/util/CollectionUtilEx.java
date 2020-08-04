package com.github.ahunigel.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

/**
 * Enhance the spring {@link CollectionUtils}, provide more useful utils
 * <p>
 * Created by nigel on 2020/3/26.
 *
 * @author nigel
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectionUtilEx extends CollectionUtils {

  public static <T> boolean isNotEmpty(Collection<T> t) {
    return !isEmpty(t);
  }

  public static <T> Collection<T> nullToEmpty(Collection<T> t) {
    return nonNull(t) ? t : Collections.emptyList();
  }

  public static <T> Collection<T> concat(Collection<T>... collections) {
    if (isNull(collections)) {
      return Collections.emptyList();
    }
    return Arrays.stream(collections).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toList());
  }

  public static <T> Collection<T> concatAndFilter(Predicate<T> filter, Collection<T>... collections) {
    return concat(collections).stream().filter(filter).collect(Collectors.toList());
  }

  public static <T> Collection<T> concatWithoutNull(Collection<T>... collections) {
    return concatAndFilter(Objects::nonNull, collections);
  }

  public static <T> Collection<T> concatAndDistinct(Collection<T>... collections) {
    return concat(collections).stream().collect(Collectors.toSet());
  }

  public static <K, V> Map<K, V> zipToMap(List<K> keys, List<V> values) {
    return IntStream.range(0, keys.size()).boxed()
        .collect(Collectors.toMap(keys::get, values::get));
  }
}
