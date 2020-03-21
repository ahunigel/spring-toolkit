package com.github.ahunigel.converter;

import org.springframework.beans.BeanUtils;

/**
 * Reversible and functional Spring converter
 * <pre>
 *     assertThat(converter.convert(foo)).isNotNull().isEqualTo(boo);
 *     assertThat(converter.doForward(foo)).isNotNull().isEqualTo(boo);
 *     assertThat(converter.doBackward(boo)).isNotNull().isEqualTo(foo);
 *     assertThat(converter.convert(foo, new Boo())).isNotNull().isEqualTo(boo);
 *     assertThat(converter.reverseConvert(boo, new Foo())).isNotNull().isEqualTo(foo);
 *     assertThat(converter.reverse().convert(boo)).isNotNull().isEqualTo(foo);
 *     assertThat(converter.reverse().reverse().convert(foo)).isNotNull().isEqualTo(boo);
 *
 *     List<Boo> booList = Arrays.asList(foo).stream().map(converter).collect(Collectors.toList());
 *     booList.stream().map(converter.reverse()).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
 *     booList.stream().map(converter.reverse().reversible().reverse()).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
 *
 *     Iterable<Boo> booList = converter.convertAll(Arrays.asList(foo));
 *     booList.forEach(b -> assertThat(b).isNotNull().isEqualTo(boo));
 *     converter.reverse().convertAll(booList).forEach(f -> assertThat(f).isNotNull().isEqualTo(foo));
 * </pre>
 * <p>
 * Created by nigel on 8/17/2018.
 *
 * @author nigel
 */
public abstract class ReversibleConverter<A, B> extends com.google.common.base.Converter<A, B> implements Converter<A, B> {
  private transient ReversibleConverter<B, A> reversible;

  protected B convert(A a, B b) {
    BeanUtils.copyProperties(a, b);
    return b;
  }

  protected A reverseConvert(B b, A a) {
    BeanUtils.copyProperties(b, a);
    return a;
  }

  public ReversibleConverter<B, A> reverse() {
    return reversible();
  }

  protected ReversibleConverter<B, A> reversible() {
    ReversibleConverter<B, A> result = reversible;
    return (result == null) ? reversible = new ReverseReversibleConverter<A, B>(this) : result;
  }

  public static class ReverseReversibleConverter<A, B> extends ReversibleConverter<B, A> {
    final ReversibleConverter<A, B> original;

    ReverseReversibleConverter(ReversibleConverter<A, B> original) {
      this.original = original;
    }

    @Override
    protected A doForward(B b) {
      return original.doBackward(b);
    }

    @Override
    protected B doBackward(A a) {
      return original.doForward(a);
    }

    @Override
    protected ReversibleConverter<A, B> reversible() {
      return this.original;
    }
  }
}
