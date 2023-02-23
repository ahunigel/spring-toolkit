package com.github.ahunigel.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * bean convert utils, support convert <code>&lt;S&gt;</code> type to <code>&lt;T&gt;</code> type
 * <p>
 * Translated and extracted by ahunigel on 8/11/2022.
 *
 * @author bugpool
 * @see https://mp.weixin.qq.com/s/1jVb9z2zADtc2DnAV6cj7Q
 */
public class BeanConvertUtils {
  public static <S, T> T convertTo(S source, Supplier<T> targetSupplier) {
    return convertTo(source, targetSupplier, null);
  }

  /**
   * convert object
   *
   * @param source   source object
   * @param callBack callback method
   * @param <S>      source object type
   * @param <T>      target object type
   * @return target object
   */
  public static <S, T> T convertTo(S source, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
    if (null == source || null == targetSupplier) {
      return null;
    }

    T target = targetSupplier.get();
    copyProperties(source, target);
    if (callBack != null) {
      callBack.callBack(source, target);
    }
    return target;
  }

  public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier) {
    return convertListTo(sources, targetSupplier, null);
  }

  /**
   * convert a list of objects
   *
   * @param sources        source object list
   * @param targetSupplier target object supplier
   * @param callBack       callback method
   * @param <S>            source object type
   * @param <T>            target object type
   * @return target object list
   */
  public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
    if (null == sources || null == targetSupplier) {
      return null;
    }

    List<T> list = new ArrayList<>(sources.size());
    for (S source : sources) {
      T target = targetSupplier.get();
      copyProperties(source, target);
      if (callBack != null) {
        callBack.callBack(source, target);
      }
      list.add(target);
    }
    return list;
  }

  /**
   * callback interface
   *
   * @param <S> source object type
   * @param <T> target object type
   */
  @FunctionalInterface
  public interface ConvertCallBack<S, T> {
    void callBack(S t, T s);
  }
}
