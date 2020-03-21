package com.github.ahunigel.converter;

import org.springframework.beans.BeanUtils;

/**
 * Base converter, functional Spring converter
 * <p>
 * Created by nigel on 8/17/2018.
 *
 * @author nigel
 */
public abstract class BaseConverter<A, B> implements Converter<A, B> {
  @Override
  public B apply(A a) {
    return convert(a);
  }

  protected B convert(A a, B b) {
    BeanUtils.copyProperties(a, b);
    return b;
  }

  protected A reverseConvert(B b, A a) {
    BeanUtils.copyProperties(b, a);
    return a;
  }
}
