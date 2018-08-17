package com.github.ahunigel.converter;

import org.springframework.beans.BeanUtils;

/**
 * Created by Nigel.Zheng on 8/17/2018.
 *
 * @author Nigel.Zheng
 */
public abstract class ReversibleConverter<A, B> extends com.google.common.base.Converter<A, B> implements Converter<A, B> {
  protected B convert(A a, B b) {
    BeanUtils.copyProperties(a, b);
    return b;
  }

  protected A reverseConvert(B b, A a) {
    BeanUtils.copyProperties(b, a);
    return a;
  }
}
