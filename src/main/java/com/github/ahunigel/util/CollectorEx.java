package com.github.ahunigel.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Nigel.Zheng on 1/7/2021.
 * <p>
 * https://stackoverflow.com/questions/24630963/java-8-nullpointerexception-in-collectors-tomap
 *
 * @author Nigel.Zheng
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CollectorEx {
  /**
   * <pre>I wrote a Collector which, unlike the default java one, does not crash when you have null values:</pre>
   *
   * @author Emmanuel Touzery
   */
  public static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper,
                                                           Function<? super T, ? extends U> valueMapper) {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          Map<K, U> result = new HashMap<>();
          for (T item : list) {
            K key = keyMapper.apply(item);
            if (result.putIfAbsent(key, valueMapper.apply(item)) != null) {
              throw new IllegalStateException(String.format("Duplicate key %s", key));
            }
          }
          return result;
        });
  }

  /**
   * @author Tagir Valeev
   */
  public static <T, K, U> Collector<T, ?, Map<K, U>> toMapNullFriendly(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper) {
    @SuppressWarnings("unchecked")
    U none = (U) new Object();
    return Collectors.collectingAndThen(
        Collectors.<T, K, U>toMap(keyMapper,
            valueMapper.andThen(v -> v == null ? none : v)), map -> {
          map.replaceAll((k, v) -> v == none ? null : v);
          return map;
        });
  }

  /**
   * <pre>
   *   I have slightly modified Emmanuel Touzery's implementation.
   * This version;
   *
   * - Allows null keys
   * - Allows null values
   * - Detects duplicate keys (even if they are null) and throws IllegalStateException as in the original JDK implementation.
   * - Detects duplicate keys also when the key already mapped to the null value. In other words, separates a mapping with null-value from no-mapping.
   * </pre>
   *
   * @author mmdemirbas
   */
  public static <T, K, U> Collector<T, ?, Map<K, U>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
                                                                      Function<? super T, ? extends U> valueMapper) {
    return Collectors.collectingAndThen(
        Collectors.toList(),
        list -> {
          Map<K, U> map = new LinkedHashMap<>();
          list.forEach(item -> {
            K key = keyMapper.apply(item);
            if (map.containsKey(key)) {
              throw new IllegalStateException(String.format("Duplicate key %s", key));
            }
            map.put(key, valueMapper.apply(item));
          });
          return map;
        }
    );
  }

  /**
   * <pre>For completeness, I'm posting a version of the toMapOfNullables with a mergeFunction param:</pre>
   *
   * @author Henrik Langli
   */
  public static <T, K, U> Collector<T, ?, Map<K, U>> toMapOfNullables(Function<? super T, ? extends K> keyMapper,
                                                                      Function<? super T, ? extends U> valueMapper,
                                                                      BinaryOperator<U> mergeFunction) {
    return Collectors.collectingAndThen(Collectors.toList(), list -> {
      Map<K, U> result = new HashMap<>();
      for (T item : list) {
        K key = keyMapper.apply(item);
        U newValue = valueMapper.apply(item);
        U value = result.containsKey(key) ? mergeFunction.apply(result.get(key), newValue) : newValue;
        result.put(key, value);
      }
      return result;
    });
  }

  /**
   * <pre>
   *   Yep, a late answer from me, but I think it may help to understand what's happening under the hood in case anyone wants to code some other Collector-logic.
   *
   * I tried to solve the problem by coding a more native and straight forward approach. I think it's as direct as possible:
   * </pre>
   * In contrast to {@link Collectors#toMap(Function, Function)} the result map
   * may have null values.
   *
   * @author sjngm
   */
  public static <T, K, U, M extends Map<K, U>> Collector<T, M, M> toMapWithNullValues(Function<? super T, ? extends K> keyMapper,
                                                                                      Function<? super T, ? extends U> valueMapper) {
    return toMapWithNullValues(keyMapper, valueMapper, HashMap::new);
  }

  /**
   * In contrast to {@link Collectors#toMap(Function, Function, BinaryOperator, Supplier)}
   * the result map may have null values.
   *
   * @author sjngm
   */
  public static <T, K, U, M extends Map<K, U>> Collector<T, M, M> toMapWithNullValues(Function<? super T, ? extends K> keyMapper,
                                                                                      Function<? super T, ? extends U> valueMapper,
                                                                                      Supplier<Map<K, U>> supplier) {
    return new Collector<T, M, M>() {

      @Override
      public Supplier<M> supplier() {
        return () -> {
          @SuppressWarnings("unchecked")
          M map = (M) supplier.get();
          return map;
        };
      }

      @Override
      public BiConsumer<M, T> accumulator() {
        return (map, element) -> {
          K key = keyMapper.apply(element);
          if (map.containsKey(key)) {
            throw new IllegalStateException("Duplicate key " + key);
          }
          map.put(key, valueMapper.apply(element));
        };
      }

      @Override
      public BinaryOperator<M> combiner() {
        return (left, right) -> {
          int total = left.size() + right.size();
          left.putAll(right);
          if (left.size() < total) {
            throw new IllegalStateException("Duplicate key(s)");
          }
          return left;
        };
      }

      @Override
      public Function<M, M> finisher() {
        return Function.identity();
      }

      @Override
      public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));
      }

    };
  }

  /**
   * @author Igor Zubchenok
   */
  public static <T, K, V> Collector<T, HashMap<K, V>, HashMap<K, V>> toHashMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends V> valueMapper
  ) {
    return Collector.of(
        HashMap::new,
        (map, t) -> map.put(keyMapper.apply(t), valueMapper.apply(t)),
        (map1, map2) -> {
          map1.putAll(map2);
          return map1;
        }
    );
  }

  /**
   * @author Igor Zubchenok
   */
  public static <T, K> Collector<T, HashMap<K, T>, HashMap<K, T>> toHashMap(
      Function<? super T, ? extends K> keyMapper
  ) {
    return toHashMap(keyMapper, Function.identity());
  }
}
