package com.github.ahunigel.converter;

import java.util.function.Function;

/**
 * Created by nigel on 8/17/2018.
 *
 * @author nigel
 */
public interface Converter<A, B> extends org.springframework.core.convert.converter.Converter<A, B>, Function<A, B> {
}
